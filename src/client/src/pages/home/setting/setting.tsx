import Cookies from "universal-cookie";
import { Subtitle1, Subtitle2 } from "../../../components/text";
import { useNavigate } from "react-router-dom";

const SettingScreen = () => {
  const updateProfile = () => {};
  const navigate = useNavigate();
  const logout = () => {
    const cookies = new Cookies();
    // 인증 관련 토큰 제거
    cookies.remove("accessToken");
    cookies.remove("refreshToken");
    // 자동 로그인 설정 제거
    cookies.remove("autoLogin");

    navigate("/");
  };

  return (
    <div className="flex flex-col h-full justify-between px-[30px] min-w-[920px]">
      <div className="flex flex-col items-start gap-5">
        <Subtitle2 text="프로필 사진" color="black" />
        <img className="w-[93px] h-[93px] rounded-full border-[1px] border-gray2" />
        <button className="p-4 bg-gray1 rounded-[3px]">프로필 변경</button>
        <Subtitle1 text="아이디" color="placeholder" />
        <div className="flex items-center w-[320px] h-12 rounded-md border-[1px] border-gray2 px-6">
          qwerty_02@kookmin.ac.kr
        </div>
        <Subtitle1 text="회사명" color="placeholder" />
        <div className="flex items-center w-[320px] h-12 rounded-md border-[1px] border-gray2 px-6">
          (주)KMU컴퍼니
        </div>
        <Subtitle1 text="비밀번호" color="placeholder" />
        <button className="p-4 bg-gray1 rounded-[3px]">비밀번호 변경</button>
      </div>
      <div className="flex gap-4 pt-4">
        <p className="cursor-pointer">회원 탈퇴</p>
        <p className="cursor-pointer" onClick={logout}>
          로그아웃
        </p>
      </div>
    </div>
  );
};

export default SettingScreen;
