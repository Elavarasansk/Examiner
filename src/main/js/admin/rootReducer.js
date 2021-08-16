import { combineReducers } from 'redux-immutable';
import { routerReducer } from 'react-router-redux';
import adminReducer from './reduxFlow/reducer';
import layoutReducer from '../common/reduxFlow/layoutReducer';

export default combineReducers({
  routing: routerReducer,
  admin: adminReducer,
  layout: layoutReducer
});
