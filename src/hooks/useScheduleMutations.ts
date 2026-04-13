import { useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosError } from 'axios';
import { api } from '../utils/axios';
import { SCHEDULE_QUERY_KEY } from '../constants/queryKeys';
import { UseSchedulesProps } from '../types/schedule';
import { trackEvent } from '../lib/amplitude';

type CreatedSchedule = {
  id: number;
  text: string;
  startsAt?: string;
  endsAt?: string;
}

type ScheduleErrorResponse = {
  status?: number;
  code?: string;
  message?: string;
  newAccessToken?: string;
}

type ScheduleMutationError = Error & ScheduleErrorResponse;

const DEFAULT_ERROR_MESSAGE = '잠시후에 다시 시도해주세요!';


const useSchedules = (props : UseSchedulesProps = {}) => {
  const { dailyDate, weekRangeDate, monthRangeDate } = props;
  const queryClient = useQueryClient();

  const postScheduleMutation = useMutation({
    mutationFn: async (text: string) => {
      try {
        const res = await api.post('/schedules', { text });
        return res.data as CreatedSchedule;
      } catch (err) {
        const axiosError = err as AxiosError<ScheduleErrorResponse>;
        if (axiosError?.response) {
          const { status, code, message, newAccessToken } = axiosError.response.data ?? {};
          const scheduleError: ScheduleMutationError = Object.assign(
            new Error(message ?? DEFAULT_ERROR_MESSAGE),
            {
              status,
              code,
              newAccessToken,
            },
          );
          throw scheduleError;
        }

        if (err instanceof Error) {
          if (!err.message) {
            err.message = DEFAULT_ERROR_MESSAGE;
          }
          throw err;
        }

        throw new Error(DEFAULT_ERROR_MESSAGE);
      }
    },
    onSuccess: (data, text) => {
      // 성공 이벤트
      trackEvent('register_success', {
        schedule_id: data?.id,
        input_length: text?.length ?? 0,
      })
      // 데이터 최신화
      queryClient.invalidateQueries({ queryKey: SCHEDULE_QUERY_KEY.all });
      // 일정 개수 쿼리도 무효화하여 말풍선 상태 업데이트
      queryClient.invalidateQueries({ queryKey: ['scheduleCount'] });
      // 온보딩 쿼리도 무효화하여 말풍선이 사라지도록 함
      queryClient.invalidateQueries({ queryKey: ['onboarding'] });
    },
    onError: (error: any, text) => {
      // 실패 이밴트
      trackEvent('register_fail', {
        input_length: text?.length ?? 0,
        error_name: error?.name,
        error_message: error?.message,
        finished_at: Date.now(),
      });
    }
  });

  const deleteScheduleMutation = useMutation({
    mutationFn: async (scheduleId: number) => {
      await api.delete(`/schedules/${scheduleId}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: SCHEDULE_QUERY_KEY.daily(dailyDate) });
      queryClient.invalidateQueries({ queryKey: SCHEDULE_QUERY_KEY.weekly(weekRangeDate) });
      if (monthRangeDate) {
        queryClient.invalidateQueries({ queryKey: SCHEDULE_QUERY_KEY.monthly(monthRangeDate) });
      }
    },
  });


  return {
    postSchedule: postScheduleMutation.mutate,
    isPosting: postScheduleMutation.isPending,
    postError: postScheduleMutation.error,
    deleteSchedule: deleteScheduleMutation.mutate,
  };
};

export default useSchedules;
