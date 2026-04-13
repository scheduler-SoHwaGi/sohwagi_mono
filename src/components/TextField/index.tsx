import React, { useEffect, useState, useRef, useMemo } from 'react';
import useSchedules from '../../hooks/useScheduleMutations';
import LoadingSpinner from '../LoadingSpinner';
import ToastBar from '../ToastBar';
import { Dayjs } from 'dayjs';
import { getDailyDateObject, getWeeklyDateObject } from '../../utils';
import { trackEvent } from '../../lib/amplitude';



type TextFieldProps = {
  selectedDate: Dayjs;
  needsOnboarding?: boolean;
}

const DEFAULT_TOAST_MESSAGE = '잠시후에 다시 시도해주세요!';

const TextField = ({ selectedDate, needsOnboarding = false }: TextFieldProps) => {
  const [isOpenToast, setIsOpenToast] = useState(true);
  const [inputValue, setInputValue] = useState<string>('');
  const inputRef = useRef<HTMLInputElement>(null);
  
  // 온보딩이 필요한 경우 초기값 설정
  useEffect(() => {
    if (needsOnboarding && !inputValue) {
      setInputValue('내일 오후 7시 소화기 회의할거야');
    } else if (!needsOnboarding && inputValue === '내일 오후 7시 소화기 회의할거야') {
      // onboarding이 false가 되면 초기값을 초기화하여 일반 상태로 복귀
      setInputValue('');
    }
  }, [needsOnboarding, inputValue]);

  const dailyObject = getDailyDateObject(selectedDate);
  const weeklyObj = getWeeklyDateObject(selectedDate);

  const { postSchedule, isPosting, postError } = useSchedules({dailyDate : dailyObject, weekRangeDate: weeklyObj});

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const trimmedValue = inputValue.trim();

    if (!trimmedValue) return;

    postSchedule(trimmedValue);

    trackEvent('register_button', {
      input_length: trimmedValue.length,
    })

    setInputValue('');
  };

  const closeToast = () => {
    setIsOpenToast(!isOpenToast);
  };

  const toastMessage = useMemo(() => {
    if (!postError) return DEFAULT_TOAST_MESSAGE;

    if (postError instanceof Error && postError.message) {
      return postError.message;
    }

    if (typeof postError === 'string') {
      return postError;
    }

    return DEFAULT_TOAST_MESSAGE;
  }, [postError]);

  useEffect(() => {
    if (postError) {
      setIsOpenToast(false);
      setTimeout(() => setIsOpenToast(true), 10);
    }
  }, [postError]);

  return (
    <form
      className={'sticky pt-80 pb-37 top-0 flex w-full'}
      onSubmit={handleSubmit}
    >
      <div className={'w-full relative'}>
        <input
          className={
            ' w-full px-[1.0625rem] py-[0.8125rem] pr-[3.6rem] outline-none border border-transparent text-Grey_06 body_03 bg-Grey_02 rounded-15 focus:border-1 focus:border-Grey_06 placeholder:text-Grey_04 placeholder:text-14' +
    ' focus:ring-0 focus:ring-Grey_06' 
          }
          type="text"
          ref={inputRef}
          placeholder="예) 오늘 오후 7시 팀플 회의"
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button
          className={`absolute right-10 top-1/2 -translate-y-1/2 px-[0.62rem] py-[0.31rem] 
                    ${inputValue ? 'bg-Grey_06' : 'bg-Grey_04'} text-White 
                    rounded-[1.875rem] body_04 focus:bg-Grey_06`}
          type="submit"
          disabled={!inputValue.trim()}
        >
          등록
        </button>
      </div>
      {postError && isOpenToast && (
        <ToastBar msg={toastMessage} onClose={closeToast} />
      )}
      {isPosting && <LoadingSpinner />}
    </form>
  );
};

export default React.memo(TextField);
