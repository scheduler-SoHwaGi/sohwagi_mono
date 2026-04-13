import React from 'react';

import UserPrivacyPage from './UserPrivacyPage';
import HeadTitle from './Component/HeadTitle';

interface Props {
  contentTitle: string;
}

const Index: React.FC<Props> = ({ contentTitle }) => {
  if (contentTitle === '서비스 이용약관') return <UserPrivacyPage />;

  return (
    <>
      <HeadTitle headTitle={'서비스 이용약관'} />
    </>
  );
};

export default Index;
