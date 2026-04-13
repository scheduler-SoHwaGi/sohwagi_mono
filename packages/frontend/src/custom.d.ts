declare module '*.svg' {
  const src: string;
  export default src;
}

declare module '*.png' {
  const src: string;
  export default src;
}

declare module '*.svg?react' {
  import * as React from 'react';

  const ReactComponent: React.FunctionComponent<
    React.SVGProps<SVGSVGElement> & { title?: string }
  >;

  export default ReactComponent;
}

interface Window {
  receiveUserInfo?: (userInfo: any) => void;
  webkit?: {
    messageHandlers?: {
      webViewReady?: {
        postMessage: (message: any) => void;
      };
      logoutHandler?: {
        postMessage: (message: any) => void;
      };
      deleteAccountHandler?: {
        postMessage: (message: any) => void;
      };
    };
  };
}
