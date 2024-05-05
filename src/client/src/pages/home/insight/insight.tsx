import { useState } from "react";
import { Subtitle1, Subtitle2 } from "../../../components/text";
import GridViewMedia from "../gridview-media/gridview_media";
import { MediaInfo } from "../../../interfaces/interface";

export enum InsightMode {
  LIST,
  DETAIL,
}

const Insight = () => {
  const [mode, setMode] = useState(InsightMode.LIST);
  const [detailInfo, setDetailInfo] = useState<MediaInfo | null>(null);

  const data: MediaInfo[] = [
    {
      mediaId: 1,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 테스트 1",
      description: "광고 테스트 1에 대한 설명이에요.",
      dashboard: [],
    },
    {
      mediaId: 2,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 테스트 2",
      description: "광고 테스트 2에 대한 설명이에요.",
      dashboard: [],
    },
  ];

  return mode === InsightMode.LIST ? (
    <div className="flex flex-col h-full min-w-[920px] w-full px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <GridViewMedia
        mediaList={data}
        setMode={setMode}
        setDetailInfo={setDetailInfo}
      />
    </div>
  ) : (
    <div className="flex flex-col h-full min-w-[920px] w-full px-[30px] overflow-y-scroll">
      <div className="flex w-full justify-between items-center">
        <div className="flex flex-col gap-4">
          <h1>"광고 이름" 요약</h1>
          <Subtitle1 text="광고 설명" color="text-black" />
          <button onClick={() => console.log("대시보드 바로가기 클릭")}>
            대시보드 바로가기
          </button>
        </div>
        <img className="h-32 aspect-video" alt="광고 썸네일" />
      </div>
    </div>
  );
};

export default Insight;
