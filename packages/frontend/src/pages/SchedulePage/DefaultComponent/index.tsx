import bubble from '../../../assets/images/DefaultComponent/bubble.svg';

function DefaultComponent() {
  return (
    <div
      className={
        'flex flex-col body_03 text-Grey_04 text-center items-center gap-20'
      }
    >
      <img src={bubble} alt={'bubble'} />
      <p>
        일정을 등록하면 <br /> 귀여운 햄토리가 나타나요!
      </p>
    </div>
  );
}

export default DefaultComponent;
