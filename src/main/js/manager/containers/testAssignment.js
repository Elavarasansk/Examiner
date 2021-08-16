import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import TestAssignmentForm from '../components/testAssignmentForm';
import { Statistic, Input, Row, Table, Button, Icon, Tag, Col, Select, Divider,Modal,message as AntMessage,Tooltip  } from 'antd';
import {  getAllQuestionBank, getAllTestAssignment,rejectTest,getManagerCredits,searchCandidate } from '../reduxFlow/actions.js';
import PropTypes from 'prop-types';
import moment from 'moment';
import { ROLE } from '../../common/constants/userRoles';
import CandidateRegistrationDrawer from './candidateRegistrationDrawer';

const { Option } = Select;
const confirm = Modal.confirm;

const DATE_FORMAT = 'YYYY-MM-DD, hh:mm:ss A';

const statusFilter = [
  'New',
  'Inprogress',
  'Completed',
  'Rejected',
  'Expired',
];

const styles = {
	   select: {  
      width: 360,
      border: '2px solid',
      borderColor: 'royalblue',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5,
      marginTop:5
	    },
};

let confirmReject = {};

class TestAssignment extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      limit: 20,
      offset: 0,
      pagination: {},
      visible: false,
      selectedList : [] ,
      rejectedCount : 0,
      showCandidate : false
    };
  }
	componentDidMount = () => {
		  const param = {};
		  const { dispatch } = this.props;
		  const { limit, offset } = this.state;
		  Object.assign(param, { limit, offset });
		  getAllQuestionBank(dispatch);
		  getManagerCredits(dispatch);
		  searchCandidate(dispatch);
		  this.fetch(param);
	};
	
		fetch = async (param) => {
		  const { dispatch } = this.props;
		  const { pagination } = this.state;
		  this.setState({ loading: true });
		  const {totalElements } = await getAllTestAssignment(param, dispatch);
		  pagination.total = totalElements;
		  pagination.current = 0;
		  this.setState({ loading: false, pagination, param});
		};

		handleTableChange = async (pagination, filters, sorter) => {
		  const { dispatch } = this.props;
		  const currentPage = pagination.current;
		  const param = Object.assign(this.state, {
		    offset: currentPage - 1, limit: 10, sortKey: sorter.columnKey, sortType: sorter.order,
		  });
		   const {totalElements } = await getAllTestAssignment(param, dispatch);
		  pagination.total = totalElements;
		  this.setState({ pagination, sortKey: sorter.columnKey, sortType: sorter.order });
		};

		setTableData = () => {
		  const { dataList } = this.props;
		  if (!dataList || dataList.length == 0) {
		    return [];
		  }
		  const returnList = dataList.map(candidate => (Object.assign({
		    mailId: candidate.userAuthorityInfo.userCredentials.mailId,
			 questionBankName: candidate.questionBank.questionBankName,
		  }, candidate)));
		  return returnList;
		}

		closeTestAssignment = () => {
		  this.setState({ visible: false });
		}

		reloadTestAssignment = async () => {
			const { dispatch } = this.props;
		  this.setState({ visible: false });
		   await this.fetch(this.state);
		   getManagerCredits(dispatch);
		}


		handleFilterChange = async (type, name, value) => {
	      const dataMap = {};
	      dataMap[name] = value;
	      this.setState(dataMap);
		  const param = Object.assign(this.state, { offset: 0, limit: 20 }, dataMap);
		  await this.fetch(param);
		  this.setState({selectedList : [],rejectedCount : 0});
		  
		}

	showConfirm = async () => {
	 const  { rejectedCount } = this.state 
	 confirmReject = confirm({
	    width: 480,
	    title: `Are you sure do you want to reject ${rejectedCount} test?`,
	    centered: true,
	    onOk: async () => { await this.onConfirmClick(); }
	  });
	}	
	
	onConfirmClick = async() => {
	const  { selectedList } = this.state; 
	const  { dispatch } = this.props; 
	confirmReject.update({ cancelButtonProps : {disabled : true } });
	await rejectTest(selectedList,dispatch);
	confirmReject.update({ cancelButtonProps : {disabled : false } });
	AntMessage.success('Test rejected successfully.');
	this.setState({selectedList : [] ,rejectedCount : 0})
	this.reloadTestAssignment();
	}
	
   doReject = () => {
   this.showConfirm();
    }
   
   hideCandidate=() => {
	   this.setState({ showCandidate : false  });
   }

		render() {
			  const columns = [
		    {
		      title: 'Candidate',
		      dataIndex: 'mailId',
		    },
		    {
		      title: 'Question Bank',
		      dataIndex: 'questionBankName',
		    },
		    {
		      title: 'Questions Count',
		      dataIndex: 'questionsCount',
		       align: 'right',
		        sorter: true,
		    }, {
		      title: 'Invite Sent',
		      dataIndex: 'inviteSent',
		       render: row => (<span> {row ? <Icon type="check" /> : <Icon type="close" /> } </span>),
		       align: 'center',
		       sorter: true,
		     }, {
			      title: 'Start Date',
			      dataIndex: 'allowedStartDate',
			      render: allowedStartDate => (<span>{ allowedStartDate ? moment(allowedStartDate).format(DATE_FORMAT) : ''}</span>),
			      sorter: true,
			    },
		    {
			      title: 'End Date',
			      dataIndex: 'expirationTime',
			      render: expirationTime => (<span>{ expirationTime ? moment(expirationTime).format(DATE_FORMAT) : ''}</span>),
			      sorter: true,
			    },
		    {
		      title: 'Status',
		      dataIndex: 'status',
		       render: (status) => {
		      		switch (status) {
		          case 'New':
		            return (<span><Tag color="blue">{status}</Tag></span>);
		          case 'Inprogress':
		            return (<span><Tag color="gold">{status}</Tag></span>);
		          case 'Submitted':
		          case 'Completed':
		            return (<span><Tag color="green">{status}</Tag></span>);
		          case 'Expired':
		            return (<span><Tag color="#bfbfbf">{status}</Tag></span>);
		               case 'Rejected':
		            return (<span><Tag color="red">{status}</Tag></span>);
		          default:
		            return (<span />);
		        }
		     },
		     },
		  ];

		  const { dataList, total, questionBankList,managerCredits,searchCandidate } = this.props;
		  const { purchasedCredits,usedCredits } = managerCredits;

const { selectedList , rejectedCount} = this.state;
  const rowSelection = {
	    onChange: (selectedRowKeys, selectedRows) => {
	       let rejectedCount =  selectedRows.filter(data=>data.status === 'New').length;
	      this.setState({ selectedList: selectedRowKeys,rejectedCount : rejectedCount });
	    },
	    selectedRowKeys: selectedList,
	  };
	  
		  return (
  <div>
    { this.state.visible && <TestAssignmentForm
      closeTestAssignment={this.closeTestAssignment}
      reloadTestAssignment={this.reloadTestAssignment}
    /> }
    { this.state.showCandidate && <CandidateRegistrationDrawer
    	hideCandidate={this.hideCandidate} /> }
    
    <Row type="flex" justify="start" >
      <Select
      allowClear
      showSearch
        style={{ ...styles.select }}
        placeholder="Candidate"
        onChange={value=> this.handleFilterChange('select', 'mailId', value)}
         optionFilterProp="children"
          filterOption={(input, option) =>
        option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0 }>
        {searchCandidate.map(candidate => <Option key={candidate}>{candidate}</Option>) }
        </Select>
      <Select
        allowClear
        showSearch
        style={{ ...styles.select }}
        onChange={value => this.handleFilterChange('select', 'questionBankName', value)}
        placeholder="Question Bank"
        optionFilterProp="children"
        filterOption={(input, option) =>
      option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
    }
      >
        {questionBankList.map(question => <Option value={question}>{question}</Option>) }
      </Select>
      <Select
        allowClear
        style={{ ...styles.select}}
        onChange={value => this.handleFilterChange('select', 'status', value)}
        placeholder="Status"
      >
        { statusFilter.map(status => <Option value={status} >{status} </Option>) }
      </Select>

    </Row>
          <Divider />
    <Row type="flex" justify="space-between" style={{ marginTop: 8 }}>
      <Col span={2}>
        <Row type="flex" justify="start">
          <Statistic
            prefix={total == 0 ? <Icon theme="twoTone" twoToneColor="red" type="dislike" /> : <Icon theme="twoTone" twoToneColor="#52c41a" type="like" />}
            value={total}
          />
          </Row>
      </Col>
      <Col span={22}>
        <Row type="flex" justify="end">
        <span style={{marginTop : 4,marginRight : 8}}><h1>Credits</h1></span>
        <Statistic  style={{marginRight : 24}}  valueStyle={{ color: usedCredits == purchasedCredits ? 'red' : 'green' }} value={usedCredits} suffix={`/ ${purchasedCredits ? purchasedCredits : 0 }`} />
          <Tooltip placement="top" title={`Status with New will be Rejected`}>
         <Button type="danger" onClick= {this.doReject} style={{marginRight : 8}}   disabled={ rejectedCount > 0 ? false : true}>
            <Icon type="close" />Reject Test
          </Button>
           </Tooltip>
           <Button style={{ marginRight: 8 }} type="primary" onClick={() => this.setState({ showCandidate : true })} >
           <Icon type="user" /> Manage Candidate
         </Button>
          <Button type="primary" onClick={() => this.setState({ visible: true })}>
            <Icon type="plus" /> Assign Test
          </Button>
        </Row>
      </Col>
    </Row>

    <Row type="start" style={{marginTop : 16}} >
      <Table
        rowKey={record => record.id}
        rowSelection={rowSelection}
        style={{ backgroundColor:'floralwhite' }}
        bordered
        size="middle"
        columns={columns}
        dataSource={this.setTableData()}
        onChange={this.handleTableChange}
        loading={this.state.loading}
        pagination={this.state.pagination}
      />
    </Row>
  </div>
		  );
		}
}

function mapStateToProps(state) {
  const  {totalElements,content } = state.get('manager').toJS().getAllTestAssignment;
  return {
	    dataList: content,
	    total: totalElements,
	    questionBankList: state.get('manager').toJS().getAllQuestionBank,
	    managerCredits :  state.get('manager').toJS().getManagerCredits,
	    searchCandidate :  state.get('manager').toJS().searchCandidate
  };
}
export default withRouter(connect(mapStateToProps)(TestAssignment));