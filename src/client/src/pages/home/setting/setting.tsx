import { Subtitle1, Subtitle2 } from "../../../components/text";
import { useState } from "react";

const SettingScreen = () => {
  const [isShowChangePassword, setIsShowChangePassword] = useState(false);
  const updateProfile = () => {};

  return !isShowChangePassword ? (
    <div className="flex flex-col h-full justify-between px-[30px] min-w-[920px] overflow-y-scroll">
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
        <button
          className="p-4 bg-gray1 rounded-[3px]"
          onClick={() => setIsShowChangePassword(true)}
        >
          비밀번호 변경
        </button>
      </div>
      <p className="cursor-pointer underline text-red pt-14">계정 탈퇴</p>
    </div>
  ) : (
    <div className="flex flex-col h-full justify-between px-[30px] min-w-[920px]">
      <div className="flex flex-col items-start gap-5">
        <div className="flex flex-col gap-4">
          <Subtitle1 text="현재 비밀번호" color="placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
          />
        </div>
        <div className="flex flex-col gap-4">
          <Subtitle1 text="새로운 비밀번호" color="placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
          />
        </div>
        <div className="flex flex-col gap-4">
          <Subtitle1 text="새로운 비밀번호 확인" color="placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
          />
        </div>
      </div>
      <div className="flex gap-4 pt-4">
        <button
          className="p-4 bg-main text-white rounded-[3px]"
          onClick={() => {
            window.alert("비밀번호가 변경되었습니다.");
            setIsShowChangePassword(false);
          }}
        >
          변경하기
        </button>
        <button
          className="p-4 bg-gray1 rounded-[3px]"
          onClick={() => setIsShowChangePassword(false)}
        >
          뒤로가기
        </button>
      </div>
    </div>
  );
};

export default SettingScreen;
