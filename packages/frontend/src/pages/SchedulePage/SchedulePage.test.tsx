import { expect, test } from 'vitest';
import { render, screen } from '@testing-library/react';

import SchedulePage from './index';

test('placeholder가 보인다', () => {
  render(<SchedulePage />);
  expect(
    screen.getByPlaceholderText('예 ) 오늘 오후 7시 팀플 회의'),
  ).toBeInTheDocument();
});
