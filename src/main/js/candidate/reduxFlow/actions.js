import { getRequest, postRequest } from '../../common/rest/restApi';
import * as ACTION_TYPES from './actionTypes';
import * as END_POINTS from '../../common/constants/candidateEndPoints';

export const getAssignedTest = async (dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MY_ASSIGNED_TEST });
  const data = await getRequest(`${END_POINTS.VEXAMINE_ASSIGNED_TESTS_MINE}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_MY_ASSIGNED_TEST, data });
}

export const startTest = async (id, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MY_ASSIGNED_TEST });
  const data = await getRequest(`${END_POINTS.VEXAMINE_START_ASSIGNED_TESTS}/${id}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_MY_ASSIGNED_TEST, data });
}

export const getMyQuestions = async (assignId, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MY_ASSIGNED_QUESTIONS });
  const data = await getRequest(`${END_POINTS.VEXAMINE_ASSIGNED_QUESTIONS}/${assignId}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_MY_ASSIGNED_QUESTIONS, data });
}

export const answerMyQuestion = async (resultId, answer, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_ANSWER_SUBMIT });
  await getRequest(`${END_POINTS.VEXAMINE_MY_ANSWER}/${resultId}/${answer}`);
}

export const submitTestAnswers = async (id, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MY_ASSIGNED_TEST });
  const data = await getRequest(`${END_POINTS.VEXAMINE_COMPLETE_ASSIGNED_TEST}/${id}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_MY_ASSIGNED_TEST, data });
}

export const expireMyTest = async (id, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_MY_ASSIGNED_TEST });
  const data = await getRequest(`${END_POINTS.VEXAMINE_EXPIRE_ASSIGNED_TEST}/${id}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_MY_ASSIGNED_TEST, data });
}

export const getAllMyAssignedTest = async (dispatch, param) => {
  dispatch({ type: ACTION_TYPES.REQUEST_ALL_MY_ASSIGNED_TEST });
  const data = await postRequest(`${END_POINTS.VEXAMINE_ALL_ASSIGNED_TESTS_MINE}`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_ALL_MY_ASSIGNED_TEST, data });
}

