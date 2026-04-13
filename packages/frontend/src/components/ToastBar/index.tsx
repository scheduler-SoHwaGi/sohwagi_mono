import React from 'react';
import close from './assets/images/close.svg';
import warning from './assets/images/warning.svg';

interface ToastBarProps {
  msg: string;
  onClose : () => void;
}

const ToastBar: React.FC<ToastBarProps> = ({ msg, onClose }) => {


  return (
    <div className="fixed bottom-[7rem] w-[calc(100%-2rem)] 
                px-[1rem] py-[1.25rem] bg-Grey_06 
                rounded-[0.625rem] inline-flex flex-col justify-center 
                items-center gap-[0.625rem] overflow-hidden z-50 left-0 right-0 mx-auto">
      <div className="w-full inline-flex justify-between items-center">
        <div className="flex items-center gap-[0.5rem]">
          <div className="w-[1.875rem] h-[1.875rem] relative overflow-hidden">
            <img src={warning} alt="경고 아이콘" className="w-full h-full" />
          </div>
          <p className="text-White Content_01 break-words">
            {msg}
          </p>
        </div>
        <div className="w-[1.875rem] h-[1.875rem] relative overflow-hidden cursor-pointer" onClick={onClose}>
          <img src={close} alt="닫기 아이콘" className="w-full h-full" />
        </div>
      </div>
    </div>
  );
};

export default ToastBar;

