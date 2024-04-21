import { FormEvent, useRef, useState } from "react";
import logo from "../../assets/images/logo.svg";
import { signin } from "../../api/auth";
import { useNavigate } from "react-router-dom";
import Cookies from "universal-cookie";
import { SignInPageProps } from "../../interfaces/interface";

enum ErrorText {
  incorrect = "아이디 또는 비밀번호를 잘못 입력했습니다.",
  notEnterId = "아이디를 입력해주세요.",
  notEnterPw = "비밀번호를 입력해주세요.",
}

const SignInPage = ({ goToSignUp, goToFindPassword }: SignInPageProps) => {
  const [errorText, setErrorText] = useState("");
  const [autoLogin, setAutoLogin] = useState(false);
  const idRef = useRef<HTMLInputElement>(null);
  const pwRef = useRef<HTMLInputElement>(null);
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

    if (result.status === 200) {
      const cookies = new Cookies();
      const tokenData = result.data.data;

      const refreshToken = tokenData.refreshToken;
      const accessToken = tokenData.accessToken;
      const expirationTime = tokenData.refreshTokenExpirationTime;

      // 쿠키에 토큰 저장
      cookies.set("accessToken", accessToken);
      cookies.set("refreshToken", refreshToken, { maxAge: expirationTime });

      // 자동 로그인 체크박스 체킹 여부에 따라 쿠키 설정
      cookies.set("autoLogin", autoLogin);

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
      <h1 className="text-black text-xl mb-4">로그인</h1>
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
      <p className="h-7 text-red text-sm">{errorText}</p>
      <div className="flex justify-between">
        <div className="flex gap-2">
          <input
            className="w-5 bg-main border-gray-300 rounded"
            type="checkbox"
            onChange={(e) => {
              setAutoLogin(e.target.checked);
            }}
            value="autoLogin"
          />
          <label className="text-black text-lg">자동 로그인</label>
        </div>
        <p className="text-main cursor-pointer" onClick={goToFindPassword}>
          비밀번호 찾기
        </p>
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
