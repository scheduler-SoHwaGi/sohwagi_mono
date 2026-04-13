import { useQuery } from '@tanstack/react-query';
import { api } from '../utils/axios';
import { SCHEDULE_QUERY_KEY } from '../constants/queryKeys';
import { Dayjs } from 'dayjs';

type WeekData = {
  date: string;
  counts: number;
  status: string;
};

// 일정 수 조회 API
const getScheduleCounts = async (startOfWeek: Dayjs, endOfWeek: Dayjs) => {
  const start = startOfWeek.format('YYYY-MM-DD');
  const end = endOfWeek.format('YYYY-MM-DD');

  const response = await api.get('/schedules/counts', {
    params: { startDate: start, endDate: end },
  });

  return response.data.scheduleCounts as WeekData[];
};

export const useWeeklyScheduleCountsQuery = (startOfWeek: Dayjs, endOfWeek: Dayjs) => {
  const start = startOfWeek.format('YYYY-MM-DD');
  const end = endOfWeek.format('YYYY-MM-DD');

  return useQuery({
    queryKey: SCHEDULE_QUERY_KEY.weekly({ start, end }),
    queryFn: () => getScheduleCounts(startOfWeek, endOfWeek),
  });
};

export const useMonthlyScheduleCountsQuery = (selectedDate: Dayjs) => {
  const startOfMonth = selectedDate.startOf('month');
  const endOfMonth = selectedDate.endOf('month');

  const start = startOfMonth.format('YYYY-MM-DD');
  const end = endOfMonth.format('YYYY-MM-DD');

  return useQuery({
    queryKey: SCHEDULE_QUERY_KEY.monthly({ start, end }),
    queryFn: () => getScheduleCounts(startOfMonth, endOfMonth),
  });
};