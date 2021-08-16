import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Menu, Layout, Icon } from 'antd';
import '../styles/layout.css';
import { sideMenu } from '../constants/menuConstants';

const { SubMenu } = Menu;
const { Sider } = Layout;

class SideMenu extends Component {
  
  state ={
      defaultSelectMenu: ['home'],
  }
  
  handleMenuClick = (menu) => {
    const { key } = menu;
    const { router } = this.props;
    const pageType =window.location.pathname.replace(".html","").replace("/","");
    
    if(pageType.indexOf('home')!==-1){
      router.push(`/${key}`);
    } else {
      window.location=`home.html#/${key}`;
    }
  }
  
  render() {
    const { defaultSelectMenu } = this.state;
    const { collapsed, location } = this.props;
    const urlPath = location.pathname.replace("/","");
    
    return(
        <Sider trigger={null} collapsible collapsed={collapsed}>
          <div className="logo"/>
          <Menu theme="dark" mode="inline" defaultSelectedKeys={[urlPath]} onClick={this.handleMenuClick}>
            {sideMenu.map(data => <Menu.Item key={data.path}><Icon type={data.icon} style={{ fontSize: 'large' }} /><span>{data.name}</span></Menu.Item>)}
          </Menu>
        </Sider>
    );
  }
}

SideMenu.contextTypes = {
    role: PropTypes.string,
    loggedInUser: PropTypes.string
};

function mapStateToProps(state) {
  return {
    collapsed: state.get('layout').get('getSideMenuToggle'),
  };
}

export default withRouter(connect(mapStateToProps)(SideMenu));