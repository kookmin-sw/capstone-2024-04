import React from "react";
import MediaCard, { MediaCardProps } from "../../../components/media_card";
import { Subtitle2 } from "../../../components/text";
import { DashBoardMode } from "../dashboard/dashboard";

interface GridViewMedia {
  mediaList: Array<MediaCardProps>;
  setMode: React.Dispatch<React.SetStateAction<DashBoardMode>>;
  setDetailInfo: React.Dispatch<React.SetStateAction<MediaCardProps | null>>;
}

const GridViewMedia = ({
  mediaList,
  setMode,
  setDetailInfo,
}: GridViewMedia) => {
  return (
    <div className="flex flex-col gap-7 min-w-[920px] h-full">
      <Subtitle2 text="광고 목록" color="black" />
      <div className="grid grid-cols-4 gap-x-2 gap-y-7 overflow-y-scroll">
        {mediaList.map((media, index) => {
          return (
            <MediaCard
              onClick={() => {
                setDetailInfo(media);
                setMode(DashBoardMode.DETAIL);
              }}
              key={`card-${index}`}
              img={media.img}
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
