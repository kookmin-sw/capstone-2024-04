import { useEffect, useState } from "react";
import logoMain from "../../../assets/images/LogoMain.svg";
import GridViewMedia from "../gridview-media/gridview_media";
import { MediaCardProps } from "../../../components/media_card";

export enum DashBoardMode {
  LIST,
  DETAIL,
}

const DashBoard = () => {
  const [mode, setMode] = useState(DashBoardMode.LIST);
  const [detailInfo, setDetailInfo] = useState<MediaCardProps | null>(null);

  return mode === DashBoardMode.LIST ? (
    <GridViewMedia
      setMode={setMode}
      setDetailInfo={setDetailInfo}
      mediaList={[
        {
          img: logoMain,
          title: "광고1",
          description:
            "광고1에 대한 설명입니다. 조금 많이 길어서 잘릴 수도 있지만 그럼에도 불구하고 많이 작성해요",
        },
        {
          img: logoMain,
          title: "광고1",
          description:
            "광고1에 대한 설명입니다. 조금 많이 길어서 잘릴 수도 있지만 그럼에도 불구하고 많이 작성해요",
        },
      ]}
    />
  ) : (
    <>Detail Page</>
  );
};

export default DashBoard;
