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

function searchCandidate(state = immutableList(), action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_SEARCH_CANDIDATE_DATA:
	    return Immutable.fromJS(action.data);
	  default:
	    return state;
	  }
}

function getAllCandidate(state = immutableMap(), action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_ALL_CANDIDATE:
	    return Immutable.fromJS(action.data);
	  default:
	    return state;
	  }
}

function searchQuestionBank(state = immutableList(), action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_SEARCH_QUESTION_BANK:
	    return Immutable.fromJS(action.data);
	  default:
	    return state;
	  }
}

function getQuestionBankCount(state = 1, action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_QUESTION_BANK_COUNT:
	    return action.data;
	  default:
	    return state;
	  }
}

function searchSummaryDetails(state = immutableMap(), action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_SEARCH_SUMMARY_DETAILS:
	    return Immutable.fromJS(action.data);
	  default:
	    return state;
	  }
}

function getAllTestAssignment(state = immutableMap(), action) {
	  switch (action.type) {
	  case ActionTypes.RECEIVE_GET_ALL_TEST_ASSIGNMENT:
	    return Immutable.fromJS(action.data);
	  default:
	    return state;
	  }
}

function categoryGrouping(state = immutableMap(), action) {
  const { type, data } = action;
  switch (type) {
    case ActionTypes.RECEIVE_CATEGORY_GROUPING:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

function quesBankGrouping(state = immutableMap(), action) {
  const { type, data } = action;
  switch (type) {
    case ActionTypes.RECEIVE_QUESTION_BANK_GROUPING:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}


function getAllQuestionBank(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_ALL_QUESTION_BANK:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}


function getParseData(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_PARSE_FILE:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}

function getDeclarativeAnswer(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_DECLARATIVE_ANSWER:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}

function updateTestResult(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_UPDATE_TEST:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}

function getManagerCredits(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_MANAGER_CREDITS:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}

function getQuestionBankCascader(state = immutableList(), action) {
	  const { type, data } = action;
	  switch (type) {
	    case ActionTypes.RECEIVE_QUESTION_BANK_CASCADE:
	      return Immutable.fromJS(data);
	    default:
	      return state;
	  }
}

function updateCandidatePassword(state = immutableList(), action) {
  const { type, data } = action;
  switch (type) {
    case ActionTypes.RECEIVE_UPDATE_CANDIDATE_PASSWORD:
      return Immutable.fromJS(data);
    default:
      return state;
  }
}

export default combineReducers({
  getSideMenuToggle,
  searchCandidate,
  getAllCandidate,
  searchQuestionBank,
  getQuestionBankCount,
  searchSummaryDetails,
  getAllTestAssignment,
  categoryGrouping,
  quesBankGrouping,
  getAllQuestionBank,
  getParseData,
  getDeclarativeAnswer,
  updateTestResult,
  getManagerCredits,
  getQuestionBankCascader,
  updateCandidatePassword
});
