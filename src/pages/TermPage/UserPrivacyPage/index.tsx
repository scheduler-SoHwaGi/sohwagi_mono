import HeadTitle from '../Component/HeadTitle';
import PolicyContainer from '../Component/PolicyContainer';
import BodyContent from '../Component/BodyContent';
import BodyTab from '../Component/BodyTab';

function UserPrivacyPage() {
  return (
    <div className="flex flex-col h-full bg-white">
      <HeadTitle headTitle="개인정보 처리방침" />
      <div className="flex flex-col overflow-y-scroll p-4">
        <PolicyContainer>
          <BodyContent title="">
            (주)소화기(이하 “회사”)는 통신비밀보호법, 전기통신사업법, 정보통신망
            이용촉진 및 정보보호 등에 관한 법률 등 정보통신서비스제공자가
            준수하여야 할 관련 법령상의 개인정보보호 규정을 준수하며, 관련
            법령에 의거한 개인정보처리방침을 정하여 이용자 권익 보호에 최선을
            다하고 있습니다.
          </BodyContent>

          <BodyContent title="1. 개인정보의 수집 및 이용목적">
            회사는 다음 목적을 위하여 개인정보를 수집하고 있으며 다음 목적
            이외의 용도로는 수집한 개인정보를 이용하지 않으며, 이용 목적이
            변경되는 경우에는 별도의 동의를 받는 등 필요한 조치를 이행할
            예정입니다.
          </BodyContent>

          <BodyTab>
            1) 서비스 이용에 따른 가입의사 확인, 회원 식별, 원활한 의사소통
            경로의 확보, 새로운 정보의 소개 및 고지사항 전달, 서비스 방문 및
            이용기록 분석 등을 위해 이용합니다.
            <br />
            <br />
            2) 회원이 등록한 정보에 기반한 회비 관리 서비스를 위해 이용합니다.
          </BodyTab>

          <BodyContent title="2. 개인정보 항목 및 수집방법">
            회사는 법령에 따른 개인정보 보유 이용기간 또는 정보 주체로부터
            개인정보를 수집 시에 동의 받은 개인정보 보유 이용기간 내에서
            개인정보를 처리 보유합니다.
          </BodyContent>

          <BodyTab>
            1) 수집하는 개인정보의 항목
            <br />
            가. 소셜 회원가입
            <BodyTab>1. 애플 - 필수항목 : 애플계정(이메일), 이름</BodyTab>
            나. 서비스 이용과정이나 사업처리 과정에서 자동으로 수집되는 항목
            <BodyTab>
              IP Address, 쿠키, 방문 일시, 서비스 이용 기록, 불량 이용 기록,
              광고 ID, 접속 환경
            </BodyTab>
          </BodyTab>

          <BodyContent title="3. 개인정보의 보유 및 이용기간">
            회사는 이용자의 개인정보를 고지 및 동의 받은 사항에 따라 수집, 이용
            목적이 달성되기 전 또는 이용자의 탈퇴 요청이 있기 전까지 해당 정보를
            보유합니다.
          </BodyContent>

          <BodyContent title="4. 개인정보의 제공 및 위탁">
            회사는 서비스의 원활한 제공을 위해 필요한 때에는 개인정보의 처리를
            일부 위탁하고 있으며, 수탁 받은 업체가 관계 법령을 준수하도록 관리,
            감독하고 있습니다.
          </BodyContent>

          <table className="border-collapse border border-gray-300 w-full text-left mt-4">
            <thead className="bg-gray-100">
              <tr>
                <th className="border border-gray-300 p-2">수탁자</th>
                <th className="border border-gray-300 p-2">위탁 업무 내용</th>
                <th className="border border-gray-300 p-2">
                  개인정보의 보유 및 이용기간
                </th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td className="border border-gray-300 p-2">
                  Amazon Web Service
                </td>
                <td className="border border-gray-300 p-2">
                  회원 정보 보관 및 운영 서버 관리
                </td>
                <td className="border border-gray-300 p-2">
                  회원 탈퇴 시 또는 위탁 계약 종료 시까지
                </td>
              </tr>
            </tbody>
          </table>

          <BodyContent title="5. 개인정보 파기절차 및 파기방법">
            이용자의 개인정보는 원칙적으로 개인정보의 수집 및 이용목적이
            달성되면 지체없이 파기합니다.
          </BodyContent>

          <BodyContent title="6. 이용자의 권리">
            1) 회원은 언제든지 자신의 개인정보를 조회하거나 수정할 수 있습니다.
            <br />
            2) 회원은 언제든지 “탈퇴하기” 기능을 통해 개인정보의 수집 및 이용
            동의를 철회할 수 있습니다.
          </BodyContent>

          <BodyContent title="7. 개인정보 자동 수집 장치의 설치, 운영 및 거부에 관한 사항">
            회원은 언제든지 자신의 개인정보를 조회하거나 수정할 수 있습니다.
          </BodyContent>

          <BodyContent title="8. 개인정보의 기술적, 관리적 보호대책">
            1. 개인정보 암호화 : 이용자의 개인정보는 비밀번호에 의해 보호되며,
            파일 및 각종 데이터는 암호화됩니다.
            <br />
            2. 개인정보에 대한 접근 제한 : 개인정보를 처리하는
            데이터베이스시스템에 대한 접근권한을 부여, 변경, 말소합니다.
          </BodyContent>

          <BodyContent title="9. 링크">
            사이트는 다양한 배너와 링크를 포함하고 있으며, 이는 광고주와의
            계약관계에 의해 제공됩니다.
          </BodyContent>

          <BodyContent title="10. 이용자의 권리와 그 행사방법">
            이용자 및 법정대리인은 언제든지 등록된 정보를 열람, 수정, 삭제할 수
            있습니다.
          </BodyContent>

          <BodyContent title="11. 개인정보에 관한 민원서비스">
            개인정보 보호 책임자: 구나연 (team.sohawgi@gmail.com)
          </BodyContent>

          <BodyContent title="12. 개인정보 처리 방침 변경">
            본 개인정보 처리방침은 2025년 2월 3일부터 적용됩니다.
          </BodyContent>
        </PolicyContainer>
      </div>
    </div>
  );
}

export default UserPrivacyPage;
