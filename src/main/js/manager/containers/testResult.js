import React, { Component } from 'react';
import { Row, Table, Button, Modal, Col, Statistic, Icon, Select, Divider,Tag,Descriptions, Tooltip,message as AntMessage } from 'antd';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { searchSummaryDetails, downloadReport,  getAllQuestionBank,searchCandidate,sendReportByMail } from '../reduxFlow/actions.js';
import TestResultChart from '../components/testResultChart.js';
import DescriptiveModal from '../components/descriptiveModal.js';

const FileSaver = require('file-saver');
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
	    detail: {
	    	color : '#000000',
	    	fontWeight: 600

	    }
};


class TestResult extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showAnalysisModal: false,
      chartData: [],
      loading: false,
      pagination: {},
      offset: 0,
      limit: 20,
      filter: {},
      showDeclarativeModal : false,
      row : {}
    };
  }

	componentDidMount = () => {
	  const { dispatch } = this.props;
	  const param = {};
	  const { limit, offset } = this.state;
	  Object.assign(param, { limit, offset });
	  getAllQuestionBank(dispatch);
	  searchCandidate(dispatch);
	  this.fetch(param);
	}


	fetch = async (param) => {
	  const { dispatch } = this.props;
	  const { pagination } = this.state;
	  this.setState({ loading: true });
	  const data = await searchSummaryDetails(param, dispatch);
	  pagination.total = data.count;
	  pagination.current = 0;
	  this.setState({ loading: false, pagination ,param});
	};


	handleSearch = async (text) => {
	  const { pagination } = this.state;
	  const param = Object.assign(this.state, { offset: 0, limit: 10 });
	  await this.fetch(param);
	  this.setState({ pagination });
	};


	showChart = (row) => {
	  const data = [
	    { item: 'Correct Answer', count: parseInt(row.answeredCorrect) },
	    { item: 'Not Answer', count: parseInt(row.unanswered) },
	    { item: 'Wrong Answer', count: parseInt(row.answeredWrong) },
	  ];
	  this.setState({ showAnalysisModal: true, chartData: data });
	};

	printResult = async (row) => {
	  const { dispatch } = this.props;
	  this.setState({ loading : true }); 
	  const file = await downloadReport(row.id, dispatch);
	  const filName = `${row.mailId}_${row.questionBankName}.pdf`;
	  FileSaver.saveAs(file, filName);
	  this.setState({ loading : false });
	};


	handleTableChange = async (pagination, filters, sorter) => {
	  const { dispatch } = this.props;
	  const currentPage = pagination.current;
	  const param = Object.assign(this.state, {
	    offset: currentPage - 1, limit: 10, sortKey: sorter.columnKey, sortType: sorter.order,
	  });
	    const data = await searchSummaryDetails(param, dispatch);
	  pagination.total = data.count;
	  this.setState({ pagination, sortKey: sorter.columnKey, sortType: sorter.order });
	};

	  handleFilterChange = async (type, name, value) => {
		    const { filter } = this.state;
		    const { questionBankName } = filter;
		    switch (type) {
		      case 'select':
		        filter[name] = value;
		        break;
		      case 'text':
		        filter[name] = value;
		        break;
		    }
		    await this.fetch(Object.assign(this.state, filter, { offset: 0, limit: 20 }));
		    this.setState({ filter });
		  }

	  closeReportModal = () => {
		  this.setState({ showAnalysisModal: false });
	  }
	  
	  showExpandedRow = (record) => {
	 
		  return   <Descriptions title={`${record.mailId} Summary`}  layout="horizontal"  size ='middle' bordered>
		       <Descriptions.Item label={<span style={{...styles.detail}}>Category</span>}>{record.category}</Descriptions.Item>
		       <Descriptions.Item label={<span style={{...styles.detail}}>Time Taken</span>} span={2}>{record.timeTaken}</Descriptions.Item>
		       <Descriptions.Item label={<span style={{...styles.detail}}>Sub Category</span>} >{record.subCategory}</Descriptions.Item>
		       <Descriptions.Item label={<span style={{...styles.detail}}>Test Date</span>}>{record.testDate}</Descriptions.Item>
		       </Descriptions>		  
	  }
	  
	  showDeclarativeAnswer = row => {
		  this.setState({ showDeclarativeModal : true,row : row });
	  };
	  
	  hideDeclarativeModal =()=>{
		  this.setState({ showDeclarativeModal : false });
	  }
	  
	  handleError = (err) => {
		    this.setState({ loading : false });
			AntMessage.error(`${err.message}`);
		}

	  reload =()=>{
		  this.setState({ showDeclarativeModal : false });
		  const param = Object.assign(this.state, { offset: 0, limit: 10 });
		  this.fetch(param);
	  }
	  
	  sendReport = async(row)=>{
		 const { id,mailId } = row;
		  const { dispatch } = this.props;
		  const mailSend = await sendReportByMail(id,dispatch);
		   if(mailSend){
			AntMessage.success(`Mail to the following candidate send successfully - '${mailId}'`);
		   }
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
		    },{
		    title: 'Test Result',
		    children: [
	    {
	      title: 'Questions Taken',
	      dataIndex: 'questionTaken',
	      align: 'right',
	      width : '5%',
	      sorter: true,
	    },
	    {
	      title: 'Answered Correct',
	      dataIndex: 'answeredCorrect',
	      align: 'right',
	      width : '5%',
	      render : answeredCorrect => <span><Tag color='green'>{answeredCorrect}</Tag></span>,
	      sorter: true,
	    },
	    {
	      title: 'Answered Wrong',
	      dataIndex: 'answeredWrong',
	      align: 'right',
	      render : answeredWrong => <span><Tag color='red'>{answeredWrong}</Tag></span>,
	      width : '5%',
	      sorter: true,
	    },
	    {
	      title: 'Unanswered',
	      dataIndex: 'unanswered',
	      align: 'right',
	      width : '5%',
	      render : unanswered => <span><Tag color='grey'>{unanswered}</Tag></span>,
	      sorter: true,
	    }]}, {
		      title: 'Test Date',
		      dataIndex: 'testDate',
		      sorter: true,
		      align : 'center'
		    },
