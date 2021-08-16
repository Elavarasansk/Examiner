const SERVER = window.location.origin;

const jsonHeader = {
  Accept: 'application/json',
  'Content-Type': 'application/json',
};

function buildFormData(request) {
  const formData = new FormData();
  for (const name in request) {
    formData.append(name, request[name]);
  }
  return formData;
}

export async function getRequest(url) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'GET',
    headers: jsonHeader,
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

export async function getWithTextResponse(url) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'GET',
    headers: jsonHeader,
    credentials: 'same-origin',
  });
  return await analyzeResponse(response, 'text');
}

export async function getWithMultipartResponse(url) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'GET',
    headers: jsonHeader,
    credentials: 'same-origin',
  });
  return await analyzeResponse(response, 'blob');
}

export async function postWithTextResponse(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'POST',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response, 'text');
}

export async function postWithMultipartResponse(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'POST',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response, 'blob');
}

export async function postRequest(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'POST',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

export async function putRequest(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'PUT',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

export async function deleteRequest(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'DELETE',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

export async function postMultiPart(url, multipart) {
  const formData = buildFormData(multipart);
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'POST',
    body: formData,
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

export async function deleteRequestByParam(url, param) {
  const response = await window.fetch(`${SERVER}${url}`, {
    method: 'DELETE',
    headers: jsonHeader,
    body: JSON.stringify(param),
    credentials: 'same-origin',
  });
  return await analyzeResponse(response);
}

const analyzeResponse = async (response, type) => {
  const { status } = response;
  let result;

  switch (type) {
    case 'text':
      result = await response.text();
      break;

    case 'blob':
      result = await response.blob();
      break;

    default:
      result = await response.json();
      break;
  }


  switch (status) {
    case 500:
      throw new Error(result.message);
      break;

    default:
      return result;
  }
};
