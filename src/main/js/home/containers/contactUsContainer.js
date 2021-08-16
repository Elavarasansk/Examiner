import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Card, Row, Col, Input, Button, Icon } from 'antd';
import '../../common/styles/dashboard.css';
import ContactForm from '../components/contactFormComponent';

const { TextArea } = Input;

class ContactUsContainer extends Component {
	render() {
		return(
				<div>
					<Row gutter={16} style={{ marginTop: '5%' }}>
						<Col span={8} >
							<Card title="ADDRESS" style={{ textAlign: 'center', width: '80%' }}
								style={{ backgroundColor: 'white', height:200 }}
								bordered={false}>
								<div className="text">
									<address><Icon type="bank" /> 315, Lowell Ave,<br/>Trenton (Hamilton),<br/>NJ - 08619</address>
								</div>
							</Card>
						</Col>
						<Col span={8} >
							<Card title="PHONE NUMBER" style={{ textAlign: 'center' }}
								style={{ backgroundColor: 'white', height:200   }}
								bordered={false}>
								<div className="text">
									<Icon type="phone" /><a href="tel:+16092077662">(+1) 609-207-7662</a><br/>
									<Icon type="phone" /><a href="tel:+16092077823">(+1) 609-207-7823</a>
								</div>
							</Card>
						</Col>
						<Col span={8} >
							<Card title="EMAIL" style={{ textAlign: 'center' }}
								style={{ backgroundColor: 'white', height:200   }}
								bordered={false}>
								<div className="text">
									<Row>
										<Icon type="mail" /><a href="mailto:support@vexamine.com" target="_top"> support@vexamine.com</a>
										<br/>
										<Icon type="mail" /><a href="mailto:sales@vexamine.com" target="_top"> sales@vexamine.com</a>	
								</Row>
								</div>
							</Card>
						</Col>
					</Row>
					<Row>
						<h1 style={{ textAlign: 'center', marginTop: 50, fontSize: 35 }}>Contact Form</h1>
					</Row>
  			  <div style={{ width:'50%', float: 'left' }}>
  					<ContactForm />
					</div>
					<div style={{ width:'50%', float: 'right' }}>
					</div>
				</div>
		);
	}
}

function mapStateToProps(state) {
	return{
	}; 
}

export default withRouter(connect(mapStateToProps)(ContactUsContainer));