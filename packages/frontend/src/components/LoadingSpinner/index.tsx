import React from 'react';

function LoadingSpinner() {
  return (
    <div className="w-full absolute bottom-0 flex flex-row">
      <p className="text-gray-4 text-12">일정을 생성하고 있어요!</p>
      <div className="animate-spin rounded-full h-15 w-15 border-t-2 border-b-2 border-blue-500"/>
    </div>
  )
}
export default LoadingSpinner;