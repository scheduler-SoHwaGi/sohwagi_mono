import { useQuery } from '@tanstack/react-query';
import { api } from '../utils/axios';

interface OnboardingResponse {
  hasSchedule: boolean;
}

const getOnboardingStatus = async (): Promise<OnboardingResponse> => {
  const response = await api.get('/users/me/onboarding');
  return response.data;
};

export const useOnboardingQuery = () => {
  return useQuery({
    queryKey: ['onboarding'],
    queryFn: getOnboardingStatus,
    staleTime: 5 * 60 * 1000, // 5분
    gcTime: 10 * 60 * 1000, // 10분
  });
};


