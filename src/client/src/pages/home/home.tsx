import { useEffect, useRef, useState } from "react";
import logoWhite from "../../assets/images/LogoWhite.svg";
import infoCircle from "../../assets/icons/InfoCircle.svg";
import clipboardTextClockBlacksub from "../../assets/icons/clipboard-text-clock-blacksub.svg";
import clipboardTextClockWhitesub from "../../assets/icons/clipboard-text-clock-whitesub.svg";
import chartTimelineBlacksub from "../../assets/icons/chart-timeline-blacksub.svg";
import chartTimelineWhitesub from "../../assets/icons/chart-timeline-whitesub.svg";
import cogBlacksub from "../../assets/icons/cog-blacksub.svg";
import cogWhitesub from "../../assets/icons/cog-whitesub.svg";
import fileCompareBlacksub from "../../assets/icons/file-compare-blacksub.svg";
import fileCompareWhitesub from "../../assets/icons/file-compare-whitesub.svg";
import listBoxBlacksub from "../../assets/icons/list-box-blacksub.svg";
import listBoxWhitesub from "../../assets/icons/list-box-whitesub.svg";
import uploadBlacksub from "../../assets/icons/upload-blacksub.svg";
import uploadWhitesub from "../../assets/icons/upload-whitesub.svg";
import accountBlacksub from "../../assets/icons/account-blacksub.svg";
import accountWhitesub from "../../assets/icons/account-whitesub.svg";
import viewDashboardBlacksub from "../../assets/icons/view-dashboard-blacksub.svg";
import viewDashboardWhitesub from "../../assets/icons/view-dashboard-whitesub.svg";
import MenuButton from "../../components/menu_button";

const HomePage = () => {
  const mainDivRef = useRef<HTMLDivElement>(null);
  const [divHeight, setDivHeight] = useState(0);
  const [currMenuIdx, setCurrMenuIdx] = useState(0);

  const menuButtons = [
    {
      title: "대시보드",
      iconWhiteSrc: viewDashboardWhitesub,
      iconBlackSrc: viewDashboardBlacksub,
    },
    {
      title: "요약",
      iconWhiteSrc: chartTimelineWhitesub,
      iconBlackSrc: chartTimelineBlacksub,
    },
    {
      title: "비교",
      iconWhiteSrc: fileCompareWhitesub,
      iconBlackSrc: fileCompareBlacksub,
    },
    {
      title: "히스토리",
      iconWhiteSrc: clipboardTextClockWhitesub,
      iconBlackSrc: clipboardTextClockBlacksub,
    },
    {
      title: "광고등록",
      iconWhiteSrc: uploadWhitesub,
      iconBlackSrc: uploadBlacksub,
    },
    {
      title: "광고목록",
      iconWhiteSrc: listBoxWhitesub,
      iconBlackSrc: listBoxBlacksub,
    },
    {
      title: "내 계정",
      iconWhiteSrc: accountWhitesub,
      iconBlackSrc: accountBlacksub,
    },
    {
      title: "설정",
      iconWhiteSrc: cogWhitesub,
      iconBlackSrc: cogBlacksub,
    },
  ];

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
        <div className="grid grid-cols-2 gap-y-3 gap-x-4 flex-grow bg-[#F0F2F5] p-[60px]">
          {menuButtons.map((button, index) => (
            <MenuButton
              key={`menu-button-${index}`}
              title={button.title}
              iconBlackSrc={button.iconBlackSrc}
              iconWhiteSrc={button.iconWhiteSrc}
              isActive={index === currMenuIdx}
              onClick={() => setCurrMenuIdx(index)}
            />
          ))}
        </div>
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
        <div className="h-full rounded-[10px] py-[50px] px-[30px] mt-6 mb-[60px] bg-white">
          {/* 스크린 내부 */}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
