import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Row, Col, Icon } from 'antd';
import '../../common/styles/dashboard.css';

class AmazonComponent extends Component {
	render() {
		return(
				<Row>
				
					<Row type="flex" justify="center" >
						<Icon type="lock" style={{ fontSize: 60 }} />
					</Row>
					<Row type="flex" justify="center" >
						<div className='title' >
							Security provided by Amazon EC2 
						</div>
					</Row>
					<Row type="flex" justify="center" >
						<div style={{ fontSize: 20 }} ><b>Our Dedicated team guides you from start to finish</b></div> 
					</Row>
					<Row type="flex" justify="center" >
						<div className="centered" style={{ marginTop: 10, fontSize: 20, width: '80%' }}>
							VEXAMINE will provide you with the requested number of tests as per the purchased package.Once the test has been initiated,the test taker will get a link,username and password.As soon as the test has been successfully completed by the candidate,the test reslts are automatically sent to test initiator which can be downloaded.
						</div>
						<div className="centered" style={{ marginTop: 15, fontSize: 20, width: '80%' }}>
							The informations provided are completely secured in Elastic Compute Cloud.Amazon EC2 is a web service that provides secure, resizable compute capacity in the cloud. It is designed to make web-scale cloud computing easier for developers.
						</div>
						<div className="centered" style={{ marginTop: 15, fontSize: 20, width: '80%' }}>
							Amazon EC2 offers a highly reliable environment where replacement instances can be rapidly and predictably commissioned. The service runs within Amazon’s proven network infrastructure and data centers. The Amazon EC2 Service Level Agreement commitment is 99.99% availability for each Amazon EC2 Region.
						</div>
						<div className="centered" style={{ marginTop: 15, fontSize: 20, width: '80%' }}>
							Cloud security at AWS is the highest priority. As an AWS customer, you will benefit from a data center and network architecture built to meet the requirements of the most security-sensitive organizations. Amazon EC2 works in conjunction with Amazon VPC to provide security and robust networking functionality for your compute resources. 
						</div>
					</Row>
				</Row>
		);
	}
}

function mapStateToProps(state) {
	return {};
}

export default withRouter(connect()(AmazonComponent));