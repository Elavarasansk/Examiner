import React, { Component } from 'react';
import { Router, Route, hashHistory, IndexRedirect } from 'react-router'
import App from '../common/app';
import HomeInfoContainer from './containers/homeInfoContainer';
import ServicesInfoContainer from './containers/servicesInfoContainer';
import AboutUsContainer from './containers/aboutUsContainer';
import ContactUsContainer from './containers/contactUsContainer';
import pricingContainer from './containers/pricingContainer';

const mainPath = "/";

class Navigator extends Component {
  render() {    
    return(
        <Router history={hashHistory}>
          <Route path="/" component={App}>
             <IndexRedirect to={mainPath+"dashboard"} />
             <Route path={mainPath+"dashboard"} component={HomeInfoContainer}/>
             <Route path={mainPath+"services"} component={ServicesInfoContainer}/>
             <Route path={mainPath+"about"} component={AboutUsContainer}/>
             <Route path={mainPath+"pricing"} component={pricingContainer}/>
             <Route path={mainPath+"contactus"} component={ContactUsContainer}/>
          </Route>
        </Router>
    );
  }
}

export default Navigator;