import { useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  GetDailyPlayListProps,
  getDailyPlayList,
} from "../../api/none/broadcasting";
import { LocationInfo, MediaInfo } from "../../interfaces/interface";

interface TodayList {
  playListId: number;
  broadCasting: boolean;
  location: LocationInfo;
  media: MediaInfo;
}

const PlayListScreen = () => {
  const { locationId } = useParams();

  const loadDailyPlayList = async ({ locationId }: GetDailyPlayListProps) => {
    const result = await getDailyPlayList({ locationId });
    if (result.status === 200) {
      console.log(result.data.data);
    }
  };

  useEffect(() => {
    if (locationId) {
      loadDailyPlayList({ locationId: parseInt(locationId) });
    }
  }, [locationId]);

  return <>{locationId}</>;
};

export default PlayListScreen;
