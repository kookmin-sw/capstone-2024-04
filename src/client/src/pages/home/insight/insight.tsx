import { useEffect, useState } from "react";
import { Subtitle2 } from "../../../components/text";
import GridViewMedia from "../gridview-media/gridview_media";
import { MediaInfo } from "../../../interfaces/interface";
import { getMedia } from "../../../api/client/media";
import InsightDetail from "./Insight_detail";

export enum InsightMode {
  LIST,
  DETAIL,
}

const Insight = ({ mode, setMode }: any) => {
  const [medias, setMedias] = useState<MediaInfo[]>([]);
  const [detailInfo, setDetailInfo] = useState<MediaInfo | null>(null);

  useEffect(() => {
    loadInsightList();
  }, []);

  const loadInsightList = async () => {
    const response = await getMedia({ filter: "ACCEPT" });

    if (response.status === 200) {
      console.log(response);
      setMedias(response.data.data);
    }
  };

  return mode === InsightMode.LIST ? (
    <div className="flex flex-col h-full min-w-[920px] w-full px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <GridViewMedia
        mediaList={medias}
        setMode={setMode}
        setDetailInfo={setDetailInfo}
      />
    </div>
  ) : (
    <InsightDetail detailInfo={detailInfo} />
  );
};

export default Insight;
