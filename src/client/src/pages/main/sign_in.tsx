import { FormEvent, useRef, useState } from "react";
import logo from "../../assets/images/logo.svg";

enum ErrorText {
  incorrect = "아이디 또는 비밀번호를 잘못 입력했습니다.",
  notEnterId = "아이디를 입력해주세요.",
  notEnterPw = "비밀번호를 입력해주세요.",
}

const SignInPage = ({ goToSignUp }) => {
  const [errorText, setErrorText] = useState("");
  const idRef = useRef<HTMLInputElement>(null);
  const pwRef = useRef<HTMLInputElement>(null);
  const checkBoxRef = useRef<HTMLInputElement>(null);

  const login = (e: FormEvent) => {
    e.preventDefault();
    // 아이디를 미입력한 경우
    if (idRef.current?.value === "") {
      setErrorText(ErrorText.notEnterId);
      return false;
    }
    // 비밀번호를 미입력한 경우
    if (pwRef.current?.value === "") {
      setErrorText(ErrorText.notEnterPw);
      return false;
    }
    // 로그인에 실패한 경우(가입되지 않은 아이디 입력 및 비밀번호 틀림)

    return true;
  };

  return (
    <div className="flex flex-col w-[360px]">
      <div className="flex gap-2 mb-4">
        <img className="w-10 mb-4" src={logo} />
        <h1 className="text-black font-black text-4xl">DO YOU READ ME</h1>
      </div>
      <p className="text-placeholder">아이디</p>
      <input
        type="text"
        onFocus={() => setErrorText("")}
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        placeholder="example@google.com"
        ref={idRef}
      />
      <p className="text-placeholder mt-4">비밀번호</p>
      <input
        type="password"
        onFocus={() => setErrorText("")}
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={pwRef}
      />
      {/* 에러 텍스트 */}
      <p className="h-7 text-error text-sm">{errorText}</p>
      <div className="flex justify-between">
        <div className="flex gap-2">
          <input
            className="w-5 bg-main border-gray-300 rounded"
            type="checkbox"
            onChange={(e) => {
              e.target.checked;
            }}
            ref={checkBoxRef}
          />
          <label className="text-black text-lg">자동 로그인</label>
        </div>
        <a className="text-main" href="#">
          비밀번호 찾기
        </a>
      </div>
      <button
        onClick={(e) => login(e)}
        className="bg-main text-white p-4 rounded-md my-5"
      >
        로그인
      </button>
      <div className="flex gap-8">
        <p className="text-placeholder">아직 계정이 없으신가요?</p>
        <a className="text-main" href="#" onClick={goToSignUp}>
          회원가입
        </a>
      </div>
    </div>
  );
};

export default SignInPage;
