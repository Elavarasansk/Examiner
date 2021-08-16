import React, { Component } from 'react';
import {  AutoComplete, Row, Col, DatePicker, InputNumber, Select, Form,Tooltip,TimePicker,Divider,Cascader } from 'antd';
import {  Steps,Button, Checkbox, Modal, Table, Input, message as AntMessage, Drawer,Badge,Icon,Switch } from 'antd';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import PropTypes from 'prop-types';
import { registerTest, searchCandidate, getAllCandidate, getQuestionBankCount, getQuestionBankCascader } from '../reduxFlow/actions.js';
import CandidateViewModal from './candidateViewModal.js';
import VerifyTestAssignment from './verifyTestAssignment.js';
import moment from 'moment';
const confirm = Modal.confirm;
const { Option } = AutoComplete;
const { RangePicker } = DatePicker;
const { Step } = Steps;



const formItemLayout = {
		labelCol: {
			xs: { span: 24 },
			sm: { span: 8 },
		},
		wrapperCol: {
			xs: { span: 24 },
			sm: { span: 16 },
		},
};

const steps = [
	{
		title: 'Assignment',
		icon : 'user'
	},
	{
		title: 'Verification',
		icon : 'solution'
	}
	];

const tailFormItemLayout = {
		wrapperCol: {
			xs: {
				span: 24,
				offset: 0,
			},
			sm: {
				span: 16,
				offset: 8,
			},
		},
};

const styles = {
		select: {
			border: '2px solid',
			borderColor: 'royalblue',
			borderRadius: 6,
			marginRight: 5,
			marginBottom: 5,
		},
};


const columns = [
	{
		key: 'mailId',
		title: 'Candidate',
		dataIndex: 'mailId',
	},
	];
let confirmUpdate = {};
class TestAssignmentForm extends Component {
	constructor(props) {
		super(props);
		this.state = {
				open: false,
				questionCount: 0,
				showCandidateModal: false,
				selectedList: [],
				loading: false,
				showCandidateViewModal : false,
				mailId : '',
				current: 0,
				timeOption : true 
		}; 
	}

	componentDidMount = () => {
		const { dispatch } = this.props;
		getAllCandidate(dispatch);
		searchCandidate(dispatch);
		getQuestionBankCascader(dispatch);
	}

	showModal = async () => {
		this.setState({ loading : true });
		this.setState({ showCandidateModal: true, loading : false });
	};

	showConfirm = async () => {
		confirmUpdate = confirm({
			width: 480,
			title: 'Are you sure do you want to assign the test?',
			centered: true,
			onOk: async () => { await this.onConfirmClick(); },
		});
	}

	showCloseConfirm = async () => {
		confirm({    width: 480,
			title: 'Do you want to close without assigning the test?',
			content: 'Unsaved data will be lost',
			centered: true,
			onOk: async () => { this.onClose(); }
		});
	}


	handleSubmit = async (e) => {
		e.preventDefault();
		let isValid = false;
		const { form } = this.props;
		const { selectedList }  = this.state; 
		form.validateFields(err => isValid = !err);
		if (!isValid) {
			return;
		}
		if(selectedList.length == 0 ){
			AntMessage.error('Please add the candidate for the test.');
			return;
		}
		let formData = form.getFieldsValue();		
		formData['questionBankPath'] =  this.state.questionBankPath;
		formData['questionBankId'] =  this.state.questionBankId;
		this.setState( { formData : formData } );
		this.next();
	};

	onConfirmClick = async () => {
		await this.registerTest().catch(this.handleError);
	};

	registerTest = async () => {
		const { form, dispatch,reloadTestAssignment } = this.props;
		const { selectedList,formData }  = this.state; 
		let param = Object.assign({},formData);
		const { testDate,allowedTimeTaken } = param ;
		Object.assign(param,{ assigneeList : selectedList,allowedStartDate : testDate[0], expirationTime : testDate[1] });
		const input  = allowedTimeTaken.format('HH:mm:ss'); 
		const data = input.split(':'); 
		const seconds = (+data[0]) * 60 * 60 + (+data[1]) * 60 + (+data[2]); //convert to seconds
		param['allowedTime'] = seconds;
		delete param.questionBankName;
		confirmUpdate.update({ cancelButtonProps : {disabled : true } });
		await registerTest(param, dispatch);
		confirmUpdate.update({ cancelButtonProps : {disabled : false } });
		AntMessage.success('Test Assigned successfully.');
		reloadTestAssignment();
	};

