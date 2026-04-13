import { ReactNode } from 'react';

interface BodyTapProps {
  children: ReactNode;
}

function BodyTab({ children }: BodyTapProps) {
  return <div className={'ml-4'}>{children}</div>;
}

export default BodyTab;
