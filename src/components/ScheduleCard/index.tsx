import React, { useState } from 'react';

import ScheduleDetail from '../ScheduleDetail';
import BottomSheet from '../BottomSheet';

import {getDailyScheduleListQuery} from '../../hooks/useDailySchedulesQuery';
import DefaultComponent from '../../pages/SchedulePage/DefaultComponent';

import useSchedule from '../../hooks/useScheduleMutations';
import { Dayjs } from 'dayjs';
import { getDailyDateObject, getWeeklyDateObject, getMonthlyDateObject } from '../../utils';

type ScheduleCardProps = {
  selectedDate: Dayjs;
};

const ScheduleCard = ({ selectedDate }: ScheduleCardProps) => {
  const dailyObj = getDailyDateObject(selectedDate);
  const weeklyObj = getWeeklyDateObject(selectedDate);
  const monthlyObj = getMonthlyDateObject(selectedDate);

  const { data: scheduleList = [] } = getDailyScheduleListQuery(dailyObj);
  const  { deleteSchedule } = useSchedule({dailyDate : dailyObj, weekRangeDate: weeklyObj, monthRangeDate: monthlyObj });

  const [isSheetOpen, setSheetOpen] = useState<boolean>(false);
  const [clickedSchedule, setClickedSchedule] = useState<number | null>(null);
  
  const handleSheet = () => {
    setSheetOpen(!isSheetOpen);
    setClickedSchedule(null);
  };

  const onClickHandler = (scheduleId: number) => {
    setClickedSchedule(scheduleId);
    setSheetOpen(true);
  };

  const handleDelete = async () => {
    if (clickedSchedule !== null) {
      deleteSchedule(clickedSchedule);
    }
  };

  return (
    <div className="flex flex-col w-full h-full no-scrollbar flex-shrink-0 gap-6 bg-White rounded-[1.7rem] overflow-y-scroll">
      <div className="flex flex-col gap-16 w-full h-full">
        {scheduleList.length === 0 ? (
          <div className="flex flex-1 w-full h-full justify-center items-center">
            <DefaultComponent />
          </div>
        ) : (
          <div className="flex flex-col gap-35 pt-40 pb-100">
            {scheduleList.map((schedule, idx) => (
              <ScheduleDetail
                key={`sched-${schedule.scheduleId}-${schedule.time ?? 'no-time'}-${idx}`}
                scheduleId={schedule.scheduleId}
                title={schedule.title}
                time={schedule.time}
                checked={schedule.checked}
                dailyDate={dailyObj}
                weekRangeDate={weeklyObj}
                onClick={() => onClickHandler(schedule.scheduleId)}
              />
            ))}
          </div>
        )}
      </div>
      <BottomSheet
        isOpen={isSheetOpen}
        onClose={handleSheet}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default React.memo(ScheduleCard);
