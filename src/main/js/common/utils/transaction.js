import { notification } from 'antd';

export const restNotification = (type,response) => {
  const { message, description } = response;
  notification[type]({
    message,
    description,
    duration: 6
  });
}

export const transactionError = error => error.response.json().then((response) => {
  notification['error']({
    message: response.error,
    description: response.message,
    duration: 6
  });
});