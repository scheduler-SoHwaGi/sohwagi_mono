import React from 'react';

interface CustomCheckBoxProps {
  bgColor: string;
  iconColor: string;
  className?: string;
  onClick?: () => void;
}

const CheckedBox:React.FC<CustomCheckBoxProps> = ({
  bgColor='#4093FD', iconColor='#FFFFFF', className, onClick}) => {
  return (
    <svg className={className} onClick={onClick} width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g clipPath="url(#clip0_1491_3058)">
        <rect width="27.9259" height="27.9259" rx="9" fill={bgColor}/>
        <path d="M7.33301 5.46809C9.45067 4.98671 11.7551 6.32593 12.4795 8.4593C12.5809 8.75788 12.6446 9.05634 12.6758 9.34993C13.2302 9.30059 13.7938 9.27473 14.3643 9.27473C14.9345 9.27474 15.4975 9.30062 16.0518 9.34993C16.0829 9.05627 16.1476 8.75795 16.249 8.4593C16.9736 6.32608 19.277 4.98683 21.3945 5.46809C23.5121 5.94955 24.6414 8.06918 23.917 10.2025C23.7498 10.6946 23.4972 11.1434 23.1836 11.5384C27.184 13.8208 29.8016 17.6128 29.8018 21.9056C29.8018 28.8815 22.8904 34.5373 14.3643 34.5374C5.83799 34.5374 -1.07422 28.8816 -1.07422 21.9056C-1.07405 17.6131 1.54309 13.8208 5.54297 11.5384C5.22959 11.1435 4.9776 10.6944 4.81055 10.2025C4.0862 8.06923 5.21558 5.94961 7.33301 5.46809Z" fill={iconColor}/>
        <path d="M10.7402 17.9012L13.3829 21.7183C13.6651 22.126 14.2661 22.1304 14.5543 21.727L19.3328 15.037" stroke="white" strokeWidth="1.4321" strokeLinecap="round"/>
      </g>
      <defs>
        <clipPath id="clip0_1491_3058">
          <rect width="27.9259" height="27.9259" rx="9" fill="none"/>
        </clipPath>
      </defs>
    </svg>

  )
}
export default CheckedBox;