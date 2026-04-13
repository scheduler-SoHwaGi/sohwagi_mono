import React from 'react';
import bubble from './bubble.svg'

interface OnboardingBubbleProps {
  message: string;
  className?: string;
}

const OnboardingBubble: React.FC<OnboardingBubbleProps> = ({ 
  className = '' 
}) => {
  return (
    <div className={`relative flex items-center justify-center ${className} `}>
        <div className="flex items-center gap-2">
          <img 
            src={bubble} 
            alt="말풍선 아이콘" 
            className="flex-shrink-0" 
          />
 
        </div>
        {/* 말풍선  */}
    </div>
  );
};

export default OnboardingBubble;


