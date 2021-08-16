import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import {  Modal, message as AntMessage,Empty,Spin,Descriptions,Card,Radio,Icon,Divider,Row,Button } from 'antd';
import { getDeclarativeAnswer,updateTestResult } from '../reduxFlow/actions.js';
const confirm = Modal.confirm;
let confirmUpdate = {};
class DescriptiveModal  extends Component {
	constructor(props) {
		super(props);
		this.state = {
				showModal: true,
				selectedList : {},
				loading : false
		};
	}
	
	componentDidMount(){
		this.loadModal();

	};

	loadModal = async() => {
		const { row,dispatch } = this.props;
		const { testAssignmentId } = row;
		this.setState( { loading : true});
		const data = await getDeclarativeAnswer(testAssignmentId , dispatch);
		this.setState( { dataList :  data,loading : false});
	};

	onClose=()=>{
		this.setState({ showModal: false });
		const { hideDeclarativeModal }  = this.props;
		hideDeclarativeModal();
	}

	setData = data =>{
		const { selectedList } = this.state;
	    Object.assign(selectedList ,data)
		this.setState( { selectedList  });
	}

	showConfirm = async () => {
		const { dataList } = this.state;
		if(!dataList || dataList.length == 0){
			this.onClose();
			return;
		}
		confirmUpdate = confirm({
			width: 480,
			title: 'Are you sure do you want to update the test result?',
			centered: true,
			onOk: async () => { await this.onConfirmClick(); }
		});
	}

	showCloseConfirm = async () => {
		const { dataList } = this.state;
		if(!dataList || dataList.length == 0){
			this.onClose();
			return;
		}
		confirm({
			width: 480,
			title: 'Do you want to close without correct descriptive answer?',
			content: 'Unsaved data will be lost',
			centered: true,
			onOk: () => { this.onClose(); },
		});
	}


	onConfirmClick = async () => {
		await this.updateTestResult().catch(this.handleError);
	};

	handleError = (err) => {
		AntMessage.error(`${err.message}`);
	}

	updateTestResult = async () => {
		const {  selectedList,dataList } =  this.state;
		const { dispatch,row,reload } = this.props;
		const { id , testAssignmentId } = row;
		const keysList = Object.keys(selectedList);
		dataList.forEach(data => { 
			const { id,validAnswer  } = data;
			if(keysList.indexOf(id.toString()) == -1 ){
				selectedList[id] = validAnswer;
			}});
		let param = {};
		param['updateMap'] = selectedList;
		param['summaryId'] = id;
		confirmUpdate.update({ cancelButtonProps : {disabled : true } });
		await updateTestResult(param,dispatch);
		confirmUpdate.update({ cancelButtonProps : {disabled : false } });
		reload();
		AntMessage.success('Test result updated successfully.');

	};
	
	onChange = e =>{
		const   { setData } = this.props;
		const { name,value }  = e.target;
		let selectedList = {};
		selectedList[parseInt(name)] = value ;
		this.setData(selectedList);
	}


	render() {
		const { showModal , selectedList,dataList,loading } = this.state;
		const { row } = this.props;
		return ( <div> 
		<Modal
		width={720}
		title="Correct Descriptive"
			visible={showModal}
		onCancel={this.showCloseConfirm} 
		onOk={this.showConfirm}
		maskClosable={false}
		>
		<Spin spinning = { loading } >
		{ dataList &&  dataList.length > 0 &&dataList.map( (item,index) =>
		 <Card>
		  <Descriptions title={<Button type="dashed" type="primary" shape="circle">{index+1}</Button>} bordered>
		    <Descriptions.Item  span={3} label="Question" >{item.question}</Descriptions.Item>
		    <Descriptions.Item span={3} label="Answer" ><p style={{ color : 'green' }}>{item.correctAnswer}</p></Descriptions.Item>
		    <Descriptions.Item  span={3} label="Candidate Answer"><p style={{ color : 'red' }}>{item.answer }</p></Descriptions.Item>
		  </Descriptions>
		   <Divider orientation="left">Choose <Icon type="check" /></Divider>
		   <Row justify='center' type='flex'> 
		  <Radio.Group name={item.id} defaultValue={item.validAnswer} onChange={this.onChange}>
	      <Radio value={true}>Right</Radio>
	      <Radio value={false}>Wrong</Radio>			     
	    </Radio.Group>
	    </Row>
		  </Card>
		)}
		{!dataList || dataList.length == 0 && <Empty /> }
		</Spin>
		</Modal>
		</div> );
	}
}

export default withRouter(connect(null)(DescriptiveModal));