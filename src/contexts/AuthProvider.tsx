import React from 'react';
import { createContext, ReactNode, useEffect, useState } from 'react';
import { api } from '../utils/axios';

export const AuthContext = createContext(null);

interface MyComponentProps {
  children: ReactNode;
}

const isDevLogin = async() => {
  try{
    const response = await api.post('/oauth/qa');

    localStorage.setItem('accessToken', JSON.stringify(response.data.accessToken).replace(/"/g, ''))
    localStorage.setItem('refreshToken', JSON.stringify(response.data.refreshToken).replace(/"/g, ''))
  }
  catch(error){
    console.log(error);
  }
}

export function AuthProvider({ children }: MyComponentProps) {
  const [authInfo, setAuthInfo] = useState(null);
  const isDev = import.meta.env.DEV;

  useEffect(() => {
    if ( !isDev &&
      window.webkit &&
      window.webkit.messageHandlers &&
      window.webkit.messageHandlers.webViewReady
    ) {
      window.webkit.messageHandlers.webViewReady.postMessage('WebViewReady');
    }

    window.receiveUserInfo = function (info) {
      localStorage.setItem('userID', JSON.stringify(info.userID));
      localStorage.setItem(
        'accessToken',
        JSON.stringify(info.accessToken).replace(/"/g, ''),
      );
      localStorage.setItem(
        'refreshToken',
        JSON.stringify(info.refreshToken).replace(/"/g, ''),
      );
      localStorage.setItem(
        'authorizationCode',
        JSON.stringify(info.authorizationCode),
      );

      setAuthInfo(info);
    };

    if (isDev) {
      isDevLogin();
    }
  }, []);

  return (
    <AuthContext.Provider value={authInfo}>{children}</AuthContext.Provider>
  );
}
