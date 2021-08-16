import { getRequest, postWithTextResponse } from '../rest/restApi';
import * as ACTION_TYPES from './actionTypes';
import * as END_POINTS from '../constants/endPoints';

export const logoutUser = async (dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_USER_LOGOUT });
  const data = await getRequest(`${END_POINTS.VEXAMINE_LOGOUT}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_USER_AUTHORITY, data });
}

export const authenticateUser = async (dispatch) => {
  dispatch({ type: ACTION_TYPES.REQUEST_USER_AUTHORITY });
  const data = await getRequest(`${END_POINTS.VEXAMINE_USER_AUTH}`);
  dispatch({ type: ACTION_TYPES.RECEIVE_USER_AUTHORITY, data });
}
