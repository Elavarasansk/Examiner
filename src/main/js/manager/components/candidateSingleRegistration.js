import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import {  Icon, Button, Form, Input, Modal,message as AntMessage , Switch } from 'antd';
import { addCandidate } from '../reduxFlow/actions.js';

const confirm = Modal.confirm;

let confirmUpdate = {};

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

class CandidateSingleRegistration extends Component {
  constructor(props) {
    super(props);
    this.state = {
        switchType : false
    };
  }

  componentDidMount() {
	  this.props.form.resetFields();
	  }

	onClose = () => {
	  this.props.hideCandidate();
	};
	
	reload = () => {
		const { reload } =  this.props;
		reload();
		};


	compareToFirstPassword = (rule, value, callback) => {
	  const form = this.props.form;
	  if (value && value !== form.getFieldValue('password')) {
	    callback('Two passwords that you enter is inconsistent!');
	  } else {
	    callback();
	  }
	};

	validateToNextPassword = (rule, value, callback) => {
	  const form = this.props.form;
	  if (value && this.state.confirmDirty) {
	    form.validateFields(['confirm'], { force: true });
	  }
	  callback();
	};

	handleSingleRegisterSubmit = async (e) => {
	  e.preventDefault();
	  let isValid = false;
	  const { form } = this.props;
	  form.validateFields(err => isValid = !err);
	  if (!isValid) {
	    return;
	  }
	  this.showConfirm();
	}

	showConfirm = async () => {
		confirmUpdate = confirm({
	    width: 480,
	    title: 'Are you sure do you want to create a candidate?',
	    centered: true,
	    onOk: async () => { await this.onConfirmClick(); }
	    
	    });
	}

	onConfirmClick = async () => {
	  await this.registerSingleCandidate().catch(this.handleError);
	};

	handleError = (err) => {
	  AntMessage.error(`${err.message}`);
	}

	handleReset = () => {
	  this.props.form.resetFields();
	};

	registerSingleCandidate = async () => {
	  const { form, dispatch } = this.props;
	  const { switchType } = this.state;
	  const param = form.getFieldsValue();
	  param.switchType = switchType;
	  confirmUpdate.update({ cancelButtonProps : {disabled : true } });
	  await addCandidate(param, dispatch);
	  confirmUpdate.update({ cancelButtonProps : {disabled : false } });
	  this.reload();
	  AntMessage.success('Candidate created successfully.');
	};
	
  onChange = (event) => {
    this.setState({ switchType: event });
    const { form } = this.props;
    let formValues = form.getFieldsValue();
}

	render() {
	  const { getFieldDecorator } = this.props.form;
	  const { switchType } = this.state;
	  return (
  <div>
  <b>Auto-Generate Password:</b> <Switch checkedChildren="Yes" unCheckedChildren="No" defaultunChecked  onChange={this.onChange}/>
 <Form style={{ marginTop: 20 }} layout="vertical" onSubmit={this.handleSingleRegisterSubmit} >
        <Form.Item label="E-mail">
          {getFieldDecorator('mailId', {
					rules: [
						{
							type: 'email',
							message: 'The input is not valid E-mail!',
						},
						{
							required: true,
							message: 'Please input your E-mail!',
						},
						],
				})(<Input />)}
        </Form.Item>
        { !switchType &&  <Form.Item label="Password" hasFeedback>
          {getFieldDecorator('password', {
					rules: [
						{
							required: true,
							message: 'Please input your password!',
						},
						{
							validator: this.validateToNextPassword,
						},
						],
				})(<Input.Password  />)}
        </Form.Item> }
        { !switchType && <Form.Item label="Confirm Password" hasFeedback>
          {getFieldDecorator('confirm', {
					rules: [
						{
							required: true,
							message: 'Please confirm your password!',
						},
						{
							validator: this.compareToFirstPassword,
						},
						],
				})(<Input.Password  />)}
        </Form.Item>}
        <Form.Item {...tailFormItemLayout}>
          <Button style={{ marginRight: 8 }} onClick={this.handleReset}>Clear</Button>
          <Button type="primary" htmlType="submit">
				Register
          </Button>
        </Form.Item>
      </Form> 
  </div>
	  );
	}
}

const WrappedCandidateSingleRegistration = Form.create()(CandidateSingleRegistration);
export default withRouter(connect(null)(WrappedCandidateSingleRegistration));
