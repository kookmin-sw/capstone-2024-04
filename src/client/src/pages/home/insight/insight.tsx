import { useState } from "react";
import { Subtitle2 } from "../../../components/text";
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
    <div className="flex flex-col h-full min-w-[920px] px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="black" />
      <GridViewMedia
        mediaList={data}
        setMode={setMode}
        setDetailInfo={setDetailInfo}
      />
    </div>
  ) : (
    <>Detail Page</>
  );
};

export default Insight;
