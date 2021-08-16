import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { authenticateUser } from '../reduxFlow/actions';
import { Spin } from 'antd';

class Authentication extends Component {

  constructor(props) {
    super(props);
    this.state = {
        roles: null,
        roleInfo: null,
        processing: false,
        loggedInUser: null
    };
  }

  getChildContext() {
    const { authorityInfo } = this.props;
    if(authorityInfo){
      return {
        loggedInUser: authorityInfo.get('username'),
        role: authorityInfo.get('roleInfo'),
        role: authorityInfo.get('role')
      };
    }
  }

  componentWillMount() {
    const { dispatch } = this.props;
    authenticateUser(dispatch);
  }

  componentWillReceiveProps(nextProps) {
    const { authorityInfo } = nextProps;
    if(nextProps !== this.props) {
      this.setState({
        loggedInUser: authorityInfo.get('username'),
        roleInfo: authorityInfo.get('roleInfo'),
        role: authorityInfo.get('role')
      });
    }
  }

  render() {
    const { loggedInUser, processing } = this.state;
    const { children } = this.props;
    if (loggedInUser) {
      return children;
    } else {
    	return (<span>{children}</span>);
    }
  }
}

Authentication.childContextTypes = {
    role: PropTypes.string,
    router: PropTypes.object,
    roleInfo: PropTypes.object,
    loggedInUser: PropTypes.string
};

function mapStateToProps(state) {
  return {
    authorityInfo: state.get('layout').get('authorityInfo')
  };
}

export default withRouter(connect(mapStateToProps)(Authentication));