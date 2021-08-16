import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Menu, Layout, Button, Icon, Row, Col, message as AntMessage, Tooltip, Tag } from 'antd';
import { logoutUser } from '../reduxFlow/actions';
import * as ActionTypes from '../reduxFlow/actionTypes';
import UserLoginForm from '../../home/components/userLogin';
import { ROLE } from '../constants/userRoles';
import { adminMenu, managerMenu, candidateMenu } from '../constants/menuConstants';

const { SubMenu } = Menu;
const { Header } = Layout;
const redirectHome = "/home.html";
const blogsArr = [{
  icon:'facebook',
  path:''
}, {
  icon:'twitter',
  path:''
}, {
  icon:'linkedin',
  path:''
}];


class MainHeader extends Component {
  
  state = {
      collapsed: true,
      openLogin:false
  };
  
  toggle = () => {
    const { dispatch } = this.props;
    const { collapsed } = this.state;

    this.setState({ collapsed: !collapsed });
    dispatch({
      type: ActionTypes.RECEIVE_SIDE_MENU_TOGGLE,
      data: !collapsed
    });
  };
  
  handleUserLogin = (openLogin) => {
    this.setState({ openLogin })
  }
  
  handleUserlogout = async () => {
    const { dispatch } = this.props;
    await logoutUser(dispatch);
    window.location = redirectHome;
    AntMessage.success(`Logout Success`);
  }
  
  loadUserBasedMenu = () => {
    const { role, loggedInUser } = this.context;
    switch (role){
      case ROLE.SUPER_ADMIN:
        return adminMenu;
      case ROLE.MANAGER:
        return managerMenu;
      case ROLE.CANDIDATE:
        return candidateMenu;
    }
    return [];
  }
  
  handleMenuClick = (key) => {
    const { router } = this.props;
    const { role } = this.context;
    const pageType =window.location.pathname.replace(".html","").replace("/","");
    
    if(role.toLowerCase().indexOf(pageType) !== -1){
      router.push(`/${key}`);
    } else {
      switch (role){
        case ROLE.SUPER_ADMIN:
          window.location= `/admin.html#${key}`;
          break;

        case ROLE.MANAGER:
          window.location= `/manager.html#${key}`;
          break;

        case ROLE.CANDIDATE:
          window.location= `/candidate.html#${key}`;
          break;
      }
    }
  }
  
  render() {    
    const { router,location,authorityInfo } = this.props;
    const { openLogin } = this.state;
    
    const { role, loggedInUser } = this.context;
    const headerMenu = this.loadUserBasedMenu();
    const urlPath = location.pathname.replace("/","");
    
    let roleInfo = authorityInfo !== undefined ? authorityInfo.toJS() : {};
    
    if(roleInfo.role === 'SUPER_ADMIN')
    	roleInfo.role = 'ADMINISTRATOR';
    
    return(
        <Header style={{ background: 'auto', padding: 0 }} className="container">
        <div style={{ width: '100%',display:'block' }}>
            <div id="header-left-room" style={{ float : 'left' }} >
              <Button  onClick={this.toggle} style={{ margin: 15 }} ><Icon type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}/></Button>
              {headerMenu.map(data => <Button onClick={()=> this.handleMenuClick(data.path)} key={data.name} icon={data.icon}
              ghost type={ data.path == urlPath ? "danger" : "dashed" } style={{ marginRight:10 }} >{data.name}</Button>)}
            </div>
            <div id="header-right-room" style={{ float : 'right',marginRight : '24px'}}>
              {roleInfo.role !== undefined && roleInfo.role !== null && <span><span style={{ color: '#fff' }}>Logged in as:  </span><Tag color="red" >{roleInfo.role}</Tag></span>}
              {blogsArr.map(data => <Button onClick={() => window.open(data.path)} type="dashed" shape="circle" icon={data.icon} size={'large'} style={{ marginRight: 10 }} />)}
              {!loggedInUser && <Button type="primary" icon="login" onClick={() => this.handleUserLogin(true)}>
              Login
              </Button>}
              {loggedInUser && <Tooltip title= {"Logout"}><Button type="danger" icon="poweroff" onClick={this.handleUserlogout}>{loggedInUser}</Button></Tooltip>}
              {openLogin && !loggedInUser && <UserLoginForm open={openLogin} closeLogin={() => this.handleUserLogin(false)}/>}
              </div>
          </div>
        </Header>
    );
  }
}

MainHeader.contextTypes = {
    role: PropTypes.string,
    loggedInUser: PropTypes.string
};

function mapStateToProps(state) {
	  return {
	    authorityInfo: state.get('layout').get('authorityInfo')
	  };
}

export default withRouter(connect(mapStateToProps)(MainHeader));
