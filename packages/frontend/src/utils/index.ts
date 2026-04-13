import dayjs, {Dayjs} from 'dayjs';

export const convertDateFormat = (dateString: Dayjs) => {
  return dayjs(dateString).format('YYYY-MM-DD').toString();
};

export const formatTime=(time: string) => {
  const match = time.match(/(오전|오후)\s(\d{1,2})시\s(\d{2})분/);
  if (!match) return time;
  const [, period, hour, minute] = match;
  return `${period} ${hour}:${minute}`;
}

export const getRandomIndex = (max: number) => Math.floor(Math.random() * max);

export const getDailyDateObject = (date: Dayjs) => ({
  year : date.year().toString(),
  month : (date.month() + 1).toString(),
  day : date.date().toString()
})

export const getWeeklyDateObject = (date: Dayjs) => ({
  start : date.startOf('week').format('YYYY-MM-DD'),
  end : date.endOf('week').format('YYYY-MM-DD'),
})

export const getMonthlyDateObject = (date: Dayjs) => ({
  start : date.startOf('month').format('YYYY-MM-DD'),
  end : date.endOf('month').format('YYYY-MM-DD'),
})