import { ReactNode } from 'react';

interface BodycontentProps {
  title: string;
  children: ReactNode;
}

function BodyContent({ title, children }: BodycontentProps) {
  return (
    <div className={'mb-4'}>
      <div className={'font-bold'}>{title}</div>
      <div>{children}</div>
    </div>
  );
}

export default BodyContent;
