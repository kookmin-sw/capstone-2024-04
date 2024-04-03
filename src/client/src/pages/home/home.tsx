import { useEffect, useRef, useState } from "react";
import logoWhite from "../../assets/images/LogoWhite.svg";
import infoCircle from "../../assets/icons/InfoCircle.svg";

const HomePage = () => {
  const mainDivRef = useRef<HTMLDivElement>(null);
  const [divHeight, setDivHeight] = useState(0);

  useEffect(() => {
    const height = mainDivRef.current?.clientHeight || 0;
    setDivHeight(height);
  }, []);

  return (
    <div className="flex">
      {/* 좌측 영역 */}
      <div className="flex flex-col h-screen">
        <div ref={mainDivRef} className="bg-main pt-[55px] pb-[66px] px-[60px]">
          <img className="h-5" src={logoWhite} />
          <div className="flex flex-col w-[180px] mt-12 items-center">
            <img className="w-20 h-20 rounded-full bg-white" src="" />
            <p className="font-medium text-base text-white pt-5 pb-2">
              (주)KM컴퍼니
            </p>
            <p className="text-white">kmofficial@gmail.com</p>
          </div>
        </div>
        <div className="flex-grow bg-[#F0F2F5]"></div>
      </div>
      {/* 스크린 영역 */}
      <div
        className="flex flex-col flex-grow pr-[60px]"
        style={{
          background: `linear-gradient(to bottom, #4200ff ${divHeight}px, #f0f2f5 ${divHeight}px)`,
        }}
      >
        <div className="flex justify-between items-end">
          <div className="flex gap-9 mt-[55px]">
            <p className="whitespace-nowrap text-white text-lg font-medium">
              타이틀
            </p>
            <p className="whitespace-nowrap text-white_sub">
              설명이 들어가는 곳입니다.
            </p>
          </div>
          <img className="w-4 h-4 cursor-pointer" src={infoCircle} />
        </div>
        <div className="h-full rounded-[10px] py-[50px] px-[60px] mt-6 mb-[60px] bg-white">
          {/* 스크린 내부 */}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
