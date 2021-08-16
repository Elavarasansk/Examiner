import { combineReducers } from 'redux-immutable';
import { routerReducer } from 'react-router-redux';
import managerReducer from './reduxFlow/reducer';
import layoutReducer from '../common/reduxFlow/layoutReducer';

export default combineReducers({
  routing: routerReducer,
  manager: managerReducer,
  layout: layoutReducer,
});
