import React, { Component } from 'react';
import { Router, Route, hashHistory, IndexRedirect } from 'react-router'
import App from '../common/app';
import AdminDetailsContainer from './containers/adminDetailsContainer';
import ManagerInformationContainer from './containers/managerInformationContainer';

const mainPath = "/";

class Navigator extends Component {
  render() {    
    return(
        <Router history={hashHistory}>
          <Route path="/" component={App}>
             <IndexRedirect to={mainPath+"questionnaire"} />
             <Route path={mainPath+"questionnaire"} component={AdminDetailsContainer}/>
             <Route path={mainPath+"configure/manager"} component={ManagerInformationContainer}/>
             </Route>
        </Router>
    );
  }
}

export default Navigator; 