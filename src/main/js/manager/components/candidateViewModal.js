import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Modal,Table } from 'antd';
import '../../common/styles/candidate.css'; 

class CandidateViewModal extends Component {
	constructor(props) {
		super(props);
		this.state = {};
	}


	  componentDidMount() {
		  this.setState({showModal:true});
		  }

	onClose = () =>{
		this.setState({showModal:false});
		this.props.hideViewModal();
	}

	render() {
	    const columns = [
	        {
	          title: 'Candidate'}
	      ];
		const { selectedList } = this.props;
		const { showModal }  = this.state;
		return (
				<div>
				<Modal
				title="Selected Candidate"
				visible={showModal}
				onCancel={this.onClose}
				maskClosable={false}
				footer={null} >
				 <Table 
			        id={"candidateView"}
			        bordered
			        style={{ border:'0px solid', backgroundColor:'white'}}
			        columns={columns} 
			        dataSource={selectedList}
			        />
				</Modal>
				</div>
		);
	}
}

export default withRouter(connect(null)(CandidateViewModal));
