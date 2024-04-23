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
      {mediaList.length !== 0 ? (
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
      ) : (
        <div className="w-full h-full flex justify-center items-center text-base font-medium">
          아직 집행한 광고가 없습니다.
        </div>
      )}
    </div>
  );
};

export default GridViewMedia;
