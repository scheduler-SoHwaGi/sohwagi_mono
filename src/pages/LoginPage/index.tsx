import { api } from '../../utils/axios';

export default function LoginPage() {
  const onClickHandler = async () => {
    console.log('clicked');
    try {
      const response = await api.post('/oauth/qa');
      console.log(response.data);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="w-full h-full flex items-center justify-center">
      <button className="border" onClick={onClickHandler}>
        로그인버튼
      </button>
    </div>
  );
}
