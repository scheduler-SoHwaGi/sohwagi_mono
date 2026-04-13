import { useNavigate } from 'react-router-dom';
import { Term } from '../../../../types/term';
import arrow from '../../../../assets/images/TermPage/arrow.svg';

function HeadTitle({ headTitle }: Term) {
  const navigate = useNavigate();

  return (
    <div className="flex items-center justify-stretch bg-white font-bold text-[#333] text-[1.5rem] py-[4.25rem] pb-4 w-full">
      <div className="flex justify-around w-full relative">
        <img
          src={arrow}
          alt="arrow"
          onClick={() => navigate(-1)}
          className="w-24 h-24 absolute left-6 cursor-pointer"
        />
        <div className="text-[1.2rem]">{headTitle}</div>
      </div>
    </div>
  );
}

export default HeadTitle;
