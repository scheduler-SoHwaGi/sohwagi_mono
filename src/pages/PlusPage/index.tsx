import React, { useEffect, useState } from 'react';
import { api } from '../../utils/axios';

// Import Components
import UserProfile from '../../components/UserProfile';
import AddPageContainer from '../../components/AddPageContainer';
import VersionInfo from '../../components/VersionInfo';

interface UserInfo {
  name: string;
  email: string;
}

const PlusPage = () => {
  const addPageInfoTitles = ['서비스 이용약관', '개인정보 처리방침'];
  const addPageSettingTitles = ['로그아웃', '회원탈퇴'];
  const [user, setUser] = useState<UserInfo | null>(null);

  const getUserInfo = async () => {
    try {
      const response = await api.get('/users/me');
      setUser(response.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  return (
    <div className="bg-white w-full h-full min-h-[calc(100vh-86px)]">
      <div className="flex flex-col pl-17 pt-80 pr-17">
        <UserProfile name={user?.name} email={user?.email} />
        <div className="flex flex-col gap-[2.3125rem]">
          <AddPageContainer title="정보" contentTitles={addPageInfoTitles} />
          <AddPageContainer title="설정" contentTitles={addPageSettingTitles} />
        </div>
        <VersionInfo />
      </div>
    </div>
  );
};

export default PlusPage;
