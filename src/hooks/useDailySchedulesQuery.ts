import { useQuery } from '@tanstack/react-query';
import { api } from '../utils/axios';
import { SCHEDULE_QUERY_KEY } from '../constants/queryKeys';
import { DailyScheduleDate } from '../types/schedule';

const getSchedules = async ({ year, month, day }: DailyScheduleDate) => {
  const response = await api.get('/schedules', {
    params: { year, month, day },
  });
  const { todo = [], schedules = [] } = response.data;

  const normalizedTodo = todo.map((t: any) => ({
    ...t,
    time: null,
    type: 'todo',
  }));

  const normalizedSchedules = schedules.map((s: any) => ({
    ...s,
    type: 'schedule',
  }));

  return [...normalizedTodo, ...normalizedSchedules];
};

// 특정 날짜의 일정을 불러오는 react query
export const getDailyScheduleListQuery = ({ year, month, day }: DailyScheduleDate )=> {
  return useQuery({
    queryKey: SCHEDULE_QUERY_KEY.daily({ year, month, day }),
    queryFn: () => getSchedules({ year, month, day }),
  });
};