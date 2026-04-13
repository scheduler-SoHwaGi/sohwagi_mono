import HeadTitle from '../Component/HeadTitle';
import BodyContent from '../Component/BodyContent';
import BodyTab from '../Component/BodyTab';

function UsePoliciesPage() {
  return (
    <div className="flex flex-col h-full bg-white">
      <HeadTitle headTitle="서비스 이용약관" />
      <div className="flex flex-col overflow-y-scroll p-4">
        <BodyContent title="제 1조 목적">
          본 약관은 (주)소화기(이하 “회사”)가 제공하는 소화기 서비스의 이용과
          관련하여 회사와 이용자 간의 권리, 의무 및 책임 사항 등을 규정함을
          목적으로 합니다.
        </BodyContent>

        <BodyContent title="제 2조 용어의 정의">
          1. 본 약관에서 사용하는 용어의 정의는 다음과 같습니다
          <BodyTab>
            1. “서비스”란, 회사가 제공하는 모든 서비스 및 기능을 말합니다.
            <br />
            2. “이용자”란, 본 약관에 따라 서비스를 이용하는 회원 및 비회원들을
            말합니다.
            <br />
            3. “회원”이란, 서비스에 회원등록을 하고 서비스를 이용하는 자를
            말합니다.
          </BodyTab>
        </BodyContent>

        <BodyContent title="제 3조 약관의 게시와 개정">
          1. 회사는 본 약관의 내용을 회원이 쉽게 알 수 있도록 서비스 내의 화면
          또는 링크로 연결된 화면에 게시합니다.
          <br />
          2. 회사는 ‘약관의 규제에 관한 법률’ 등에 따라 본 약관을 개정할 수
          있습니다.
        </BodyContent>

        <BodyContent title="제 4조 약관 외 준칙">
          1. 회사는 개별 서비스 별로 별도의 이용약관 및 정책(이하 “개별 약관
          등”)을 둘 수 있으며, 해당 내용이 본 약관과 상충할 경우에는 개별 약관
          등이 우선하여 적용됩니다.
        </BodyContent>

        <BodyContent title="제 5조 이용 신청의 승낙과 제한">
          1. 회원 가입 신청자는 이 약관에 동의한 후, 회사가 정한 절차에 따라
          회원 가입을 신청할 수 있습니다.
        </BodyContent>

        <BodyContent title="제 6조 회원정보의 제공 및 변경">
          1. 회원은 본인의 회원정보를 열람하고 수정할 수 있습니다.
          <br />
          2. 회원이 회원정보 변경을 요구할 경우, 회사는 본인 확인 후 이를 즉시
          정정해야 합니다.
        </BodyContent>

        <BodyContent title="제 7조 회원정보의 관리">
          1. 회원정보에 대한 관리책임은 전적으로 회원에게 있으며, 회원은 아이디
          및 비밀번호를 타인에게 양도, 대여할 수 없습니다.
        </BodyContent>

        <BodyContent title="제 8조 개인정보의 보호 및 제공">
          1. 회사는 관계 법령이 정하는 바에 따라 회원정보를 보호하기 위해
          노력합니다.
        </BodyContent>

        <BodyContent title="제 9조 정보의 제공 및 광고의 게재">
          1. 회사는 서비스 이용 중 필요하다고 인정되는 정보를 회원에게 제공할 수
          있습니다.
        </BodyContent>

        <BodyContent title="제 10조 회사의 의무">
          1. 회사는 관계 법령과 본 약관이 금지하거나 미풍양속에 반하는 행위를
          하지 않으며, 계속적이고 안정적으로 서비스를 제공하기 위하여 최선을
          다하여 노력합니다.
        </BodyContent>

        <BodyContent title="제 11조 회원의 의무">
          1. 회원은 본인의 단말기 및 계정에서 발생하는 일체의 행위 및 행위에
          따른 결과에 대해 책임을 집니다.
        </BodyContent>

        <BodyContent title="제 12조 서비스 제공 및 변경">
          1. 회사는 회원에게 일정 관리 서비스를 포함한 서비스를 제공합니다.
        </BodyContent>

        <BodyContent title="제 13조 권리의 귀속">
          1. 서비스에 대한 저작권 및 지적재산권 등 모든 권리는 회사에
          귀속됩니다.
        </BodyContent>

        <BodyContent title="제 14조 서비스 이용 해지">
          1. 회원은 언제든지 서비스 이용 해지 신청(서비스 탈퇴)을 할 수
          있습니다.
        </BodyContent>

        <BodyContent title="제 15조 이용의 제한">
          1. 회사는 회원이 이 약관의 의무를 위반하거나 서비스의 정상적인 운영을
          방해한 경우, 서비스 이용을 제한할 수 있습니다.
        </BodyContent>

        <BodyContent title="제 16조 손해배상">
          1. 회사는 서비스 이용으로 발생할 수 있는 회원의 피해에 대해 책임을
          부담하지 않습니다.
        </BodyContent>

        <BodyContent title="제 17조 면책">
          1. 회사는 정보통신망의 장애, 천재지변 등의 사유로 인해 서비스를 제공할
          수 없는 경우 책임을 면제받습니다.
        </BodyContent>

        <BodyContent title="제 18조 준거법 및 재판관할">
          1. 본 약관과 관련된 사항에 대해서는 대한민국 법률을 준거법으로 합니다.
        </BodyContent>

        <p className="text-center text-gray-600 mt-4">
          부칙: 이 약관은 2025년 2월 3일부터 시행됩니다.
        </p>
      </div>
    </div>
  );
}

export default UsePoliciesPage;
