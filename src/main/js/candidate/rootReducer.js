import { combineReducers } from 'redux-immutable';
import { routerReducer } from 'react-router-redux';
import candidateReducer from './reduxFlow/reducer';
import layoutReducer from '../common/reduxFlow/layoutReducer';

export default combineReducers({
  routing: routerReducer,
  candidate: candidateReducer,
  layout: layoutReducer
});
