import { Button, Carousel, Checkbox, Input } from "antd";
import { useState } from "react";

const MainPage = () => {
  const [idError, setIdError] = useState(true);
  const [pwError, setPwError] = useState(false);

  return (
    <div className="flex w-screen h-screen">
      <div className="flex w-1/2 bg-main">
        <div className="bg-red p-4 w-full">
          <Carousel autoplay={true}>
            <div>
              <h3>1</h3>
            </div>
            <div>
              <h3>2</h3>
            </div>
            <div>
              <h3>3</h3>
            </div>
            <div>
              <h3>4</h3>
            </div>
          </Carousel>
        </div>
      </div>
      <div className="flex items-center justify-center w-1/2 bg-white">
        <div>
          <img />
          <h1>TOIFTALAE</h1>
          <p>아이디</p>
          <Input
            className="p-2"
            placeholder="example@google.com"
            status={idError ? "error" : ""}
          />
          <p>비밀번호</p>
          <Input className="p-2" status={pwError ? "error" : ""} />
          <div>
            <Checkbox
              onChange={(e) => {
                e.target.checked;
              }}
            >
              자동 로그인
            </Checkbox>
            <a>비밀번호 찾기</a>
          </div>
          <Button>로그인</Button>
          <div className="flex gap-2">
            <p>아직 계정이 없으신가요?</p>
            <a href="#">회원가입</a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;
