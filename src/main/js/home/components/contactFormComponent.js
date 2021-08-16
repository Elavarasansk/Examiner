import React, { Component } from 'react';
import { Row, Col, Button, Input, Form } from 'antd';
import { sendContactFormDetails } from '../reduxFlow/actions';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

const { TextArea } = Input;

class ContactFormComponent extends Component {
	
	handleSubmit = async (e) => {
		e.preventDefault();
		let isValid = false;
		const { form } = this.props;
		form.validateFields((err) =>  isValid = !err);
		if(!isValid){
			return;
		} 
		await this.handleContactDetailsSend().catch(this.handleError);
	}

	handleContactDetailsSend = async() => {
		const data = this.props.form.getFieldsValue();
		const { dispatch } = this.props;
		await sendContactFormDetails(dispatch, data);
	}

	handleError = (err) => {
		this.setState({ loading: false });
	}
	
	render() {
		
		const { getFieldDecorator } = this.props.form;
		
		return(
				<div>
					<Row>
						<Form>
						<Col span={22} >
							<Row gutter={12} >
								<Col span={12} >
								<Form.Item>
									{getFieldDecorator('name', {
										rules: [{
											required: true,
											message: 'Please enter your name'
										}]
									})(<Input size='large' placeholder='Your Name' />)}
								</Form.Item>
								</Col>
								<Col span={12} >
								<Form.Item>
									{getFieldDecorator('email', {
										rules:[{
											required: true,
											message: 'Please enter you email'
										},{
							                type: 'email',
							                message: 'The input is not valid E-mail!',
							            }]
									})(<Input type="email" size='large' placeholder='Your Email' />)}
								</Form.Item>
								</Col>
							</Row>
							<Form.Item>
							{getFieldDecorator('subject', {
								rules:[{
									required: true,
									message: 'Please enter a subject'
								}]
							})(<Input size='large' placeholder='Subject'/>)}
							</Form.Item>
							<Form.Item>
							{getFieldDecorator('message', {
								rules:[{
									required: true,
									message: 'Please enter a message'
								}]
							})(<TextArea rows={8} placeholder='Your Message' />)}
							</Form.Item>
							<Row>
								<Button type='primary' style={{ width: '100%' }} onClick={this.handleSubmit} >Send your message</Button>
							</Row>
						</Col>
						</Form>
					</Row>
				</div>
		);
	}
}

function mapStateToProps(state) {
	return{};
}

const ContactForm = Form.create()(ContactFormComponent);
export default withRouter(connect(mapStateToProps)(ContactForm));