//		    {
//		    title: 'Descriptive',
//		    align: 'center',
//		    render : row => <Button shape="circle" icon="edit" disabled ={!row.isDescriptive} type="primary" onClick={() => this.showDeclarativeAnswer(row)}  />
//			    },
	    {
	      title: 'Analysis',
	      align: 'center',
	      render: row => <span><Tooltip title="Download Report" ><Button shape="circle" icon="file-pdf"  type="primary" onClick={() => this.printResult(row).catch(this.handleError)} /></Tooltip>
	      						<Tooltip title="View Chart" ><Button shape="circle" icon="pie-chart" type="primary" onClick={() => this.showChart(row)} style={{marginLeft : 4}} /></Tooltip></span>,
	    },
	    {
	    	title: 'Mail',
		    align: 'center',
		    render : row =><Tooltip title="Mail Report" ><Button shape="circle" icon="mail" type="primary" onClick={() => this.sendReport(row).catch(this.handleError)}  /></Tooltip>
			    }
	  ];

	  const { showAnalysisModal, chartData, filter,showDeclarativeModal,row } = this.state;
	  const { dataList, total, questionBankList,searchCandidate  } = this.props;
	  const quesBankObj = questionBankList.size > 0 ? questionBankList.toJS() : [];
	  const { questionBankName, mailIdName } = filter || {};
	  
	  return (
  <div>
  { showDeclarativeModal &&  <DescriptiveModal reload={this.reload} row={row} hideDeclarativeModal={this.hideDeclarativeModal} /> } 
    {showAnalysisModal && <TestResultChart
      close={this.closeReportModal}
      showAnalysisModal={showAnalysisModal}
      chartData={chartData}
    />}
    <Row type="flex" justify="start">
    <Select
    allowClear
    showSearch
      style={{ ...styles.select }}
      placeholder="Candidate"
      onChange={value=> this.handleFilterChange('select', 'mailIdName', value)}
       optionFilterProp="children"
        filterOption={(input, option) =>
      option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0 }>
      {searchCandidate.map(candidate => <Option key={candidate}>{candidate}</Option>) }
      </Select>
      <Select
        allowClear
        showSearch
        placeholder="Question Bank"
        optionFilterProp="children"
        value={questionBankName ? questionBankName : undefined}
        style={styles.select}
        onChange={value => this.handleFilterChange('select', 'questionBankName', value)}
        filterOption={(input, option) =>
      option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}>
        {quesBankObj.map(data => <Option value={data}>{data}</Option>)}
      </Select>
    </Row>
    <Divider />

    <Row type="flex" justify="start">
      <Statistic
        prefix={total == 0 ? <Icon theme="twoTone" twoToneColor="red" type="dislike" /> : <Icon theme="twoTone" twoToneColor="#52c41a" type="like" />}
        value={total}
      />
    </Row>
   
    <Row type="start" style={{ marginTop: 16 }}>
      <Table rowKey={record => record.id}
        bordered size="small"
        columns={columns}
        style={{ backgroundColor:'floralwhite' }}
        dataSource={dataList}
        onChange={this.handleTableChange}
        loading={this.state.loading} 
        pagination={this.state.pagination}
        expandedRowRender={this.showExpandedRow}
      />
    </Row>
  </div>
	  );
	  }
}

function mapStateToProps(state) {
  return {
    dataList: state.get('manager').toJS().searchSummaryDetails.value,
    total: state.get('manager').toJS().searchSummaryDetails.count,
    questionBankList: state.get('manager').get('getAllQuestionBank'),
    searchCandidate :  state.get('manager').toJS().searchCandidate
  };
}


export default withRouter(connect(mapStateToProps)(TestResult));
