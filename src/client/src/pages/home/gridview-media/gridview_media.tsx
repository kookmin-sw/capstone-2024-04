import React from "react";
import MediaCard from "../../../components/media_card";
import { MediaInfo } from "../../../interfaces/interface";
import { InsightMode } from "../insight/insight";

interface GridViewMedia {
  mediaList: Array<MediaInfo>;
  setMode: React.Dispatch<React.SetStateAction<InsightMode>>;
  setDetailInfo: React.Dispatch<React.SetStateAction<MediaInfo | null>>;
}

const GridViewMedia = ({
  mediaList,
  setMode,
  setDetailInfo,
}: GridViewMedia) => {
  return (
    <div className="flex flex-col gap-7 min-w-[920px] h-full">
      <div className="grid grid-cols-4 gap-x-2 gap-y-7 overflow-y-scroll">
        {mediaList.map((media, index) => {
          return (
            <MediaCard
              onClick={() => {
                setDetailInfo(media);
                setMode(InsightMode.DETAIL);
              }}
              key={`card-${index}`}
              img={media.mediaLink}
              title={media.title}
              description={media.description}
            />
          );
        })}
      </div>
    </div>
  );
};

export default GridViewMedia;
