import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getDailyPlayList } from "../../api/none/broadcasting";
import { LocationInfo, MediaInfo } from "../../interfaces/interface";
import ReactPlayer from "react-player/lazy";
import { Kafka } from "kafkajs";

interface TodayList {
  playListId: number;
  broadCasting: boolean;
  location: LocationInfo;
  media: MediaInfo;
}
const KAFKA_BROKER = import.meta.env.VITE_APP_KAFKA_BROKER;
const kafka = new Kafka({
  clientId: "my-app",
  brokers: [KAFKA_BROKER],
});

const producer = kafka.producer();

const getCurrentTimeList = () => {
  const now = new Date();
  return [
    now.getFullYear(),
    now.getMonth() + 1,
    now.getDate(),
    now.getHours(),
    now.getMinutes(),
    now.getSeconds(),
    now.getMilliseconds(),
    0,
    0,
  ];
};

const sendAdChange = async (playlist_index: number) => {
  const currentTimeList = getCurrentTimeList();

  try {
    await producer.connect();

    const messagePayload = {
      playlist_index,
      broadcast_time: currentTimeList,
    };

    await producer.send({
      topic: "drm-advt-topic",
      messages: [
        {
          key: String(playlist_index),
          value: JSON.stringify(messagePayload),
        },
      ],
    });

    console.log("Message sent successfully");
  } catch (error) {
    console.error("Failed to send message:", error);
  } finally {
    await producer.disconnect();
  }
};

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
      sendAdChange(currentVideoIndex).catch(console.error);
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
          onEnded={handleVideoEnded} // Typo correction: "onEmded" -> "onEnded"
        />
      ) : (
        <></>
      )}
    </div>
  );
};

export default PlayListScreen;
