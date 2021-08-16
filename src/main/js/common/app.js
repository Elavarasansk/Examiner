import React, { Component } from 'react';
import { Layout } from 'antd';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import MainHeader from './layouts/mainheader';
import MainContent from './layouts/mainContent';
import MainFooter from './layouts/mainFooter';
import SideMenu from './layouts/sideMenu';
import Authentication from './rest/authentication';

class App extends Component {
	
  render() {
    const { children } = this.props;
    
    return(<Authentication>
        <Layout>
          <SideMenu />            
        <Layout className={"body_content"} >
          <MainHeader/>            
          <MainContent>{this.props.children}</MainContent>
          {false && <MainFooter/>}
        </Layout>
      </Layout></Authentication>
    );
  }
}


App.contextTypes = {
  router: PropTypes.object.isRequired,
};

App.propTypes = {
  form: PropTypes.object,
};

function mapStateToProps(state) {
  return {
  };
}

export default withRouter(connect(mapStateToProps)(App));