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

function getAllCategories(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_ALL_TEST_CATEGORY:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function saveCategoryDetails(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_SAVE_CATEGORY:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function getCategoryDetails(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_CATEGORY_INFO:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function registerManager(state = immutableMap(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_REGISTER_MANAGER:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function getQuestionBankDetails(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_QUESTION_BANK_INFO:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function categoryGrouping(state = immutableMap(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_CATEGORY_GROUPING:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function quesBankGrouping(state = immutableMap(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_QUESTION_BANK_GROUPING:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function categoryList(state = immutableMap(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_FILTERED_LIST:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function questionAnswer(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_QUESTION_ANSWER_LIST:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function getManagersDetails(state = immutableList(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_ALL_MANAGER_INFO:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function getSingleManagerDetail(state = immutableMap(), action) {
  switch (action.type) {
    case ActionTypes.RECEIVE_MANAGER_INFO:
      return Immutable.fromJS(action.data);
    default:
      return state;
  }
}

function filteredQuestionBankList(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_FILTERED_QB_LIST:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function getFilteredEmailId(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_FILTERED_EMAILID:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function updateQbModelView(state = false , action){
  const { type , data} = action;
  switch(type){
    case ActionTypes.RECEIVE_QB_MODEL_VIEW:
      return data;
    default:
      return state;
  }
}

function handleQbSelectedList(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_QB_SELECTED_LIST:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function updateManagerDetails(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_UPDATE_MANAGER_INFO:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function updateCreditDetails(state = immutableList(), action){
  const { type, data } = action;
  switch(type){
    case ActionTypes.RECEIVE_UPDATE_CREDIT_INFO:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function updateManagerPassword(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_UPDATE_MANAGER_PASSWORD:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
	}

export default combineReducers({
  getSideMenuToggle,
  getAllCategories,
  saveCategoryDetails,
  getCategoryDetails,
  getQuestionBankDetails,
  categoryGrouping,
  quesBankGrouping,
  questionAnswer,
  categoryList,
  filteredQuestionBankList,
  getManagersDetails,
  getSingleManagerDetail,
  getFilteredEmailId,
  updateQbModelView,
  handleQbSelectedList,
  updateManagerDetails,
  updateCreditDetails,
  updateManagerPassword
});
