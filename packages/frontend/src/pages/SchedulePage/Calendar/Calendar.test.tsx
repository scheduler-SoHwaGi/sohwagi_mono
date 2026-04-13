import React from 'react';
import dayjs from 'dayjs';
import { vi, describe, it, expect, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Calendar from './index';

vi.mock('../../../assets/images/check.svg?react', () => ({
  default: () => <svg data-testid="check-icon" />,
}));


// ✅ useWeeklyScheduleCountsQuery 훅 mock
vi.mock('../../../hooks/useWeeklyScheduleCountsQuery', () => {
  const today = dayjs();
  const startOfWeek = today.startOf('week');
  return {
    default: () => ({
      data: [
        {
          date: startOfWeek.format('YYYY-MM-DD'),
          counts: 2,
          status: null,
        },
        {
          date: startOfWeek.add(1, 'day').format('YYYY-MM-DD'),
          counts: 0,
          status: 'DONE',
        },
      ],
    }),
  };
});

// ✅ 기본 테스트
describe('Calendar component', () => {
  const mockSetSelectedDate = vi.fn();
  const today = dayjs();

  beforeEach(() => {
    render(<Calendar selectedDate={today} setSelectedDate={mockSetSelectedDate} />);
  });

  it('renders 7 days of the current week', () => {
    const dayLabels = ['일', '월', '화', '수', '목', '금', '토'];
    dayLabels.forEach((label) => {
      expect(screen.getByText(label)).toBeInTheDocument();
    });
  });

  it('calls setSelectedDate when a day cell is clicked', () => {
    const dayElements = screen.getAllByText(/일|월|화|수|목|금|토/);
    fireEvent.click(dayElements[0]);
    expect(mockSetSelectedDate).toHaveBeenCalled();
  });

  it('calls setSelectedDate with previous week date when prev button is clicked', () => {
    const prevButton = screen.getByAltText('prevBtn');
    fireEvent.click(prevButton);
    expect(mockSetSelectedDate).toHaveBeenCalledWith(today.subtract(1, 'week'));
  });

  it('calls setSelectedDate with next week date when next button is clicked', () => {
    const nextButton = screen.getByAltText('nextBtn');
    fireEvent.click(nextButton);
    expect(mockSetSelectedDate).toHaveBeenCalledWith(today.add(1, 'week'));
  });

  it('renders correct count', () => {
    // 첫 번째 날짜: count = 2
    expect(screen.getByText('2')).toBeInTheDocument();
  });

  it('renders check emoji when status is DONE', () => {
    expect(screen.getByTestId('check-icon')).toBeInTheDocument();
  })
});