import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Drawer } from 'antd';
import CandidateDetails from '../components/candidateDetails.js'


const SINGLE_REGISTER = 'Single Register';
const BULK_REGISTER = 'Bulk Register';

class CandidateRegistrationDrawer extends Component {
	
	constructor(props) {
		super(props);
	}
	
	onClose = () => {
	  const { hideCandidate }	= this.props;
	   hideCandidate();
	};

	render() {
		return (
				<div>
				<Drawer
				title="Manage Candidate"
				width={560}
				onClose={this.onClose}
				visible={true}
				maskClosable={false} >
				<CandidateDetails />
				</Drawer>
				</div>
		);
	}
}

export default withRouter(connect(null)(CandidateRegistrationDrawer));
