import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Modal,Switch, message as AntMessage } from 'antd';
import CandidateBulkRegistration from './candidateBulkRegistration.js';
import WrappedCandidateSingleRegistration from './candidateSingleRegistration.js'

const confirm = Modal.confirm;

const SINGLE_REGISTER = 'Single Register';
const BULK_REGISTER = 'Bulk Register';

class CandidateRegistrationModal extends Component {
  constructor(props) {
    super(props);
    this.state = {
      viewType: 'Single',
      checkedChildren: SINGLE_REGISTER,
      unCheckedChildren: BULK_REGISTER,
      checked: true,
      showModal : true
    };
  }

  componentDidMount() {
	  this.setState({
	    viewType: 'Single',	checked: true,showModal:true
	  });
	  }

  onClose = () =>{
      this.showConfirm();
  }
  
	showConfirm = () => {
		  confirm({
		    width: 480,
		    title: 'Do you want to close without adding the Candidate?',
		    content: 'Unsaved data will be lost',
		    centered: true,
		    onOk: async () => { this.onConfirmClick(); },
		  });
		}

	onConfirmClick =  () => {
		 const { close }  =  this.props;
		  this.setState({ showModal : false }); 
		  close();
		};
		
	switchChange = (e) => {
	  const { viewType } = this.state;
	  viewType == 'Single' ? this.setState({ viewType: 'Bulk' }) : this.setState({ viewType: 'Single' });
	  this.setState({ checked: e });
	}

	render() {
	  const { showModal,viewType,checkedChildren, unCheckedChildren, checked } = this.state;
	  return (
  <div>
    <Modal
        title="Add Candidate"
        visible={showModal}
        onCancel={this.onClose}
        maskClosable={false}
        footer={null} >
      <Switch
        onChange={this.switchChange}
        style={{ float: 'right', marginBottom: 20 }}
        checkedChildren={checkedChildren}
        unCheckedChildren={unCheckedChildren}
        checked={checked}
        defaultChecked
      />
    {viewType == 'Single' && <WrappedCandidateSingleRegistration reload={this.props.reload} hideCandidate={this.onConfirmClick} /> } 
    {viewType == 'Bulk' && <CandidateBulkRegistration reload={this.props.reload} hideCandidate={this.onConfirmClick} /> }
    </Modal>
  </div>
	  );
	}
}


export default withRouter(connect(null)(CandidateRegistrationModal));
