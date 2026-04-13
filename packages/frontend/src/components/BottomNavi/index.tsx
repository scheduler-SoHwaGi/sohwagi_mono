import React from 'react';
import { Link, useLocation } from 'react-router-dom';

import plus_act from '../../assets/images/BottomNavi/plus_act.svg';
import plus_nonact from '../../assets/images/BottomNavi/plus_nonact.svg';
import schedule_act from '../../assets/images/BottomNavi/schedule_act.svg';
import schedule_nonact from '../../assets/images/BottomNavi/schedule_nonact.svg';

const BottomNavi = () => {
  const location = useLocation();
  const isScheduleActive = location.pathname === '/';
  const isMoreActive = location.pathname === '/info';

  return (
    <footer
      id="footer"
      className="fixed bottom-0 w-screen h-86 flex justify-around bg-White p-16 pb-60 border-t border-Grey_03 shadow-none z-[1000] overflow-hidden"
    >
      {/* 일정 영역 */}
      <Link to="/" className="no-underline">
        <div className="flex flex-col items-center cursor-pointer">
          <img
            src={isScheduleActive ? schedule_act : schedule_nonact}
            alt="일정 아이콘"
            className="w-22 h-22 mb-1"
          />
          <span
            className={`body_05 ${isScheduleActive ? 'text-Grey_06' : 'text-Grey_02'}`}
          >
            일정
          </span>
        </div>
      </Link>

      {/* 더보기 영역 */}
      <Link to="/info" className="no-underline">
        <div className="flex flex-col items-center cursor-pointer">
          <img
            src={isMoreActive ? plus_act : plus_nonact}
            alt="더보기 아이콘"
            className="w-22 h-22 mb-1"
          />
          <span
            className={`body_05 ${isMoreActive ? 'text-Grey_06' : ' text-Grey_02'}`}
          >
            더보기
          </span>
        </div>
      </Link>
    </footer>
  );
};

export default BottomNavi;
