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
import uploadBlacksub from "../../assets/icons/upload-blacksub.svg";
import uploadWhitesub from "../../assets/icons/upload-whitesub.svg";
import viewDashboardBlacksub from "../../assets/icons/view-dashboard-blacksub.svg";
import viewDashboardWhitesub from "../../assets/icons/view-dashboard-whitesub.svg";
import MenuButton from "../../components/menu_button";
import PostMediaScreen from "./post-media/post_media";
import { Body1, Headline1 } from "../../components/text";
import CompareMediaScreen from "./compare-media/compare_media";
import SettingScreen from "./setting/setting";
import DashBoard from "./dashboard/dashboard";
import { useNavigate } from "react-router-dom";
import Cookies from "universal-cookie";

const HomePage = () => {
  const mainDivRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const [divHeight, setDivHeight] = useState(0);
  const [currMenuIdx, setCurrMenuIdx] = useState(0);

  const logout = () => {
    const cookies = new Cookies();
    // 인증 관련 토큰 제거
    cookies.remove("accessToken");
    cookies.remove("refreshToken");
    // 자동 로그인 설정 제거
    cookies.remove("autoLogin");

    navigate("/");
  };

  const menuButtons = [
    {
      title: "대시보드",
      description: "해당 기간에 집행한 광고에 대한 통계를 확인할 수 있어요.",
      iconWhiteSrc: viewDashboardWhitesub,
      iconBlackSrc: viewDashboardBlacksub,
      component: <DashBoard />,
    },
    {
      title: "인사이트",
      description:
        "하나의 광고에 대해 전체 기간의 통계를 한눈에 확인할 수 있어요.",
      iconWhiteSrc: chartTimelineWhitesub,
      iconBlackSrc: chartTimelineBlacksub,
      component: <>인사이트</>,
    },
    {
      title: "위치정보비교",
      description:
        "각 위치별로 유동인구 정보를 비교할 수 있어요. 집행할 디스플레이 선택시 참고하면 좋아요.",
      iconWhiteSrc: fileCompareWhitesub,
      iconBlackSrc: fileCompareBlacksub,
      component: <CompareMediaScreen />,
    },
    {
      title: "광고 등록",
      description: "집행할 광고를 등록하세요.",
      iconWhiteSrc: uploadWhitesub,
      iconBlackSrc: uploadBlacksub,
      component: <PostMediaScreen />,
    },
    {
      title: "히스토리",
      description: "전체 광고 히스토리를 확인할 수 있어요.",
      iconWhiteSrc: clipboardTextClockWhitesub,
      iconBlackSrc: clipboardTextClockBlacksub,
      component: <>히스토리</>,
    },
    {
      title: "설정",
      description: "",
      iconWhiteSrc: cogWhitesub,
      iconBlackSrc: cogBlacksub,
      component: <SettingScreen />,
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
        <div ref={mainDivRef} className="bg-main pt-9 pb-[66px] px-[60px]">
          <img className="h-5" src={logoWhite} />
          <div className="flex flex-col w-[180px] mt-12 items-center">
            <img className="w-20 h-20 rounded-full bg-white" src="" />
            <p className="font-medium text-base text-white pt-5 pb-2">
              (주)KM컴퍼니
            </p>
            <p className="text-white">kmofficial@gmail.com</p>
          </div>
        </div>
        <div className="flex-grow bg-[#F0F2F5]">
          <div className="grid grid-cols-2 gap-y-3 gap-x-4 p-[60px]">
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
          <footer className="flex flex-col justify-center items-center gap-1">
            <p className="text-[#7e94b1] text-xs">
              @DOYOUREADME 2024 All rights reserved.
            </p>
            <div className="flex gap-4">
              <a className="text-[#7e94b1] text-xs">About us</a>
              <a
                href="https://github.com/kookmin-sw/capstone-2024-04"
                target="_blank"
                className="text-[#7e94b1] text-xs"
              >
                Github
              </a>
              <p
                onClick={logout}
                className="text-black_sub text-xs cursor-pointer font-medium"
              >
                LOGOUT
              </p>
            </div>
          </footer>
        </div>
      </div>
      {/* 스크린 영역 */}
      <div
        className="flex flex-col flex-grow pr-[60px] h-screen min-h-[710px]"
        style={{
          background: `linear-gradient(to bottom, #4200ff ${divHeight}px, #f0f2f5 ${divHeight}px)`,
        }}
      >
        <div className="flex justify-between items-end">
          <div className="flex gap-9 mt-[55px] items-end">
            <Headline1 text={menuButtons[currMenuIdx].title} color="white" />
            <Body1
              text={menuButtons[currMenuIdx].description}
              color="white_sub"
            />
          </div>
          <img className="w-4 h-4 cursor-pointer" src={infoCircle} />
        </div>
        <div className="h-full rounded-[10px] py-[50px] px-[30px] mt-6 mb-[60px] bg-white overflow-hidden">
          {/* 스크린 내부 */}
          {menuButtons[currMenuIdx].component}
        </div>
      </div>
    </div>
  );
};

export default HomePage;
