import { useEffect, useState } from "react";
import { Headline1 } from "../../components/text";
import { FindPasswordPageProps } from "../../interfaces/interface";

const FindPasswordPage = ({ goToSignIn }: FindPasswordPageProps) => {
  const [email, setEmail] = useState("");
  const [emailErr, setEmailErr] = useState("");
  const [isActive, setIsActive] = useState(false);

  useEffect(() => {
    setIsActive(email !== "" && emailErr === "");
  }, [email, emailErr]);

  return (
    <div className="flex flex-col w-[360px]">
      <div className="flex justify-between mb-4 items-center">
        <h1 className="text-black text-xl">비밀번호 찾기</h1>
        <a className="text-main cursor-pointer" onClick={goToSignIn}>
          홈으로
        </a>
      </div>
      <Headline1 text="회원가입시 등록한 메일을 입력하세요" color="black" />

      <p className="mt-4 text-placeholder">아이디</p>
      <input
        type="text"
        className="w-full p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        placeholder="example@google.com"
        onChange={(e) => setEmail(e.target.value)}
        onFocus={() => setEmailErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setEmailErr("이메일을 입력해주세요.");
        }}
      />
      <p className="h-7 text-red text-sm">{emailErr}</p>
      <button
        className={`${
          isActive ? "bg-main" : "bg-black_sub"
        } text-white p-4 rounded-md my-5`}
        disabled={!isActive}
        onClick={() => {
          window.alert("인증 메일이 전송되었습니다.");
        }}
      >
        인증메일 받기
      </button>
    </div>
  );
};

export default FindPasswordPage;
