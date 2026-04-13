import React, { useCallback, useEffect, useState } from 'react';
import { trackEvent } from '../../lib/amplitude';

import TextField from '../../components/TextField';
import ScheduleCard from '../../components/ScheduleCard';
import Calendar from './Calendar';
import OnboardingBubble from '../../components/OnboardingBubble';
import { useOnboardingQuery } from '../../hooks/useOnboardingQuery';
import { useScheduleCountQuery } from '../../hooks/useScheduleCountQuery';
import dayjs, { Dayjs } from 'dayjs';

const SchedulePage = () => {
  const [selectedDate, setSelectedDate] = useState(dayjs());
  const setDate = useCallback((date: Dayjs) => setSelectedDate(date), []);

  // 온보딩 상태와 일정 개수 조회
  const { data: onboardingData, isLoading: isOnboardingLoading } = useOnboardingQuery();
  const { data: scheduleCountData, isLoading: isScheduleCountLoading } = useScheduleCountQuery();

  // 말풍선 노출 조건: 온보딩이 필요하고 일정이 1개 미만인 경우
  const hasNoSchedule = onboardingData && typeof onboardingData === 'object' && 'hasSchedule' in onboardingData && 
                         onboardingData.hasSchedule === false;
  const hasLessThanOneSchedule = scheduleCountData && typeof scheduleCountData === 'object' && 'count' in scheduleCountData && 
                                  typeof scheduleCountData.count === 'number' && scheduleCountData.count < 1;
  
  // 단순히 hasSchedule이 false일 때만 말풍선을 띄움 (일정 개수와 무관하게)
  const shouldShowBubble = hasNoSchedule && !isOnboardingLoading;

  useEffect(() => {
    trackEvent('view_home', { page: '/'})
  }, [])

  return (
    <div className="flex w-full flex-col px-18 h-screen no-scrollbar relative">
      {shouldShowBubble && (
        <div className="absolute top-[140px] right-[0] z-20">
          <OnboardingBubble message="한 줄로 일정 등록 끝!"  />
        </div>
      )}
      <div className="sticky bg-white top-0 z-9">
        <div>
          <TextField 
            selectedDate = {selectedDate} 
            needsOnboarding={hasNoSchedule && !isOnboardingLoading}
          />
        </div>
        <div>
          <Calendar
            selectedDate={selectedDate}
            setSelectedDate={setDate}
          />
        </div>
      </div>
      <div className="flex-1 h-full overflow-y-auto">
        <ScheduleCard selectedDate = {selectedDate} />
      </div>
    </div>
  );
};

export default SchedulePage;
