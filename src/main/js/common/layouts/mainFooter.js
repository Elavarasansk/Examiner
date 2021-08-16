import React, { Component } from 'react';
import { Layout } from 'antd';
import { withRouter } from 'react-router';

const {  Footer } = Layout;


class MainFooter extends Component {
  render() {    
    return(
      <Footer style={{ textAlign: 'center' }}>
        <div className="copyright">Copyright © 2018 <a href="#">VEXAMINE</a>. All Rights Reserved</div>
      </Footer>
    );
  }
}

export default withRouter(MainFooter);