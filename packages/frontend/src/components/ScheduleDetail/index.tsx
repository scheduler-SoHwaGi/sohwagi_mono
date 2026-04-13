import React, { useState } from 'react';

import { formatTime, getRandomIndex } from '../../utils';
import { colorPairs } from './constants';
import {useHandleChecked} from '../../hooks/useHandleChecked';

import CheckedBox from './CheckedBox';
import DefaultBox from './DefaultBox';
import { DailyScheduleDate, WeeklyScheduleRange } from '../../types/schedule';
import { trackEvent } from '../../lib/amplitude';

type Props = {
  scheduleId: number;
  time?:string;
  title: string;
  checked: boolean;
  onClick: () => void;
  dailyDate : DailyScheduleDate,
  weekRangeDate : WeeklyScheduleRange
};

const ScheduleDetail = ({ scheduleId, time, title, onClick, checked, dailyDate, weekRangeDate }: Props) => {
  const [done, setDone] = useState<boolean>(checked);
  const [colorIndex, setColorIndex] = useState(getRandomIndex(colorPairs.length));

  const { mutate: toggleCheck } = useHandleChecked({ dailyDate, weekRangeDate });

  const onCheckClick = () => {
    const newDone = !done;
    setDone(newDone);
    toggleCheck(scheduleId);

    trackEvent('complete_schedule', {
      schedule_id: scheduleId,
      timestamp: new Date().toISOString(),
    })

    if (newDone) {
      const randomIndex = getRandomIndex(colorPairs.length);
      setColorIndex(randomIndex);
    }
  }

  return (
    <div className="flex items-center justify-start gap-[0.9rem] w-full ">
      {done ? (
        <CheckedBox
          className="w-28 h-28"
          bgColor={colorPairs[colorIndex].bg}
          iconColor={colorPairs[colorIndex].icon}
          onClick={onCheckClick}
        />
      ) : (
        <DefaultBox onClick={onCheckClick} className={'cursor-pointer'} />
      )}
      <div
        onClick={onClick}
        className="flex flex-col cursor-pointer w-full gap-[0.05rem]"
      >
        {time && (<div className="text-Grey_05 body_05">{formatTime(time)}</div>)}
        <div className="text-Grey_06 body_02">{title}</div>
      </div>
    </div>
  );
};

export default ScheduleDetail;
