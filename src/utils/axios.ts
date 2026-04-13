import axios from 'axios';
import { trackEvent } from '../lib/amplitude';

const isTest =
  typeof process !== 'undefined' &&
  typeof process.env !== 'undefined' &&
  process.env.JEST_WORKER_ID !== undefined;

function getBaseURL() {
  if (isTest) {
    return process.env.VITE_BASE_SERVER_URL;
  } else {
    return import.meta.env.VITE_BASE_SERVER_URL;
  }
}

const baseURL = getBaseURL();

if (!baseURL) {
  throw new Error('[ENV ERROR] VITE_BASE_SERVER_URL가 설정되지 않았습니다.');
}

export const api = axios.create({
  baseURL: baseURL,
  headers: {
    'content-type': 'application/json',
  },
  withCredentials: true,
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    if (accessToken) {
      config.headers['X-ACCESS-TOKEN'] = `${accessToken}`;
      config.headers['X-REFRESH-TOKEN'] = `${refreshToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 응답 인터셉터
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 403 && !originalRequest._retry) {
      //만료된 access token일 때
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const getNewAccessToken = error.response.data?.newAccessToken;

        if (refreshToken && getNewAccessToken) {
          localStorage.setItem('accessToken', getNewAccessToken);
          originalRequest.headers['X-ACCESS-TOKEN'] = `${getNewAccessToken}`;
        }
        return api(originalRequest);
      } catch (error) {
        // refreshtoken으로 인한 재발급에서도 오류가 나는 상황 -> swift에 브릿지 연결
        console.error('Token failed', error);
        handleErrorInTokenRefresh();
        return Promise.reject(error);
      }
    }

    const config = error.config;
    const url = config?.url;
    const method = config?.method;

    trackEvent('network_error', {
        url,
      method,
      status: error.response?.status,
      error_message: error.message,
      timestamp: new Date().toISOString(),
    })
    return Promise.reject(error);
  },
);

function handleErrorInTokenRefresh() {
  if (
    window.webkit &&
    window.webkit.messageHandlers &&
    window.webkit.messageHandlers.logoutHandler
  ) {
    window.webkit.messageHandlers.logoutHandler.postMessage('logout');
  } else console.log('Logout handler not found');
}
