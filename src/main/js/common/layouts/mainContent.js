import React, { Component } from 'react';
import { Menu, Layout, Breadcrumb, Card } from 'antd';
import { withRouter } from 'react-router';

const { SubMenu } = Menu;
const { Content } = Layout;
const { Meta } = Card;

class MainContent extends Component {
  render() {
    const { children } = this.props;
    
    return(
      <Content
          style={{
            margin: '16px 6px 24px',
            padding: '0 20px',
            background: 'transparent'
          }}
        >
       {true && children}
      </Content>
    );
  }
}
export default withRouter(MainContent);