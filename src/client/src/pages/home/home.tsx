import { useEffect, useRef, useState } from "react";
import logoWhite from "../../assets/images/LogoWhite.svg";
import leftArrow from "../../assets/icons/left-arrow.svg";
import clipboardTextClockBlacksub from "../../assets/icons/clipboard-text-clock-blacksub.svg";
import clipboardTextClockWhitesub from "../../assets/icons/clipboard-text-clock-whitesub.svg";
import chartTimelineBlacksub from "../../assets/icons/chart-timeline-blacksub.svg";
import chartTimelineWhitesub from "../../assets/icons/chart-timeline-whitesub.svg";
import cogBlacksub from "../../assets/icons/cog-blacksub.svg";
import cogWhitesub from "../../assets/icons/cog-whitesub.svg";
import peopleBlacksub from "../../assets/icons/people-blacksub.svg";
import peopleWhitesub from "../../assets/icons/people-whitesub.svg";
import uploadBlacksub from "../../assets/icons/upload-blacksub.svg";
import uploadWhitesub from "../../assets/icons/upload-whitesub.svg";
import viewDashboardBlacksub from "../../assets/icons/view-dashboard-blacksub.svg";
import viewDashboardWhitesub from "../../assets/icons/view-dashboard-whitesub.svg";
import MenuButton from "../../components/menu_button";
import PostMediaScreen from "./post-media/post_media";
import { Body1, Headline1 } from "../../components/text";
import SettingScreen from "./setting/setting";
import DashBoard, { DashBoardMode } from "./dashboard/dashboard";
import { useNavigate } from "react-router-dom";
import Cookies from "universal-cookie";
import Insight, { InsightMode } from "./insight/insight";
import HistoryScreen from "./history/history";
import { UserInfo } from "../../interfaces/interface";
import defaultImageRectangle from "../../assets/images/default_rectangle.svg";
import FootTrafficInfo from "./foot-traffic-info/foot_traffic_info";

const HomePage = () => {
  const mainDivRef = useRef<HTMLDivElement>(null);
  const navigate = useNavigate();
  const [divHeight, setDivHeight] = useState(0);
  const [currMenuIdx, setCurrMenuIdx] = useState(0);
  const [currInfo, setCurrInfo] = useState<UserInfo | undefined | null>(null);

  const logout = () => {
    const cookies = new Cookies();
    // 인증 관련 토큰 제거
    cookies.remove("accessToken");
    cookies.remove("refreshToken");
    // 사용자 정보 관련 토큰 제거
    cookies.remove("userInfo");
    cookies.remove("role");
    // 자동 로그인 설정 제거
    cookies.remove("autoLogin");

    navigate("/");
  };

  const [dashboardMode, setDashboardMode] = useState(DashBoardMode.LIST);
  const [insightMode, setInsightMode] = useState(InsightMode.LIST);

  const menuButtons = [
    {
      title: "대시보드",
      description: "해당 기간에 집행한 광고에 대한 통계를 확인할 수 있어요.",
      iconWhiteSrc: viewDashboardWhitesub,
      iconBlackSrc: viewDashboardBlacksub,
      component: <DashBoard mode={dashboardMode} setMode={setDashboardMode} />,
    },
    {
      title: "인사이트",
      description:
        "하나의 광고에 대해 전체 기간의 통계를 한눈에 확인할 수 있어요.",
      iconWhiteSrc: chartTimelineWhitesub,
      iconBlackSrc: chartTimelineBlacksub,
      component: <Insight mode={insightMode} setMode={setInsightMode} />,
    },
    {
      title: "유동인구정보",
      description:
        "각 위치별로 유동인구 정보를 비교할 수 있어요. 집행할 디스플레이 선택시 참고하면 좋아요.",
      iconWhiteSrc: peopleWhitesub,
      iconBlackSrc: peopleBlacksub,
      component: <FootTrafficInfo />,
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
      component: <HistoryScreen />,
    },
    {
      title: "설정",
      description: "",
      iconWhiteSrc: cogWhitesub,
      iconBlackSrc: cogBlacksub,
      component: (
        <SettingScreen userInfo={currInfo} setUserInfo={setCurrInfo} />
      ),
    },
  ];

  const loadInfo = () => {
    const cookies = new Cookies();
    const userInfo: UserInfo = cookies.get("userInfo");
    setCurrInfo(userInfo);
  };

  useEffect(() => {
    loadInfo();
  }, []);

  useEffect(() => {
    const height = mainDivRef.current?.clientHeight || 0;
    setDivHeight(height);
  }, [divHeight]);

  return (
    <div className="flex">
      {/* 좌측 영역 */}
      <div className="flex flex-col h-screen">
        <div ref={mainDivRef} className="bg-main pt-9 pb-[66px] px-[60px]">
          <img className="h-5" src={logoWhite} />
          <div className="flex flex-col w-[180px] mt-12 items-center">
            <img
              className="w-20 h-20 rounded-full bg-white"
              src={currInfo?.profileImage}
              onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
                const target = e.target as HTMLImageElement;
                target.src = defaultImageRectangle;
              }}
            />
            <p className="font-medium text-base text-white pt-5 pb-2">
              {currInfo?.company}
            </p>
            <p className="text-white">{currInfo?.email}</p>
          </div>
        </div>
        <div className="flex-grow bg-[#F0F2F5] flex flex-col justify-between">
          <div className="grid grid-cols-2 gap-y-3 gap-x-4 p-[60px]">
            {menuButtons.map((button, index) => (
              <MenuButton
                key={`menu-button-${index}`}
                title={button.title}
                iconBlackSrc={button.iconBlackSrc}
                iconWhiteSrc={button.iconWhiteSrc}
                isActive={index === currMenuIdx}
                onClick={() => {
                  if (index !== 0) {
                    // 대시보드가 아닌 메뉴 클릭 시 리스트 뷰로 초기화
                    setDashboardMode(DashBoardMode.LIST);
                  }
                  if (index !== 1) {
                    // 인사이트가 아닌 메뉴 클릭 시 리스트 뷰로 초기화
                    setInsightMode(InsightMode.LIST);
                  }
                  setCurrMenuIdx(index);
                }}
              />
            ))}
          </div>
          <footer className="flex flex-col items-center gap-1 pb-[60px]">
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
            <Headline1
              text={menuButtons[currMenuIdx].title}
              color="text-white"
            />
            <Body1
              text={menuButtons[currMenuIdx].description}
              color="text-white_sub"
            />
          </div>
          {currMenuIdx === 0 && dashboardMode === DashBoardMode.DETAIL && (
            <div
              className="flex gap-2 px-3 py-2 bg-white rounded-lg cursor-pointer"
              onClick={() => setDashboardMode(DashBoardMode.LIST)}
            >
              <img src={leftArrow} />
              <p className="text-xs">광고목록</p>
            </div>
          )}
          {currMenuIdx === 1 && insightMode === InsightMode.DETAIL && (
            <div
              className="flex gap-2 px-3 py-2 bg-white rounded-lg cursor-pointer"
              onClick={() => setInsightMode(InsightMode.LIST)}
            >
              <img src={leftArrow} />
              <p className="text-xs">광고목록</p>
            </div>
          )}
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
