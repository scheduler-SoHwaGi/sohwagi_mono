import { DailyScheduleDate, WeeklyScheduleRange } from '../types/schedule';

export const SCHEDULE_QUERY_KEY = {
  all: ['SCHEDULE'] as const,
  daily: ({ year, month, day }: DailyScheduleDate) =>
    [...SCHEDULE_QUERY_KEY.all, 'DAILY', year, month, day] as const,
  weekly: ({ start, end } : WeeklyScheduleRange) =>
    [...SCHEDULE_QUERY_KEY.all, 'WEEKLY', start, end] as const,
  monthly: ({ start, end } : WeeklyScheduleRange) =>
    [...SCHEDULE_QUERY_KEY.all, 'MONTHLY', start, end] as const,
};