	handleError = (err) => {
		AntMessage.error(`${err.message}`);
	}

	handleOpenChange = (open) => {
		this.setState({ open });
	};

	handleClose = () => this.setState({ open: false });

	handleReset = () => {
		this.props.form.resetFields();
		this.setState({ questionBankName: null,selectedList:[] });
	};

	setQuestionCount = (e) => {
		this.setState({ questionCount: e });
	};

	showCandidateViewModal=()=>{
		this.setState({ showCandidateViewModal : true });
	};

	hideViewModal =()=>{
		this.setState({ showCandidateViewModal : false });
	}

	next=()=> {
		const current = this.state.current + 1;
		this.setState({ current });
	}

	prev=async()=> {
		const { form }= this.props;
		const { setFieldsValue } = form ;
		const { formData } = this.state;
		const current = this.state.current - 1;
		await this.setState({ current });
		setFieldsValue(formData);
	}

	handleOk = (e) => {
		const { selectedList } = this.state;
		this.props.form.setFields({
			assigneeList: {
				value: selectedList.length > 0 ? `${selectedList.length} candidates assign to a test` : null,
			},
		});	  
		this.setState({showCandidateModal: false});
	};

	handleCancel = (e) => {
		this.setState({showCandidateModal: false});
	};

	getQuestionBankCount = async (questionBankName) => {
		const { dispatch } = this.props;
		await getQuestionBankCount(questionBankName, dispatch);

	}
	setTableData = () => {
		const { getAllCandidate } = this.props;
		const { mailId } = this.state;

		if (!getAllCandidate || getAllCandidate.length == 0) {
			return [];
		}
		let returnList = getAllCandidate.map(candidate => {    
			const {  userCredentials } = candidate; 
			const { mailId } = userCredentials;
			return { mailId: mailId, key: mailId };
		});
		if(!mailId || mailId === ''){
			return returnList;
		}
		return  returnList.filter(data => data.key === mailId );
	}

	validateQuestionBank = (value,selectedOptions) => {
		if(value && value.length > 0 ){
			const questionBankPath =  value.slice(0,2).concat(selectedOptions[2]['label']).toString();
			const questionBankId = value[2];
			this.setState({ questionBankName: value,	questionBankPath : questionBankPath ,questionBankId : questionBankId });
			this.getQuestionBankCount(questionBankId);
		}else{
		    this.setState({ questionBankName: null });
		}
	};

	onClose = () => {
		this.handleReset();
		const { closeTestAssignment } = this.props;
		closeTestAssignment();
	};


	toggleSwitch = async( value ) => {
		const { form } = this.props;
		const { setFieldsValue } = form; 
		if(value){
			form.setFieldsValue({ allowedTimeTaken: null });

		}else{
			form.setFieldsValue({ allowedTimeTaken: null } );
		}
		await this.setState({ timeOption : value });
	};


	calculateTotalTime = () => {
		const { form } = this.props;
		const { avgTimeTaken,questionsCount } = form.getFieldsValue();
		if(!questionsCount || questionsCount == 0){
			AntMessage.error('Please set Question count.');
			return;
		}else if(!avgTimeTaken){
			AntMessage.error('Please set Average Time Taken.');
			return;
		}
		this.multiplyTime();
	}

	multiplyTime = () =>{
		const { form } = this.props;
		const { avgTimeTaken,questionsCount } = form.getFieldsValue();
		const totalTime = avgTimeTaken.clone();
		const timeSplit = avgTimeTaken.format('HH:mm:ss').split(':');
		const hours = parseInt(timeSplit[0]) * questionsCount;
		const minutes = parseInt(timeSplit[1]) * questionsCount;
		const seconds = parseInt(timeSplit[2]) * questionsCount;
		totalTime.set('hours', hours);
		totalTime.set('minutes', minutes);
		totalTime.set('seconds', seconds);
		form.setFieldsValue( { allowedTimeTaken  : totalTime  });	  
	}


