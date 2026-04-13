import { useQuery } from '@tanstack/react-query';
import { api } from '../utils/axios';

interface ScheduleCountResponse {
  count: number;
}

const getScheduleCount = async (): Promise<ScheduleCountResponse> => {
  // 오늘 날짜로 일정 조회
  const today = new Date();
  const response = await api.get('/schedules', {
    params: { 
      year: today.getFullYear(),
      month: today.getMonth() + 1,
      day: today.getDate()
    },
  });
  
  const { todo = [], schedules = [] } = response.data;
  const totalCount = todo.length + schedules.length;
  
  return { count: totalCount };
};

export const useScheduleCountQuery = () => {
  return useQuery({
    queryKey: ['scheduleCount'],
    queryFn: getScheduleCount,
    staleTime: 1 * 60 * 1000, // 1분
    gcTime: 5 * 60 * 1000, // 5분
  });
};


