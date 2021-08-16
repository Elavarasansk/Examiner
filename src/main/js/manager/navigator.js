import React, { Component } from 'react';
import { Router, Route, hashHistory, IndexRedirect } from 'react-router';
import App from '../common/app';
import TestAssignment from './containers/testAssignment';
import TestResult from './containers/testResult';

const mainPath = '/';

class Navigator extends Component {
  render() {
    return (
      <Router history={hashHistory}>
        <Route path="/" component={App}>
          <IndexRedirect to={`${mainPath}test/assignment`} />
          <Route path={`${mainPath}test/assignment`} component={TestAssignment} />
          <Route path={`${mainPath}test/summary`} component={TestResult} />
        </Route>
      </Router>
    );
  }
}

export default Navigator;
