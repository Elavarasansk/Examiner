import { combineReducers } from 'redux-immutable';
import { routerReducer } from 'react-router-redux';
import layoutReducer from '../common/reduxFlow/layoutReducer';

export default combineReducers({
  routing: routerReducer,
  layout: layoutReducer,
});
