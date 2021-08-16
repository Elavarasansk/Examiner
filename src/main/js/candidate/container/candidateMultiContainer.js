import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import Immutable from 'immutable';
import { Spin, Table, Tag, Button, Alert, Icon } from 'antd';
import { Select, message as AntMessage, Input, Divider } from 'antd';
import { getAllMyAssignedTest } from '../reduxFlow/actions';
import moment from 'moment';

import TestAgreement from '../components/multi/testAgreement';
import TestAttendMultiContainer from './testAttendMultiContainer';
const DATE_FORMAT = 'YYYY-MM-DD, hh:mm:ss A';

const styles={
	candidate: {
		color: 'whitesmoke',
		top: 20,
		position:'relative'
	},
	testCategory: {
		marginTop:30,
		marginRight:10
	},
	select:{
    width: 400,
    border: '2px solid',
    borderColor: 'royalblue',
    borderRadius: 6,
    marginRight: 5,
    marginBottom: 5,
    marginTop: 10,
  }
}

const { Search } = Input;

const statusFilter = [
  'New',
  'Inprogress',
  'Completed',
  'Rejected',
  'Expired',
];

class CandidateMultiContainer extends Component {
	
	constructor(props){
	  super(props);
		this.state = {
		    loading: false,
		    openAgreement: false,
		    openTest:false,
		    filter: {
		    }
		}
	}
	
	componentDidMount() {
	  this.fetchUserAssignedData().catch(this.handleError);
	}
	
	fetchUserAssignedData = async (e) => {
	  const { dispatch } = this.props;
	  const { filter } = this.state;
	  await getAllMyAssignedTest(dispatch, filter);
	}
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  compareFirstTimeGreater = (first, second) => {
    const firstDate = new Date(first);
    const secondDate = new Date(second);
    
    const firstTimeStamp = firstDate.getTime();
    const secondTimeStamp = secondDate.getTime();
    
    return (secondTimeStamp > firstTimeStamp);
  }
  
  handleStatusRender = (status) => {
    switch (status) {
      case 'New':
        return (<span><Tag color="blue">{status}</Tag></span>);
      case 'Inprogress':
        return (<span><Tag color="gold">{status}</Tag></span>);
      case 'Submitted':
      case 'Completed':
        return (<span><Tag color="green">{status}</Tag><Icon type="info-circle" /></span>);
      case 'Expired':
        return (<span><Tag color="#bfbfbf">{status}</Tag></span>);
      case 'Rejected':
        return (<span><Tag color="red">{status}</Tag></span>);
      default:
        return (<span />);
    }
  }
  
  takeUpTheTest = (text, row) => {
    const { expired, status } = row;
    const canWriteTest = (status.toUpperCase()==="NEW") || (status.toUpperCase()==="INPROGRESS")
    return <Button icon="play-circle" disabled={!canWriteTest} onClick={() => this.handleTakeUpTest(row)}/> 
  }
  
  handleTakeUpTest = (testDetails) => {
   this.setState({ testDetails, openAgreement: true });
  }
  
  handleFilterChange = (type, value) => {
    const { filter } = this.state;
    if(type==="category"){
      filter.subCategory = value;
      filter.questionBankName = value;
    }
    filter[type] = value;
    
    this.setState({ filter });
    this.fetchUserAssignedData().catch(this.handleError);
  }
  
  handleExpirationSorter = () => {
    
  }
  
  findAnyTestInProgress = () => {
    const myAssignedTests = this.props.myAssignedTests.toJS();
    const testData = myAssignedTests.filter(data => data.status.toLowerCase()==="inprogress");
    return testData;
  }
	
  render() {
    const { loading, openAgreement, testDetails, openTest } = this.state;
    const { myAssignedTests } = this.props;
    
    const testInProgress = this.findAnyTestInProgress();
    const proceedTest = (openTest || testInProgress.length>0);
    
    const testAssignmentColumn = [{
      title:'Categorisation',
      render: (text,row) => { 
        const { questionBankName, testCategory } = row.questionBank;
        return(<Tag color="blue">{testCategory.category} -> {testCategory.subCategory} -> {questionBankName}</Tag>);
      }
    }, {
      title:<span>Allowed to Start on <Icon type="caret-up" onClick={this.handleExpirationSorter}/></span>,
      dataIndex:'allowedStartDate',
      render: allowedStartDate => (<span>{ allowedStartDate ? moment(allowedStartDate).format(DATE_FORMAT) : ''}</span>),
      key:'allowedStartDate'
    }, {
      title: <span>Will Expire On <Icon type="caret-up" onClick={this.handleExpirationSorter}/></span>,
      dataIndex:'expirationTime',
      render: expirationTime => (<span>{ expirationTime ? moment(expirationTime).format(DATE_FORMAT) : ''}</span>),
      key:'expirationTime'
    }, {
      title:'Status',
      dataIndex:'status',
      key:'status',
      render:this.handleStatusRender
    }, {
      title:'Attend Test',
      render:this.takeUpTheTest
    }];
    
    
    return(<Spin spinning={loading} >
        <Search
          placeholder="Categorisation"
          style={styles.select}
          onChange={(e) => this.handleFilterChange('category',e.target.value)}
          enterButton
        />
        {false && <Select
          allowClear
          style={{ ...styles.select, width: 200, marginLeft: 8 }}
          onChange={value => this.handleFilterChange('status', value)}
          placeholder="Status"
        >
          { statusFilter.map(status => <Option value={status} >{status} </Option>) }
        </Select>}
        <Divider />
        <Table
          rowKey="id" 
          style={{ backgroundColor:'ivory' }}
          columns={testAssignmentColumn}
          dataSource={myAssignedTests.size>0 ? myAssignedTests.toJS():[]}
          bordered
          size={"middle"}
        />
        
        {openAgreement && <TestAgreement open={openAgreement} 
          testId={testDetails.id} 
          close={() => this.setState({ openAgreement: false})}
          proceedTest={() => this.setState({ openTest: true, testDetails })} />}
        
        {proceedTest && <TestAttendMultiContainer 
          open={proceedTest}
          assignedTest={openTest? Immutable.fromJS(testDetails): Immutable.fromJS(testInProgress[0])}
          close={() => this.setState({ openTest: false})}/>}
      </Spin>
    );
  }
}
          
function mapStateToProps(state) {
  return {
    myAssignedTests: state.get('candidate').get('allMyTests')
  };
}
export default withRouter(connect(mapStateToProps)(CandidateMultiContainer));
