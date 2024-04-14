import { FormEvent, useEffect, useState } from "react";
import { signup } from "../../api/auth";

interface SignUpPageProps {
  goToSignIn: any;
}

const SignUpPage = ({ goToSignIn }: SignUpPageProps) => {
  const [email, setEmail] = useState("");
  const [company, setCompany] = useState("");
  const [password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("");

  const [idErr, setIdErr] = useState("");
  const [incErr, setIncErr] = useState("");
  const [pwErr, setPwErr] = useState("");
  const [pwCheckErr, setPwCheckErr] = useState("");
  const [isActive, setIsActive] = useState(false);

  const createAccount = async (e: FormEvent) => {
    e.preventDefault();

    const body = {
      email,
      password,
      company,
    };

    const result = await signup(body);
    console.log(result);

    if (result.status === 400) {
      if (result.data.divisionCode === "G014") {
        setIdErr("이미 사용 중인 이메일입니다.");
      }
      return false;
    }

    if (result.status === 201) {
      // Toast 메세지 출력 <- 계정 생성에 대한

      goToSignIn(); // 로그인 화면으로 이동
    }
  };

  useEffect(() => {
    setIsActive(
      email !== "" &&
        company !== "" &&
        password !== "" &&
        pwErr === "" &&
        password === passwordCheck
    );
  }, [email, company, password, passwordCheck]);

  useEffect(() => {
    password === passwordCheck
      ? setPwCheckErr("")
      : setPwCheckErr("비밀번호가 일치하지 않습니다.");
  }, [password, passwordCheck]);

  return (
    <div className="flex flex-col w-[360px]">
      <div className="flex justify-between mb-4 items-center">
        <h1 className="text-black text-xl">회원가입</h1>
        <a className="text-main cursor-pointer" onClick={goToSignIn}>
          홈으로
        </a>
      </div>
      <p className="text-placeholder">아이디</p>
      <input
        type="text"
        className="w-full p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        placeholder="example@google.com"
        onChange={(e) => setEmail(e.target.value)}
        onFocus={() => setIdErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setIdErr("이메일을 입력해주세요.");
        }}
      />
      <p className="h-7 text-red text-sm">{idErr}</p>

      <p className="text-placeholder">회사명</p>
      <input
        type="text"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        onChange={(e) => setCompany(e.target.value)}
        onFocus={() => setIncErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setIncErr("회사명을 입력해주세요.");
        }}
      />
      <p className="h-7 text-red text-sm">{incErr}</p>

      <p className="text-placeholder">비밀번호</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        onChange={(e) => {
          setPassword(e.target.value);
          if (
            // 영문자 조합 8글자 이상, 특수문자 제외
            !/^(?=.*[a-zA-Z])(?=.*[0-9])(?!.*[^a-zA-Z0-9]).{8,}$/.test(password)
          ) {
            // 주어진 정규식을 만족하지 못하는 경우
            setPwErr("영문자, 숫자를 조합하여 8글자 이상 입력해주세요.");
          } else {
            // 주어진 정규식을 만족하는 경우
            setPwErr("");
          }
        }}
        onFocus={() => setPwErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setPwErr("비밀번호를 입력해주세요.");
        }}
      />
      <p className="h-7 text-red text-sm">{pwErr}</p>

      <p className="text-placeholder">비밀번호 확인</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        onChange={(e) => setPasswordCheck(e.target.value)}
      />
      <p className="h-7 text-red text-sm">{pwCheckErr}</p>

      <button
        className={`${
          isActive ? "bg-main" : "bg-black_sub"
        } text-white p-4 rounded-md my-5`}
        disabled={!isActive}
        onClick={createAccount}
      >
        회원가입
      </button>
    </div>
  );
};

export default SignUpPage;
