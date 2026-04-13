import { api } from '../utils/axios';
import { useQueryClient, useMutation } from '@tanstack/react-query';
import { SCHEDULE_QUERY_KEY } from '../constants/queryKeys';


export const useHandleChecked = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async (scheduleId: number) => {
      await api.post(`/schedules/${scheduleId}/actions/toggle-checked`);
    },

    onSuccess: () => {
      queryClient.invalidateQueries({queryKey: SCHEDULE_QUERY_KEY.all})
    }
  })
}

