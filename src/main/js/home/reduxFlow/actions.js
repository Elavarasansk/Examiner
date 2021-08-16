import { postRequest, postWithTextResponse } from '../../common/rest/restApi';
import * as ACTION_TYPES from './actionTypes';
import * as END_POINTS from '../../common/constants/endPoints';

export const validateUserLogin = async (param, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_USER_LOGIN });
  const data = await postWithTextResponse(`${END_POINTS.VEXAMINE_LOGIN}`, param);
  return data;
}

export const registerUser = async (param, dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_USER_REGISTRATION });
  const data = await postRequest(`${SERVER_PATH}user/register`, param);
  dispatch({ type: ACTION_TYPES.RECEIVE_USER_REGISTRATION, data });
}

export const sendContactFormDetails = async (dispatch, param) => {
	const data = await postRequest(`/home/contactform`, param);
}

export const getCategoryForHome = async (dispatch) => {
	const data = await postWithTextResponse(`/home/categorydata`);
	return data;
}