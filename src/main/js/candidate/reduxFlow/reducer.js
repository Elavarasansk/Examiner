import Immutable, { Map as immutableMap, List as immutableList } from 'immutable';
import { combineReducers } from 'redux-immutable';
import * as ActionTypes from './actionTypes';

function myTests(state = immutableMap(), action) {
  switch (action.type) {
  case ActionTypes.RECEIVE_MY_ASSIGNED_TEST:
    return Immutable.fromJS(action.data);
  default:
    return state;
  }
}

function myQuestions(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_MY_ASSIGNED_QUESTIONS:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function allMyTests(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_ALL_MY_ASSIGNED_TEST:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

export default combineReducers({
  myTests,
  myQuestions,
  allMyTests
});
