import { useEffect, useRef, useState } from "react";
import logoWhite from "../../assets/images/LogoWhite.svg";
import infoCircle from "../../assets/icons/InfoCircle.svg";
import clipboardTextClockBlacksub from "../../assets/icons/clipboard-text-clock-blacksub.svg";
import clipboardTextClockWhitesub from "../../assets/icons/clipboard-text-clock-whitesub.svg";
import cogBlacksub from "../../assets/icons/cog-blacksub.svg";
import cogWhitesub from "../../assets/icons/cog-whitesub.svg";
import listboxWhite from "../../assets/icons/list-box-whitesub.svg";
import listboxBlack from "../../assets/icons/list-box-blacksub.svg";
import MenuButton from "../../components/menu_button";

import { Body1, Headline1 } from "../../components/text";
import { useNavigate } from "react-router-dom";
import Cookies from "universal-cookie";
import SettingScreen from "../home/setting/setting";
import { UserInfo } from "../../interfaces/interface";
import AdminApplyPage from "./apply/apply";
import AdminHistoryPage from "./history/history";

const AdminPage = () => {
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
    // 자동 로그인 설정 제거
    cookies.remove("autoLogin");

    navigate("/");
  };

  const loadInfo = () => {
    const cookies = new Cookies();
    const userInfo: UserInfo = cookies.get("userInfo");
    setCurrInfo(userInfo);
  };

  useEffect(() => {
    loadInfo();
  }, []);

  const menuButtons = [
    {
      title: "광고 승인",
      description: "전체 고객사의 광고를 승인하거나 거절할 수 있어요.",
      iconWhiteSrc: listboxWhite,
      iconBlackSrc: listboxBlack,
      component: <AdminApplyPage />,
    },
    {
      title: "히스토리",
      description: "전체 고객사의 광고 히스토리를 볼 수 있어요.",
      iconWhiteSrc: clipboardTextClockWhitesub,
      iconBlackSrc: clipboardTextClockBlacksub,
      component: <AdminHistoryPage />,
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
            <img
              className="w-20 h-20 rounded-full bg-white"
              src={currInfo?.profileImage}
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
                onClick={() => setCurrMenuIdx(index)}
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

export default AdminPage;
