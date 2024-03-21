import { useRef } from "react";

const SignUpPage = () => {
  const idRef = useRef<HTMLInputElement>(null);
  const codeRef = useRef<HTMLInputElement>(null);
  const pwRef = useRef<HTMLInputElement>(null);
  const pwCheckRef = useRef<HTMLInputElement>(null);

  return (
    <div className="flex flex-col w-[360px]">
      <div className="flex justify-between mb-4 items-center">
        <h1 className="text-black text-xl">회원가입</h1>
        <a className="text-main">홈으로</a>
      </div>
      <p className="text-placeholder">아이디</p>
      <div className="flex gap-2">
        <input
          type="text"
          className="w-full p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
          placeholder="example@google.com"
          ref={idRef}
        />
        <button className="p-4 whitespace-nowrap text-main ring-1 ring-main rounded-md">
          인증하기
        </button>
      </div>

      <p className="text-placeholder mt-4">인증번호</p>
      <input
        type="number"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={codeRef}
      />

      <p className="text-placeholder mt-4">비밀번호</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={pwRef}
      />

      <p className="text-placeholder mt-4">비밀번호 확인</p>
      <input
        type="password"
        className="p-4 rounded-md ring-1 ring-inset ring-[#d9d9d9] outline-none"
        ref={pwCheckRef}
      />

      <button
        onClick={() => {}}
        className="bg-main text-white p-4 rounded-md my-5"
      >
        회원가입
      </button>
    </div>
  );
};

export default SignUpPage;
