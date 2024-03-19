import { Button, Carousel, Checkbox, ConfigProvider, Input } from "antd";
import { FormEvent, useRef, useState } from "react";
import logo from "../assets/images/logo.svg";
import carousel_1 from "../assets/images/carousel_1.svg";

enum ErrorText {
  incorrect = "아이디 또는 비밀번호를 잘못 입력했습니다.",
  notEnterId = "아이디를 입력해주세요.",
  notEnterPw = "비밀번호를 입력해주세요.",
}

const MainPage = () => {
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
    <div className="flex flex-col-reverse lg:flex-row w-screen lg:h-screen">
      {/* 좌측 화면 - Carousel */}
      <div className="hidden lg:flex w-full lg:w-1/2 bg-main items-center">
        <div className="w-full">
          <Carousel autoplay={true}>
            <div className="flex flex-col items-center jusitfy-center">
              <div>
                <img src={carousel_1} />
              </div>
              <h1 className="text-center">빠르고 쉽게 성장하는 비즈니스</h1>
              <p className="text-center">
                TOIFTALAF와 함께 오프라인 광고를 분석해요
                <br />
                광고 효과를 한 눈에 확인 가능해요
              </p>
            </div>
            <div className="flex flex-col items-center jusitfy-center">
              <div>
                <img src={carousel_1} />
              </div>
              <h1 className="text-center">빠르고 쉽게 성장하는 비즈니스</h1>
              <p className="text-center">
                TOIFTALAF와 함께 오프라인 광고를 분석해요
                <br />
                광고 효과를 한 눈에 확인 가능해요
              </p>
            </div>
          </Carousel>
        </div>
      </div>
      {/* 우측 화면 */}
      <div className="flex items-center justify-center w-full h-screen lg:w-1/2 bg-white">
        <div className="flex flex-col w-[360px]">
          <div className="flex gap-2 mb-4">
            <img className="w-10 mb-4" src={logo} />
            <h1 className="text-black font-black text-4xl">TOIFTALAE</h1>
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
            <a className="text-main" href="#">
              회원가입
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
