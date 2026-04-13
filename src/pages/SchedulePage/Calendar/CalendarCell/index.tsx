import React from 'react';

import selectedEmoji from '../../../../assets/images/Calendar/selectedEmoji.svg';
import defaultEmoji from '../../../../assets/images/Calendar/defaultEmoji.svg';
import CheckEmoji from '../../../../assets/images/check.svg?react';
import { Dayjs } from 'dayjs';

type Props = {
  day: Dayjs;
  selectedDate: Dayjs;
  setSelectedDate: (date: Dayjs) => void;
  status: string;
  counts: number;
};

function CalendarCell({day, selectedDate, setSelectedDate, status, counts} : Props) {

  const isSelected = selectedDate?.isSame(day, 'day');
  return (
    <div
    className={`flex flex-col items-center body_05 gap-6 hover:cursor-pointer text-center ${isSelected ? 'text-Grey_06' : 'text-Grey_03'}`}
    onClick={() => setSelectedDate(day)}
  >
    <div className={'relative '}>
      <img
        alt={'emoji'}
        src={isSelected ? selectedEmoji : defaultEmoji}
        className={'w-32 h-34'}
      />
      <div
        className={`absolute inset-0 flex items-center justify-center  ${isSelected ? 'text-white' : 'text-Grey_04'}`}
      >
        {status === "DONE" ? <CheckEmoji style={{ color: isSelected ? '#ffffff' : '#DADDE0', cursor: 'pointer' }} className="w-18 h-16 white"/> :counts !==0 ? counts : null }
      </div>
    </div>
    <div>{day.format('D') + '일 '}</div>
  </div>
  )
}
export default React.memo(CalendarCell);