	disabledDate = (current)=> {
		return current && current < moment().subtract(1,'day');
	}

	render() {
		const { getFieldDecorator } = this.props.form;
		const { timeOption } = this.state;
		const { questionCount, showCandidateModal, candidateList, questionBankName, 
			selectedList,showCandidateViewModal,current } = this.state;
		const { searchCandidate, questionBankList, totalQuestionCount,questionBankCascader } = this.props;
		const rowSelection = {
				onChange: (selectedRowKeys, selectedRows) => {
					this.setState({ selectedList: selectedRowKeys });
				},
				selectedRowKeys: selectedList,
		};	  
		const filter =  (inputValue, path) =>  {
			return path.some(option => option.label.toLowerCase().indexOf(inputValue.toLowerCase()) > -1);
		}
		let questionBankDataList =  questionBankCascader ? questionBankCascader : [] ; 
		return (
				<div>
				<Drawer
				title="Test Assignment"
					width={620}
				onClose={this.showCloseConfirm}
				visible
				maskClosable={false}>
				{showCandidateModal && <Modal
					title="Candidate List"
						visible={showCandidateModal}
				onOk={this.handleOk}
				onCancel={this.handleCancel}>
				<Row >
				<Col>
				<Select
				showSearch
				allowClear
				placeholder="Candidate"
				style={{ width: 360 ,...styles.select}}
				onSelect={(value, option) => this.setState({ mailId: value }) }
				onChange={value =>  this.setState({ mailId: value })}
				optionFilterProp="children"
					filterOption={(input, option) =>
					option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0 }>
				{searchCandidate.map(candidate => <Option key={candidate}>{candidate}</Option>) }
				</Select>
				</Col>
				</Row>
				<Row style={{ marginTop: 16 }}>
				<Col>
				<Table
				bordered
				rowSelection={rowSelection}
				columns={columns}
				loading={this.state.loading} 
				dataSource={this.setTableData()}
				size="middle"
				/>
				</Col>
				</Row>
				</Modal>}
				<Row>
				<Steps current={current}>
				{steps.map(item => (
						<Step key={item.title} title={item.title} icon={<Icon type={item.icon} />} />
				))}
						</Steps>
						</Row>
						{  showCandidateViewModal &&  <CandidateViewModal hideViewModal={this.hideViewModal} selectedList={selectedList} /> }
						{ current == 0 &&  
							<Form onSubmit={this.handleSubmit} style={{ marginTop : 16 }}>
						<Form.Item label="Question Bank" {...formItemLayout}>
						{getFieldDecorator('questionBankName', {
							rules: [{ required: true, message: 'Please input Question Bank!' }],
						})( <Cascader
								options={questionBankDataList}
						value={questionBankName}
						onChange={this.validateQuestionBank}
						expandTrigger="hover"
							placeholder="Question Bank"
								showSearch={{filter}}
						/> )}
						</Form.Item>
						<Form.Item label="Candidate" {...formItemLayout}>
						<Row gutter={8} justify="start" type="flex">
						<Col>
						{getFieldDecorator('assigneeList', {
							rules: [{ required: false}],
						})
						(<Input placeholder="Click user button to add candidate"
							disabled style={{ width: 248, color : 'forestgreen' }}></Input>)}
						</Col>
						<Col >
						<Button disabled={!questionBankName} shape="circle" type="primary" icon="usergroup-add" onClick={this.showModal} />
						{ selectedList.length > 0 && <Tooltip  placement="topRight" title='Click to view selected candidates'>
							<Badge count={selectedList.length}>
						<Button shape="circle" type="primary" icon="info-circle" style={{ marginLeft : 2 }}  onClick={this.showCandidateViewModal} />  </Badge> </Tooltip> }
						</Col>
						</Row>
						</Form.Item>

						<Form.Item label="Test Date" {...formItemLayout} >
						{getFieldDecorator('testDate', {
							rules: [{ required: true, message: 'Please input Test Date!' }],})
							(<RangePicker
									disabledDate={this.disabledDate}
							disabled={!questionBankName}
							showTime={{
								hideDisabledOptions: true,
							}}
							format="YYYY-MM-DD HH:mm"
								/> )}
								</Form.Item>

								<Divider orientation="left">{<Icon type="clock-circle" />} Set Total Time</Divider>
								<Row justify="end" type="flex">
								<Form.Item>
								{getFieldDecorator('timeOption', { })( 
										<Switch disabled={!questionBankName} checkedChildren="Auto" onChange={this.toggleSwitch}
										unCheckedChildren="Manual" defaultChecked />)}
								</Form.Item>
								</Row>

								<Form.Item label="Question count" {...formItemLayout}>
								<Row gutter={8} justify="start" type="flex">
								<Col>
								{getFieldDecorator('questionsCount', {
									rules: [{ required: true, message: 'Please input Question count!' }],
								})(<InputNumber placeholder='Count'
									min={1}
								max={totalQuestionCount}
								disabled={!questionBankName}
								onChange={e => this.setQuestionCount(e)}
								/>)}
								</Col>
								<Col >
								{ questionBankName &&
									<Tooltip placement="rightTop" title={`Max Question Count ( ${totalQuestionCount} )`}>
								<Button  shape="circle" icon="info-circle" />
									</Tooltip> }
								</Col>
								</Row>
								</Form.Item>

								{ timeOption  && 
									<Form.Item label="Average Time Taken" {...formItemLayout}>
								<Row gutter={8} justify="start" type="flex">
								<Col>
								{getFieldDecorator('avgTimeTaken', {
									rules: [{ required:true , message: 'Please input Average Time Taken!' }],
								})( <TimePicker  
										placeholder='Avg Time' format={'HH:mm:ss'}  disabled={!questionBankName}
								defaultOpenValue={moment('00:00:01', 'HH:mm:ss')} />)}
								</Col>
								<Col >
								{ questionBankName &&
									<Button ghost type="primary" icon="calculator" onClick={this.calculateTotalTime}>Calculate</Button>
								}
								</Col>
								</Row>
								</Form.Item>            
								}

								<Form.Item label="Total Time" {...formItemLayout}>
								{getFieldDecorator('allowedTimeTaken', {
									rules: [{ required: true, message: 'Please calculate Total Time!' }],
								})( <TimePicker  
										placeholder='Total Time' format={'HH:mm:ss'}
								disabled={timeOption} defaultOpenValue={moment('00:00:01', 'HH:mm:ss')} />)}
								</Form.Item>
								<Divider/>            

								<Form.Item {...tailFormItemLayout}>
								{getFieldDecorator('inviteSent', {
									valuePropName: 'checked',
								})(<Checkbox disabled={!questionBankName}>
								Send mail to the assigned candidates.
								</Checkbox>)}
								</Form.Item>
								<Form.Item {...tailFormItemLayout}>
								<Button style={{ marginRight: 8 }} onClick={this.handleReset}>Clear</Button>
								<Button type="primary" htmlType="submit" >Next</Button>
								</Form.Item>
								</Form> } 
								{ current == 1 &&
									<Row>
								<VerifyTestAssignment param={this.state.formData} />
								<div style={{ margin: 'auto', width: '50%',paddingTop : 16 }}>
								<Button style={{ marginRight: 8 }} onClick={this.prev}>Previous</Button>
								<Button type="primary" onClick={this.showConfirm} >Assign</Button>
								</div>
								</Row>
								}
								</Drawer>
								</div>
		);
						}
				}


				function mapStateToProps(state) {
					const  {count,value } = state.get('manager').toJS().getAllCandidate;
					return {
						searchCandidate: state.get('manager').toJS().searchCandidate,
						getAllCandidate: value,
						total : count,
						totalQuestionCount: state.get('manager').toJS().getQuestionBankCount,
						questionBankCascader : state.get('manager').toJS().getQuestionBankCascader

					};
				}

				const TestAssignmentContainer = Form.create({ name: 'test_assignment_form' })(TestAssignmentForm);
				export default withRouter(connect(mapStateToProps)(TestAssignmentContainer));
