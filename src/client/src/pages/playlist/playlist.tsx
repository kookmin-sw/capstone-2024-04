import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getDailyPlayList } from "../../api/none/broadcasting";
import { LocationInfo, MediaInfo } from "../../interfaces/interface";
import { default as _ReactPlayer } from "react-player/lazy";
import { ReactPlayerProps } from "react-player/types/lib";
const ReactPlayer = _ReactPlayer as unknown as React.FC<ReactPlayerProps>;

interface TodayList {
  playListId: number;
  broadCasting: boolean;
  location: LocationInfo;
  media: MediaInfo;
}

// const getCurrentTimeList = () => {
//   const now = new Date();
//   return [
//     now.getFullYear(),
//     now.getMonth() + 1,
//     now.getDate(),
//     now.getHours(),
//     now.getMinutes(),
//     now.getSeconds(),
//     now.getMilliseconds(),
//     0,
//     0,
//   ];
// };

const PlayListScreen = () => {
  const { locationId } = useParams();
  const [isLoad, setIsLoad] = useState<boolean>(false);
  const [videoList, setVideoList] = useState<TodayList[]>([]);
  const [currentVideoIndex, setCurrentVideoIndex] = useState<number>(0);

  const loadDailyPlayList = async (locationId: number) => {
    try {
      const result = await getDailyPlayList({ locationId });
      if (result.status === 200) {
        console.log(result.data.data);
        setVideoList(result.data.data);
        setIsLoad(true);
      }
    } catch (error) {
      console.error("Failed to load playlist:", error);
    }
  };

  useEffect(() => {
    if (locationId) {
      loadDailyPlayList(parseInt(locationId));
    }
  }, [locationId]);

  const handleVideoEnded = () => {
    setCurrentVideoIndex(
      currentVideoIndex + 1 === videoList.length ? 0 : currentVideoIndex + 1
    );
  };

  useEffect(() => {
    if (videoList.length > 0) {
      // 서버 측에 다음 동영상으로 넘어갔음을 알림
    }
  }, [currentVideoIndex, videoList]);

  return (
    <div className="w-screen h-screen">
      {isLoad ? (
        <ReactPlayer
          width={"100%"}
          height={"100%"}
          url={videoList[currentVideoIndex].media.mediaLink}
          controls
          playing
          onEnded={handleVideoEnded}
        />
      ) : (
        <></>
      )}
    </div>
  );
};

export default PlayListScreen;
