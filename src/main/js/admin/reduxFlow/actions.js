import { getRequest, postRequest, postMultiPart, postWithTextResponse, postWithMultipartResponse } from '../../common/rest/restApi';
import * as ACTION_TYPES from './actionTypes';
import * as END_POINTS from '../../common/constants/endPoints';
import * as ADMIN_END_POINTS from '../../common/constants/adminEndPoints';

export const saveCategoryDetails = async (param , dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_SAVE_CATEGORY });
  const data = await postRequest(`${END_POINTS.VEXAMINE_CATEGORY_DETAILS}/add`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_SAVE_CATEGORY, data });
}

export const getCategoryDetails = async (dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_CATEGORY_INFO });
  const data = await getRequest(`${END_POINTS.VEXAMINE_CATEGORY_DETAILS}/find/all`);
  dispatch({ type: ACTION_TYPES.RECEIVE_CATEGORY_INFO, data });
}

export const getAllByCategory = async (param, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_ALL_BY_CATEGORY });
  const data = await postRequest(`${END_POINTS.VEXAMINE_CATEGORY_DETAILS}/find/category`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_ALL_BY_CATEGORY, data });
  return data;
}

export const registerManager = async (param , dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_REGISTER_MANAGER });
  const data = await postRequest(`${END_POINTS.VEXAMINE_USER_REGISTRATION}/add/manager`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_REGISTER_MANAGER, data });
  return param;
}

export const getQuestionBankDetails = async (dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_BANK_INFO });
  const data = await getRequest(`${END_POINTS.VEXAMINE_QUESTION_BANK}/find/all/question`);
  dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_INFO, data });
}

export const getCategoryGroups = async(dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_CATEGORY_GROUPING });
  const data = await getRequest(`${ADMIN_END_POINTS.VEXAMINE_TEST_CATEGORY_GROUP}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_CATEGORY_GROUPING, data });
}

export const getQuestionBankGroups = async(dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_BANK_GROUPING });
  const data = await getRequest(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_BANK_GROUP}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_GROUPING, data });
}

export const getQuestionAnswers = async(dispatch,param, page, size) => {
  dispatch({ type: ACTION_TYPES.REQUEST_QUESTION_ANSWER_LIST });
  const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_ANSWER_LIST}?page=${page}&size=${size}`,param);
  dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_ANSWER_LIST, data });
}

export const getManagersDetails = async (dispatch , param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_ALL_MANAGER_INFO });
  const data = await postRequest(`${END_POINTS.VEXAMINE_MANAGER_DETAILS}/find/all/summary` , param);
  dispatch({ type: ACTION_TYPES.RECEIVE_ALL_MANAGER_INFO, data });
}

export const getSingleManagerDetail = async (param , dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MANAGER_INFO });
  const data = await postRequest(`${END_POINTS.VEXAMINE_MANAGER_DETAILS}/find/mail/${param}`,param);
  dispatch({ type: ACTION_TYPES.RECEIVE_MANAGER_INFO, data });
}

export const getFilteredCategory = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_FILTERED_LIST });
  const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_TEST_CATEGORY_LIST}`,param);
  dispatch({ type: ACTION_TYPES.RECEIVE_FILTERED_LIST, data });
}

export const addCategoryBatch = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_CATEGORY_GROUPING });
  const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_TEST_CATEGORY_ADD_BATCH}`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_CATEGORY_GROUPING, data });
}

export const checkAndThrowCategoryExists = async(dispatch, category) => {
  await getRequest(`${ADMIN_END_POINTS.VEXAMINE_TEST_CATEGORY_CHECK_EXISTS}/{category}`);
}

export const getFilteredQuestionBankList = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_FILTERED_QB_LIST });
  const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_BANK_FILTER}`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_FILTERED_QB_LIST, data });
}

export const getQuestionBankTemplate = async(dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_QB_TEMPLATE });
  const qbTemplateFile = await postWithMultipartResponse(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_BANK_TEMPLATE}`);
  return qbTemplateFile;
}

export const uploadQuestionBank = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_QB_TEMPLATE });
  const data = await postMultiPart(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_BANK_UPLOAD}`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_QUESTION_BANK_GROUPING, data });
}

export const doQuestionAnswerEdit = async(param) => {
	const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_ANSWER_EDIT}`, param);
	return data;
}

export const getFilteredEmailId = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_FILTERED_EMAILID });
  const data = await postRequest(`${END_POINTS.VEXAMINE_MANAGER_DETAILS}/find/all/summary` , param);
  dispatch({ type: ACTION_TYPES.RECEIVE_FILTERED_EMAILID, data });
}

export const updateQbModelView = async(dispatch, data) => {
  dispatch({ type: ACTION_TYPES.RECEIVE_QB_MODEL_VIEW, data });
}

export const handleQbSelectedList = async(dispatch, data) => {
  dispatch({ type: ACTION_TYPES.RECEIVE_QB_SELECTED_LIST, data });
}

export const updateManagerDetails = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_UPDATE_MANAGER_INFO });
  const data = await postRequest(`${END_POINTS.VEXAMINE_MANAGER_DETAILS}/add/all` , param);
  dispatch({ type: ACTION_TYPES.RECEIVE_UPDATE_MANAGER_INFO, data });
}

export const updateCreditDetails = async(dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_UPDATE_CREDIT_INFO });
  const data = await postRequest(`${END_POINTS.VEXAMINE_CREDIT_DETAILS}/update/purchase/credit` , param);
  dispatch({ type: ACTION_TYPES.RECEIVE_UPDATE_CREDIT_INFO, data });
}

export const doAddQuestionAnswerSingle = async(param) => {
	const data = await postRequest(`${ADMIN_END_POINTS.VEXAMINE_QUESTION_ANSWER_ADD_SINGLE}`, param);
	return data;
}

export const updateManagerPassword = async (param,dispatch) => {
	  dispatch({ type: ACTION_TYPES.REQUEST_UPDATE_MANAGER_PASSWORD });
	  const data = await postRequest(`${END_POINTS.VEXAMINE_USER_REGISTRATION}/user/reset/password`,param);
	  dispatch({ type: ACTION_TYPES.RECEIVE_UPDATE_MANAGER_PASSWORD, data });
	  return data;
	};