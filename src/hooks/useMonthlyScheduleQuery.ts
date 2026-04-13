import { useQuery } from '@tanstack/react-query';
import { api } from '../utils/axios';
import { SCHEDULE_QUERY_KEY } from '../constants/queryKeys';
import { Dayjs } from 'dayjs';

type WeekData = {
  date: string;
  counts: number;
  status: string;
};

