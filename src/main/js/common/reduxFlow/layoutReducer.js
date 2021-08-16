import Immutable, { Map as immutableMap, List as immutableList } from 'immutable';
import { combineReducers } from 'redux-immutable';
import * as ActionTypes from './actionTypes';

function getSideMenuToggle(state = true, action) {
  switch (action.type) {
  case ActionTypes.RECEIVE_SIDE_MENU_TOGGLE:
    return action.data;
  default:
    return state;
  }
}

function authorityInfo(state = immutableMap(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_USER_AUTHORITY:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

export default combineReducers({
  getSideMenuToggle,
  authorityInfo
});
