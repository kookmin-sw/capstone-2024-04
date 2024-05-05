import { Carousel } from "antd";
import carousel_1 from "../../assets/images/carousel_1.svg";
import SignInPage from "./sign_in";
import SignUpPage from "./sign_up";
import { Fragment, useState } from "react";
import FindPasswordPage from "./find_password";
import logoWhite from "../../assets/images/LogoWhite.svg";

const carouselArray = [
  {
    title: "빠르고 쉽게 성장하는 비즈니스",
    text: "DRM과 함께 오프라인 광고를 분석해요\n광고 효과를 한눈에 확인가능해요",
    imgSrc: carousel_1,
  },
  {
    title: "빠르고 쉽게 성장하는 비즈니스",
    text: "DRM과 함께 오프라인 광고를 분석해요\n광고 효과를 한눈에 확인가능해요",
    imgSrc: carousel_1,
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
        <img src={imgSrc} />
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
