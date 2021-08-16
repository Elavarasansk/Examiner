import { postRequest, postWithTextResponse, getRequest, getWithMultipartResponse, postMultiPart } from '../../common/rest/restApi';
import * as ACTION_TYPES from './actionTypes';
import * as END_POINTS from '../../common/constants/managerEndPoints';

export const validateUserLogin = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_USER_LOGIN });
	const data = await postRequest(`${END_POINTS.VEXAMINE_LOGIN}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_USER_LOGIN, data });
};

export const registerUser = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_USER_REGISTRATION });
	const data = await postRequest(`${SERVER_PATH}user/register`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_USER_REGISTRATION, data });
};

export function logoutUser(request) {
	const url = `${SERVER_CONTEXT_PATH}/logout/`;
}

export const authenticateUser = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_FORGOT_PASSWORD });
	const data = await postWithTextResponse(`${SERVER_PATH}user/forgot-password`, param);
	return data;
};

export const registerTest = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_TEST_REGISTRATION });
	const data = await postRequest(`${END_POINTS.REGEISTER_TEST}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_TEST_REGISTRATION, data });
};

export const searchCandidate = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_SEARCH_CANDIDATE_DATA });
	const data = await getRequest(`${END_POINTS.SEARCH_CANDIDATE}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_SEARCH_CANDIDATE_DATA, data });
};

export const getAllCandidate = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_ALL_CANDIDATE });
	const data = await getRequest(`${END_POINTS.GET_ALL_CANDIDATE}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_ALL_CANDIDATE, data });
	return data;
};

export const searchQuestionBank = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_SEARCH_QUESTION_BANK });
	const data = await postRequest(`${END_POINTS.SEARCH_QUESTION_BANK}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_SEARCH_QUESTION_BANK, data });
};

export const getQuestionBankCount = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_BANK_COUNT });
	const data = await getRequest(`${END_POINTS.GET_QUESTION_BANK_COUNT}${param}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_COUNT, data });
	return data;
};

export const searchSummaryDetails = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_SEARCH_SUMMARY_DETAILS });
	const data = await postRequest(`${END_POINTS.SEARCH_SUMMARY}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_SEARCH_SUMMARY_DETAILS, data });
	return data;
};

export const downloadReport = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_DOWNLOAD_REPORT });
	const data = await getWithMultipartResponse(`${END_POINTS.DOWNLOAD_REPORT}${param}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_DOWNLOAD_REPORT, data });
	return data;
};

export const getAllTestAssignment = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_GET_ALL_TEST_ASSIGNMENT });
	const data = await postRequest(`${END_POINTS.GET_ALL_TEST_ASSIGNMENT}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_GET_ALL_TEST_ASSIGNMENT, data });
	return data;
};

export const getCategoryGroups = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_CATEGORY_GROUPING });
	const data = await getRequest(`${END_POINTS.VEXAMINE_TEST_CATEGORY_GROUP}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_CATEGORY_GROUPING, data });
};

export const getQuestionBankGroups = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_BANK_GROUPING });
	const data = await getRequest(`${END_POINTS.VEXAMINE_QUESTION_BANK_GROUP}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_GROUPING, data });
};

export const getAllQuestionBank = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_ALL_QUESTION_BANK });
	const data = await getRequest(`${END_POINTS.GET_ALL_QUESTION_BANK}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_ALL_QUESTION_BANK, data });
};

export const getParseData = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_PARSE_FILE });
	const data = await postMultiPart(`${END_POINTS.PARSE_FILE}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_PARSE_FILE, data });
};

export const addAllCandidate = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_ADD_ALL_CANDIDATE });
	const data = await postRequest(`${END_POINTS.ADD_ALL_CANDIDATE}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_ADD_ALL_CANDIDATE, data });
};

export const addCandidate = async (param, dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_ADD_CANDIDATE });
	const data = await postRequest(`${END_POINTS.ADD_CANDIDATE}`, param);
	dispatch({ type: ACTION_TYPES.RECEIVE_ADD_CANDIDATE, data });
};

export const downloadTemplate = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_DOWNLOAD_TEMPLATE });
	const data = await getWithMultipartResponse(`${END_POINTS.DOWNLOAD_TEMPLATE}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_DOWNLOAD_TEMPLATE, data });
	return data;
};

export const rejectTest = async (param,dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_REJECT_TEST });
	const data = await postRequest(`${END_POINTS.REJECT_TEST}`,param);
	dispatch({ type: ACTION_TYPES.RECEIVE_REJECT_TEST, data });
};

export const getDeclarativeAnswer = async (param,dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_DECLARATIVE_ANSWER });
	const data = await getRequest(`${END_POINTS.DECLARATIVE_ANSWER}${param}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_DECLARATIVE_ANSWER, data });
	return data;
};

export const updateTestResult = async (param,dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_UPDATE_TEST });
	const data = await postRequest(`${END_POINTS.UPDATE_TEST_RESULT}`,param);
	dispatch({ type: ACTION_TYPES.RECEIVE_UPDATE_TEST, data });
};

export const getManagerCredits = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_MANAGER_CREDITS });
	const data = await getRequest(`${END_POINTS.GET_MANAGER_CREDITS}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_MANAGER_CREDITS, data });
};

export const updateManagerCredits = async (param,dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_MANAGER_CREDITS_UPDATE });
	const data = await postRequest(`${END_POINTS.UPDATE_MANAGER_CREDITS}`,param);
	dispatch({ type: ACTION_TYPES.RECEIVE_MANAGER_CREDITS_UPDATE, data });
	return data;
};

export const getQuestionBankCascader = async (dispatch) => {
	dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_BANK_CASCADE });
	const data = await getRequest(`${END_POINTS.QUESTION_BANK_CASCADE}`);
	dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_CASCADE, data });
};

export const updateCandidatePassword = async (param,dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_UPDATE_CANDIDATE_PASSWORD });
  const data = await postRequest(`${END_POINTS.VEXAMINE_USER_REGISTRATION}/user/reset/password`,param);
  dispatch({ type: ACTION_TYPES.RECEIVE_UPDATE_CANDIDATE_PASSWORD, data });
  return data;
};

export const sendReportByMail = async (param,dispatch) => {
	  dispatch({ type: ACTION_TYPES.REQUEST_SEND_REPORT_MAIL });
	  const data = await getRequest(`${END_POINTS.VEXAMINE_SEND_REPORT_MAIL}${param}`);
	  dispatch({ type: ACTION_TYPES.RECEIVE_SEND_REPORT_MAIL, data });
	  return data;
	};
