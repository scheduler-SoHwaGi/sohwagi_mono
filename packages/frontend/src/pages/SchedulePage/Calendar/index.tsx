import React, { useCallback, useMemo, useState } from 'react';
import dayjs, { Dayjs } from 'dayjs';
import 'dayjs/locale/ko';
import updateLocale from 'dayjs/plugin/updateLocale';

import prevBtn from '../../../assets/images/Calendar/prevWeekBtn.svg';
import nextBtn from '../../../assets/images/Calendar/nextWeekBtn.svg';

import { useWeeklyScheduleCountsQuery, useMonthlyScheduleCountsQuery } from '../../../hooks/useWeeklyScheduleCountsQuery';
import CalendarCell from './CalendarCell';
import { convertDateFormat } from '../../../utils';

type WeeklyCalendarProps = {
  selectedDate: Dayjs;
  setSelectedDate: (date: Dayjs) => void;
};

dayjs.locale('ko');
dayjs.extend(updateLocale);
dayjs.updateLocale('ko',{
  weekdaysMin: ['일', '월', '화',  '수', '목', '금', '토'],
  weekStart: 0
})

const Calendar = ({
                          selectedDate,
                          setSelectedDate,
                        }: WeeklyCalendarProps) => {
  const [isViewMonthly, setIsViewMonthly ] = useState(false)
  const startOfWeek = useMemo(() => selectedDate.startOf('week'), [selectedDate]);
  const endOfWeek = useMemo(() => selectedDate.endOf('week'), [selectedDate]);

  const { data: weeklyScheduleCounts } = useWeeklyScheduleCountsQuery(startOfWeek, endOfWeek);
  const { data: monthlyScheduleCounts } = useMonthlyScheduleCountsQuery(selectedDate);

  const scheduleData = isViewMonthly ? monthlyScheduleCounts : weeklyScheduleCounts;

  const scheduleMap = useMemo(() => {
    if(!scheduleData) return new Map();
    return new Map(scheduleData.map(item => [item.date, item]));
  }, [scheduleData]);

  const days = Array.from({ length: 7 }).map((_, idx) =>
      startOfWeek.add(idx, 'day'),
  );

  const goToPreviousWeek = useCallback(() =>  setSelectedDate(isViewMonthly? selectedDate.subtract(1, 'month') :selectedDate.subtract(1, 'week')), [isViewMonthly, selectedDate, setSelectedDate]);
  const goToNextWeek = useCallback(() =>  setSelectedDate(isViewMonthly?selectedDate.add(1, 'month'):selectedDate.add(1, 'week')), [isViewMonthly, selectedDate, setSelectedDate]);


  // 월간 캘린더 부분
  const monthStart = useMemo(() => selectedDate.startOf('month'), [selectedDate]);
  const monthGridDays = useMemo(()=>{
    const gridStart = monthStart.startOf('week');
    const gridEnd = monthStart.endOf('month').endOf('week');

    const days:Dayjs[] = [];
    let d = gridStart;
    while (d.isBefore(gridEnd) || d.isSame(gridEnd, 'day')) {
      days.push(d);
      d= d.add(1, 'day')
    }
    return days;
  }, [monthStart])

  const monthWeeks = useMemo(() => {
    const chunks: Dayjs[][] = [];
    for (let i = 0; i < monthGridDays.length; i += 7) {
      chunks.push(monthGridDays.slice(i, i + 7));
    }
    return chunks;
  }, [monthGridDays])

  return (
    <div className="w-full">
      <div className={'flex place-content-between pb-20'}>
        <span className={'body_01'}>{selectedDate.format('YYYY. MM')}</span>

        <div className={'flex gap-15'}>

          <button onClick={() => setIsViewMonthly(!isViewMonthly)} className={"border border-Grey_03 rounded-full px-10"}>
            {isViewMonthly? <div>주</div> : <div>월</div>}
          </button>
          <button onClick={goToPreviousWeek}>
            <img className={'w-13'} src={prevBtn} alt="prevBtn" />
          </button>
          <button onClick={goToNextWeek}>
            <img className={'w-13'} src={nextBtn} alt="nextBtn" />
          </button>
        </div>
      </div>

      <div className="grid grid-cols-7 mb-8">
        {['일','월','화','수','목','금','토'].map((w) => (
          <div key={`dow-${w}`} className="text-center body_05 text-Grey_04">
            {w}
          </div>
        ))}
      </div>

      {isViewMonthly ? (
        <div key={`month-${selectedDate.format('YYYY-MM')}`}>
          {monthWeeks.map((week) =>(
            <div key = {`week-${week[0].format('YYYY-MM-DD')}`} className='grid grid-cols-7'>
              {week.map((day) => {
               const isCurrentMonth = day.isSame(selectedDate, 'month');
                const certainDay = convertDateFormat(day);

                const matchingDate = scheduleMap.get(certainDay);

                return (
                  <div key={`day-${day.format('YYYY-MM-DD')}`}
                  className={`flex justify-center items-center ${isCurrentMonth? '':'opacity-40'}`}>
                    <CalendarCell
                      key = {day.format('YYYY-MM-DD')}
                      day={day}
                      selectedDate={selectedDate}
                      setSelectedDate={setSelectedDate}
                      status={matchingDate?.status}
                      counts={matchingDate?.counts}
                      />
                  </div>
                )
              })}
            </div>
          ))}
        </div>
      ): (
        <div className={'grid grid-cols-7'}>
          {days.map((day) => {
            const certainDay = convertDateFormat(day);
            const matchingDate = scheduleMap.get(certainDay);

            return (
              <CalendarCell
                key={day.format('YYYY-MM-DD')}
                day={day}
                selectedDate={selectedDate}
                setSelectedDate={setSelectedDate}
                status={matchingDate?.status}
                counts={matchingDate?.counts}
              />
            );
          })}
        </div>
        )

      }
    </div>
  );
};

export default React.memo(Calendar);
