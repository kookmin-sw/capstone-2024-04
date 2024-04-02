import { useEffect, useRef, useState } from "react";
import logoWhite from "../../assets/images/LogoWhite.svg";

const HomePage = () => {
  const mainDivRef = useRef<HTMLDivElement>(null);
  const [divHeight, setDivHeignt] = useState(0);

  useEffect(() => {
    setDivHeignt(mainDivRef.current?.clientHeight || 0);
    console.log(divHeight);
  }, []);

  return (
    <div className="flex">
      {/* 좌측 영역 */}
      <div className="flex flex-col h-screen">
        <div ref={mainDivRef} className="bg-main pt-[55px] pb-[66px] px-[60px]">
          <img src={logoWhite} />
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
      <div className="flex-grow"></div>
      {/* 우측 여백 영역 */}
      <div className="flex flex-col w-[60px]">
        <div style={{ height: `${divHeight}px` }} className="bg-main" />
        <div className="flex-grow bg-[#F0F2F5]" />
      </div>
    </div>
  );
};

export default HomePage;
