import { Subtitle1, Subtitle2 } from "../../../components/text";
import { useEffect, useRef, useState } from "react";
import { UserInfo } from "../../../interfaces/interface";
import { patchPassword, patchProfile, verifyPassword } from "../../../api/user";
import { toast } from "react-hot-toast";
import Cookies from "universal-cookie";

interface SettingScreenProps {
  userInfo: null | undefined | UserInfo;
  setUserInfo: React.Dispatch<
    React.SetStateAction<null | undefined | UserInfo>
  >;
}

const SettingScreen = ({ userInfo, setUserInfo }: SettingScreenProps) => {
  const [isShowChangePassword, setIsShowChangePassword] = useState(false);
  const [pwErr, setPwErr] = useState("");
  const [newPwErr, setNewPwErr] = useState("");
  const [newPwCheckErr, setNewPwCheckErr] = useState("");

  const [pw, setPw] = useState("");
  const [newPw, setNewPw] = useState("");
  const [newPwCheck, setNewPwCheck] = useState("");

  const [isActive, setIsActive] = useState(false);
  const selectProfile = useRef(null);

  useEffect(() => {
    if (newPw === newPwCheck) {
      setNewPwCheckErr("");
    } else {
      if (newPwCheck !== "") setNewPwCheckErr("비밀번호가 일치하지 않습니다.");
    }
  }, [newPw, newPwCheck]);

  useEffect(() => {
    setIsActive(
      pw !== "" &&
        newPw !== "" &&
        newPwCheck !== "" &&
        pwErr === "" &&
        newPwCheckErr === "" &&
        newPwErr === ""
    );
  }, [pw, newPw, newPwCheck, pwErr, newPwErr, newPwCheckErr]);

  const changeProfile = () => {
    selectProfile.current?.click();
  };

  const changePassword = async () => {
    const result = await patchPassword({ password: pw, updatePassword: newPw });
    if (result.data.status === 200 || result.data.status === 201) {
      setPw("");
      setNewPw("");
      setNewPwCheck("");
      toast.success("비밀번호 변경이 완료되었습니다.");
    } else {
      toast.error("비밀번호 변경에 실패하였습니다.");
    }
  };

  const checkPassword = async (password: string) => {
    try {
      await verifyPassword(password);
      setPwErr(""); // 비밀번호가 일치하는 경우
    } catch (err: any) {
      console.log(err.message); // 에러 메시지 출력
      setPwErr("비밀번호가 일치하지 않습니다.");
    }
  };

  const handleProfileChange = async (e: any) => {
    const file = e.target.files?.[0];
    const formData = new FormData();
    formData.append("file", file);
    console.log(file);
    if (file) {
      const result = await patchProfile(formData);
      console.log(result);

      if (result.data.status === 200 || result.data.status === 201) {
        const cookies = new Cookies();
        cookies.set("userInfo", result.data.data);
        setUserInfo(result.data.data);
        toast.success("프로필 변경이 완료되었습니다.");
      } else {
        toast.error("프로필 변경에 실패하였습니다.");
      }
    }
  };

  return !isShowChangePassword ? (
    <div className="flex flex-col h-full justify-between px-[30px] min-w-[920px] overflow-y-scroll">
      <div className="flex flex-col items-start gap-5">
        <Subtitle2 text="프로필 사진" color="text-black" />
        <img
          className="w-[93px] h-[93px] rounded-full border-[1px] border-gray2"
          src={userInfo?.profileImage}
        />
        <input
          type="file"
          accept="image/*"
          style={{ display: "none" }}
          ref={selectProfile}
          onChange={handleProfileChange}
        />
        <button onClick={changeProfile} className="p-4 bg-gray1 rounded-[3px]">
          프로필 변경
        </button>
        <Subtitle1 text="아이디" color="text-placeholder" />
        <div className="flex items-center w-[320px] h-12 rounded-md border-[1px] border-gray2 px-6">
          {userInfo?.email}
        </div>
        <Subtitle1 text="회사명" color="text-placeholder" />
        <div className="flex items-center w-[320px] h-12 rounded-md border-[1px] border-gray2 px-6">
          {userInfo?.company}
        </div>
        <Subtitle1 text="비밀번호" color="text-placeholder" />
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
      <div className="flex flex-col items-start">
        <div className="flex flex-col gap-4">
          <Subtitle1 text="현재 비밀번호" color="text-placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
            value={pw}
            onChange={(e) => setPw(e.target.value)}
            onFocus={() => setPwErr("")}
            onBlur={(e) => {
              e.target.value === ""
                ? setPwErr("비밀번호를 입력해주세요.")
                : checkPassword(e.target.value);
            }}
          />
        </div>
        <p className="h-7 text-red text-sm">{pwErr}</p>
        <div className="flex flex-col gap-4">
          <Subtitle1 text="새로운 비밀번호" color="text-placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
            value={newPw}
            onChange={(e) => {
              setNewPw(e.target.value);
              if (
                // 영문자 조합 8글자 이상, 특수문자 제외
                !/^(?=.*[a-zA-Z])(?=.*[0-9])(?!.*[^a-zA-Z0-9]).{8,}$/.test(
                  newPw
                )
              ) {
                // 주어진 정규식을 만족하지 못하는 경우
                setNewPwErr("영문자, 숫자를 조합하여 8글자 이상 입력해주세요.");
              } else {
                // 주어진 정규식을 만족하는 경우
                setNewPwErr("");
              }
            }}
            onFocus={() => setNewPwErr("")}
            onBlur={(e: any) => {
              if (e.target.value === "")
                setNewPwErr("새로운 비밀번호를 입력해주세요.");
            }}
          />
        </div>
        <p className="h-7 text-red text-sm">{newPwErr}</p>
        <div className="flex flex-col gap-4">
          <Subtitle1 text="새로운 비밀번호 확인" color="text-placeholder" />
          <input
            className="p-4 border-[1px] border-gray2 rounded-[3px] w-[360px]"
            type="password"
            value={newPwCheck}
            onChange={(e) => setNewPwCheck(e.target.value)}
          />
        </div>
        <p className="h-7 text-red text-sm">{newPwCheckErr}</p>
      </div>
      <div className="flex gap-4 pt-4">
        <button
          className={`p-4 ${
            isActive ? "bg-main" : "bg-black_sub"
          } text-white rounded-[3px]`}
          onClick={() => {
            changePassword();
            setIsShowChangePassword(false);
          }}
          disabled={!isActive}
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
