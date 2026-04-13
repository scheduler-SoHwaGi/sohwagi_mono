import React from 'react';
import {
  BrowserRouter as Router,
} from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools'

import { AuthProvider } from './contexts/AuthProvider';
import { AppContent } from './layout/AppContent';

import { initAmplitude } from './lib/amplitude';
initAmplitude();

const queryClient = new QueryClient();
const isDev = import.meta.env.DEV;

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      {isDev && <ReactQueryDevtools initialIsOpen={false} />}
      <AuthProvider>
        <Router>
          <AppContent />
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
