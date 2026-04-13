import React from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';

import SchedulePage from '../pages/SchedulePage';
import UsePolicyPage from '../pages/TermPage/UsePolicyPage';
import UserPrivacyPage from '../pages/TermPage/UserPrivacyPage';
import BottomNavi from '../components/BottomNavi';
import PlusPage from '../pages/PlusPage';

export function AppContent() {
  const location = useLocation();

  const showBottomNavi =
    location.pathname === '/' || location.pathname === '/info';

  return (
    <div
      className={`w-full flex flex-col ${showBottomNavi ? 'min-h-[calc(100vh - 86px)]' : 'min-h-screen'}`}
    >
      <Routes>
        <Route path="/" element={<SchedulePage />} />
        <Route path="/info" element={<PlusPage />} />
        <Route path="/info/usePolicy" element={<UsePolicyPage />} />
        <Route path="/info/privacy" element={<UserPrivacyPage />} />
      </Routes>
      {showBottomNavi && <BottomNavi />}
    </div>
  );
}

