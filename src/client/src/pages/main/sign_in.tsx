import { FormEvent, useRef, useState } from "react";
import logo from "../../assets/images/logo.svg";
import { signin } from "../../api/auth";
import { useNavigate } from "react-router-dom";

enum ErrorText {
  incorrect = "아이디 또는 비밀번호를 잘못 입력했습니다.",
  notEnterId = "아이디를 입력해주세요.",
  notEnterPw = "비밀번호를 입력해주세요.",
}

interface SignInPageProps {
  goToSignUp: any;
}

const SignInPage = ({ goToSignUp }: SignInPageProps) => {
  const [errorText, setErrorText] = useState("");
  const idRef = useRef<HTMLInputElement>(null);
  const pwRef = useRef<HTMLInputElement>(null);
  const checkBoxRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();

  const login = async (e: FormEvent) => {
    e.preventDefault();
    let id = idRef.current?.value || "";
    let pw = pwRef.current?.value || "";
    // 아이디를 미입력한 경우
    if (id === "") {
      setErrorText(ErrorText.notEnterId);
      return false;
    }
    // 비밀번호를 미입력한 경우
    if (pw === "") {
      setErrorText(ErrorText.notEnterPw);
      return false;
    }

    const body = { email: id, password: pw };

    const result = await signin(body);
    console.log(result);

    if (result.status === 200) {
      // 로그인 성공
      // token 값 저장 방식이 확정되면 추후 관련 로직 추가 예정
      navigate("/home");
      return true;
    }

    if (result.status === 400) {
      if (result.data.divisionCode === "G014") {
        // 이메일 정보가 없음 or 잘못된 비밀번호 입력
        setErrorText("아이디 또는 비밀번호를 잘못 입력하였습니다.");
      }
      return false;
    }

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
