import React from 'react';
import { useNavigate } from 'react-router-dom';
import rightArrow from '../../assets/images/rightArrow.svg';
import { trackEvent } from '../../lib/amplitude';

type Props = {
  title: string;
  contentTitles: string[];
};

const AddPageContainer = ({ title, contentTitles }: Props) => {
  const navigate = useNavigate();

  const onClickHandler = (contentTitle: string) => {
    if (title === '정보') {
      if (contentTitle === '서비스 이용약관') navigate('/info/usePolicy');
      if (contentTitle === '개인정보 처리방침') navigate('/info/privacy');
    }
    if (title === '설정') {
      if (contentTitle === '로그아웃') {
        if (
          window.webkit &&
          window.webkit.messageHandlers &&
          window.webkit.messageHandlers.logoutHandler
        ) {
          trackEvent('log_out', {
            user_id: localStorage.getItem('userID'),
            timestamp: new Date().toISOString()
          })

          window.webkit.messageHandlers.logoutHandler.postMessage('logout');
        } else {
          console.error('Logout handler not found');
        }
      }
      if (contentTitle === '회원탈퇴') {
        if (
          window.webkit &&
          window.webkit.messageHandlers &&
          window.webkit.messageHandlers.deleteAccountHandler
        ) {
          trackEvent('withdraw_account', {
            user_id: localStorage.getItem('userID') || 'anonymous'
            // TODO: Add reason, duration_since_signup, has_schedule_data when data is available
          })

          window.webkit.messageHandlers.deleteAccountHandler.postMessage(
            'deleteAccount',
          );
        } else {
          console.error('DeleteAccount handler not found');
        }
      }
    }
  };

  return (
    <div className="flex flex-col gap-5">
      <div className="body_05">{title}</div>
      <div className="flex flex-col gap-[1.6875rem]">
        {contentTitles.map((contentTitle, index) => (
          <div
            key={`content-${index}`}
            onClick={() => onClickHandler(contentTitle)}
            className="flex justify-between hover:cursor-pointer"
          >
            <div className="body_03">{contentTitle}</div>
            <img src={rightArrow} alt="right arrow" className="w-13 h-13" />
          </div>
        ))}
      </div>
    </div>
  );
};

export default AddPageContainer;
