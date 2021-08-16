import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Form, Icon, Input, Button, Checkbox, Modal, message as AntMessage, Spin } from 'antd';
import { validateUserLogin } from '../reduxFlow/actions.js';
import '../../common/styles/dashboard.css';

class UserLoginForm extends Component {
  
  state = {
	loading: false
  }
	
  handleSubmit = async (e) => {
    e.preventDefault();
    let isValid = false;
    const { form } = this.props;
    form.validateFields((err) =>  isValid = !err);
    if(!isValid){
      return;
    } 
    await this.loginUser().catch(this.handleError);
  }

  loginUser =async (e) => {
    this.setState({ loading: true });
    const { form, dispatch } = this.props;
    const param = form.getFieldsValue();
    const redirectionHtml = await validateUserLogin(param, dispatch);
    window.location = redirectionHtml;
    AntMessage.success(`Login Success`);
  }
  
  handleError = (err) => {
    AntMessage.error(`Invalid Credentials. Please Check username & password`);
    this.setState({ loading: false });
  }

  render() {
    const { open } = this.props;
    const { getFieldDecorator } = this.props.form;
    const { loading } = this.state;
    
    return (
      <div>
      <Modal
        title="Login Vexamine"
        visible={open}
        footer={null}
        maskClosable={false}
        width={350}
        onCancel={() => this.props.closeLogin()}
      >
      <Spin spinning={loading} >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <Form.Item>
            {getFieldDecorator('mailId', {
              rules: [{ required: true, message: 'Please input your mail-id!' }],
            })(
              <Input
                prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                placeholder="Mail Id"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('password', {
              rules: [{ required: true, message: 'Please input your Password!' }],
            })(
              <Input
                prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                type="password"
                placeholder="Password"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('remember', {
              valuePropName: 'checked',
              initialValue: true,
            })(<Checkbox >Remember me</Checkbox>)}
            {false && <a className="login-form-forgot" href="">
              Forgot password
            </a>}
            <Button type="primary" htmlType="submit" className="login-form-button">
              Log in
            </Button>
            {false && <a href="">register now!</a>}
          </Form.Item>
        </Form>
        </Spin>
      </Modal>
      </div>
    );
  }
}

const WrappedNormalLoginForm = Form.create({ name: 'normal_login' })(UserLoginForm);

export default withRouter(connect(null)(WrappedNormalLoginForm));