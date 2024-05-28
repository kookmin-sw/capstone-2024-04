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

const PlayListScreen = () => {
  const { locationId } = useParams();
  const [isLoad, setIsLoad] = useState<boolean>(false);
  const [videoList, setVideoList] = useState<TodayList[]>([]);
  const [currentVideoIndex, setCurrentVideoIndex] = useState<number>(0);
  const [videoUrl, setVideoUrl] = useState<string | undefined>(undefined);
  const [refreshKey, setRefreshKey] = useState<number>(0);

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

  useEffect(() => {
    if (videoList.length > 0) {
      setVideoUrl(videoList[currentVideoIndex].media.mediaLink);
    }
  }, [currentVideoIndex, videoList]);

  const handleVideoEnded = () => {
    setCurrentVideoIndex(
      currentVideoIndex + 1 === videoList.length ? 0 : currentVideoIndex + 1
    );
    setRefreshKey((prev) => prev + 1);
  };

  return (
    <div className="w-screen h-screen">
      {isLoad ? (
        <ReactPlayer
          key={refreshKey}
          width={"100%"}
          height={"100%"}
          url={videoUrl}
          controls
          playing
          muted
          onEnded={handleVideoEnded}
        />
      ) : (
        <></>
      )}
    </div>
  );
};

export default PlayListScreen;
