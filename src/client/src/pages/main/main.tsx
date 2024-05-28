import { Carousel } from "antd";
import SignInPage from "./sign_in";
import SignUpPage from "./sign_up";
import { Fragment, useState } from "react";
import FindPasswordPage from "./find_password";
import logoWhite from "../../assets/images/LogoWhite.svg";
import landing1 from "../../assets/images/1.svg";
import landing2 from "../../assets/images/2.svg";
import landing3 from "../../assets/images/3.svg";
import landing4 from "../../assets/images/4.svg";

const carouselArray = [
  {
    title: "지하철 광고 효과 분석은, DRM",
    text: "이제 오프라인 광고도 광고 효과를 분석할 수 있어요.\n지하철 2호선 열차 내부 광고에 특화된 DRM을 통해\n광고 성과를 한눈에 확인하세요!",
    imgSrc: landing1,
  },
  {
    title: "쉽고 빠른 광고 등록",
    text: "광고, 집행일자, 디스플레이 유형을 선택하면 끝!\n지금 바로 시작해 보세요.",
    imgSrc: landing2,
  },
  {
    title: "광고 효과분석 대시보드",
    text: "집행한 광고의 효과를 날짜별로 파악할 수 있습니다.\n대시보드를 통해 비즈니스 전략을 최적화하세요.",
    imgSrc: landing3,
  },
  {
    title: "광고 타겟층 분석 인사이트",
    text: "광고 타겟팅의 결과를 분석해볼까요?\n내가 원하는 타겟에 대한 인사이트를 확인할 수 있어요.",
    imgSrc: landing4,
  },
];

type CarouselContentType = {
  key: number;
  title: string;
  text: string;
  imgSrc: string;
};

const CarouselContent = ({ title, text, imgSrc }: CarouselContentType) => {
  return (
    <div className="flex flex-col items-center jusitfy-center mb-6">
      <div>
        <img
          className={`${imgSrc === landing1 ? "mb-[100px]" : ""}`}
          src={imgSrc}
        />
      </div>
      <h1 className="text-center font-bold text-3xl text-white mb-7">
        {title}
      </h1>
      <p className="text-center text-xl text-white mb-5">
        {text.split("\n").map((line, index) => (
          <Fragment key={index}>
            {line}
            <br />
          </Fragment>
        ))}
      </p>
    </div>
  );
};

const MainPage = () => {
  /**
   * 0: SignInScreen
   * 1: SignUpScreen
   * 2: FindPasswordScreen
   */
  const [screenIndex, setScreenIndex] = useState(0);

  const screenArray = [
    <SignInPage
      goToSignUp={() => setScreenIndex(1)}
      goToFindPassword={() => setScreenIndex(2)}
    />,
    <SignUpPage goToSignIn={() => setScreenIndex(0)} />,
    <FindPasswordPage goToSignIn={() => setScreenIndex(0)} />,
  ];

  return (
    <div className="flex flex-col-reverse lg:flex-row w-screen">
      {/* 좌측 화면 - Carousel */}
      <div className="hidden lg:flex w-full lg:w-1/2 bg-main items-center">
        <div className="w-full">
          <img className="fixed top-[55px] left-[55px] z-50" src={logoWhite} />
          <Carousel autoplay={true}>
            {carouselArray.map((content, index) => (
              <CarouselContent
                key={index}
                title={content.title}
                text={content.text}
                imgSrc={content.imgSrc}
              />
            ))}
          </Carousel>
        </div>
      </div>
      {/* 우측 화면 */}
      <div className="flex items-center justify-center w-full h-screen lg:w-1/2 bg-white">
        {screenArray[screenIndex]}
      </div>
    </div>
  );
};

export default MainPage;
