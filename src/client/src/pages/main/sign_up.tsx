import { useRef, useState } from "react";

interface SignUpPageProps {
  goToSignIn: any;
}

const SignUpPage = ({ goToSignIn }: SignUpPageProps) => {
  const idRef = useRef<HTMLInputElement>(null);
  const codeRef = useRef<HTMLInputElement>(null);
  const pwRef = useRef<HTMLInputElement>(null);
  const pwCheckRef = useRef<HTMLInputElement>(null);

  const [idErr, setIdErr] = useState("");
  const [pwErr, setPwErr] = useState("");
  const [codeErr, setCodeErr] = useState("");
  const [pwCheckErr, setPwCheckErr] = useState("");

  return (
    <div className="flex flex-col w-[360px]">
      <div className="flex justify-between mb-4 items-center">
        <h1 className="text-black text-xl">회원가입</h1>
        <a className="text-main" onClick={goToSignIn}>
          홈으로
        </a>
      </div>
      <p className="text-placeholder">아이디</p>
      <div className="flex gap-2">
        <input
          type="text"
          className="w-full p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
          placeholder="example@google.com"
          ref={idRef}
          onFocus={() => setIdErr("")}
          onBlur={(e: any) => {
            if (e.target.value === "") setIdErr("이메일을 입력해주세요.");
          }}
        />
        <button className="p-4 whitespace-nowrap text-main ring-1 ring-main rounded-md">
          인증하기
        </button>
      </div>
      <p className="h-7 text-error text-sm">{idErr}</p>

      <p className="text-placeholder">인증번호</p>
      <input
        type="number"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={codeRef}
        onFocus={() => setCodeErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setCodeErr("인증번호를 입력해주세요.");
        }}
      />
      <p className="h-7 text-error text-sm">{codeErr}</p>

      <p className="text-placeholder">비밀번호</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={pwRef}
        onFocus={() => setPwErr("")}
        onBlur={(e: any) => {
          if (e.target.value === "") setPwErr("비밀번호를 입력해주세요.");
        }}
      />
      <p className="h-7 text-error text-sm">{pwErr}</p>

      <p className="text-placeholder">비밀번호 확인</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={pwCheckRef}
        onChange={() => {
          if (pwCheckRef.current?.value !== pwRef.current?.value) {
            setPwCheckErr("비밀번호가 일치하지 않습니다.");
          } else {
            setPwCheckErr("");
          }
        }}
      />
      <p className="h-7 text-error text-sm">{pwCheckErr}</p>

      <button className="bg-main text-white p-4 rounded-md my-5">
        회원가입
      </button>
    </div>
  );
};

export default SignUpPage;
