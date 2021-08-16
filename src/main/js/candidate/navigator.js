import React, { Component } from 'react';
import { Router, Route, hashHistory, IndexRedirect } from 'react-router'
import App from '../common/app';
import CandidateMultiContainer from './container/candidateMultiContainer';

const mainPath = "/";

class Navigator extends Component {
  render() {    
    return(
        <Router history={hashHistory}>
          <Route path="/" component={App}>
             <IndexRedirect to={mainPath+"dashboard"} />
             <Route path={mainPath+"dashboard"} component={CandidateMultiContainer}/>   
          </Route>
        </Router>
    );
  }
}

export default Navigator;