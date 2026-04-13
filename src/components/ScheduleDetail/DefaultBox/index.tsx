import React from 'react';

interface CustomDefaultBoxProps {
  className:string;
  onClick?: () => void;
}

const DefaultBox = ({className, onClick} : CustomDefaultBoxProps) => {
  return(
  <svg onClick={onClick} className={className} width="28" height="28" viewBox="0 0 28 28" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="27.9259" height="27.9259" rx="9" fill="#F1F4F5"/>
  </svg>
  )
}
export default DefaultBox;