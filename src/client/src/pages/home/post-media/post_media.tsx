import { useEffect, useRef, useState } from "react";
import inbox from "../../../assets/icons/Inbox.svg";
import upload from "../../../assets/icons/Upload.svg";
import { DatePicker, Select, Input, InputRef } from "antd";
import { SelectProps } from "antd/lib/select";
import { Body1, Subtitle1 } from "../../../components/text";
import { getLocation } from "../../../api/client/location";
import { LocationInfo, MediaInfo } from "../../../interfaces/interface";
import {
  PostMediaRequest,
  getMedia,
  postMedia,
} from "../../../api/client/media";
import { toast } from "react-hot-toast";
import moment from "moment";
import { postApply } from "../../../api/client/apply";
import { TextAreaRef } from "antd/lib/input/TextArea";

const PostMediaScreen = () => {
  const enum PostMode {
    "UPLOAD", // 새로운 광고 등록
    "HISTORY", // 히스토리에서 광고 선택
  }

  const titleRef = useRef<InputRef>(null);
  const descriptionRef = useRef<TextAreaRef>(null);

  const [locationId, setLocationId] = useState<number | null>(null);
  const [postMode, setPostMode] = useState<PostMode>(PostMode.UPLOAD);
  const [video, setVideo] = useState<File | null>(null);
  const [options, setOptions] = useState<SelectProps["options"]>([]);
  const [date, setDate] = useState<string[]>([]);
  const [selectedHistory, setSelectedHistory] = useState<MediaInfo | null>(
    null
  );
  const { RangePicker } = DatePicker;
  const [histories, setHistories] = useState<MediaInfo[]>([]);

  const resetForm = () => {
    if (titleRef.current?.input) {
      titleRef.current.input.value = "";
    }
    if (descriptionRef.current?.resizableTextArea?.textArea.value) {
      descriptionRef.current.resizableTextArea.textArea.value = "";
    }
    setLocationId(null);
    setVideo(null);
    setDate([]);
    setSelectedHistory(null);
  };

  const requestPostMediaWithHistory = async () => {
    if (selectedHistory === null) {
      toast.error("히스토리는 필수로 선택되어야 합니다.");
      return false;
    }
    if (date.length === 0) {
      toast.error("광고 등록일을 선택해주세요.");
      return false;
    }
    if (locationId === -1) {
      toast.error("디스플레이를 선택해주세요.");
      return false;
    }

    const result = await postApply({
      mediaId: selectedHistory.mediaId,
      requestBody: {
        advertisementTitle: selectedHistory?.title,
        advertisementDescription: selectedHistory?.description,
        locationId: locationId,
        startDate: date[0],
        endDate: date[1],
      },
    });

    if (result.status === 201) {
      toast.success("성공적으로 광고가 등록되었습니다.");
      resetForm();
    } else {
      toast.error("광고 등록에 실패하였습니다.");
    }
  };

  const requestPostMedia = async () => {
    if (date.length === 0) {
      toast.error("광고 등록일을 선택해주세요.");
      return false;
    }
    if (!titleRef.current?.input?.value) {
      toast.error("광고 타이틀을 입력해주세요.");
      return false;
    }
    if (!descriptionRef.current?.resizableTextArea?.textArea.value) {
      toast.error("광고 설명을 입력해주세요.");
      return false;
    }
    if (!locationId) {
      toast.error("디스플레이를 선택해주세요.");
      return false;
    }

    const request: PostMediaRequest = {
      advertisementTitle: titleRef.current!.input!.value,
      advertisementDescription:
        descriptionRef.current!.resizableTextArea!.textArea.value,
      locationId: locationId,
      startDate: date[0],
      endDate: date[1],
    };
    const file = video;

    const formData = new FormData();
    const blob = new Blob([JSON.stringify(request)], {
      type: "application/json",
    });
    formData.append("request", blob);

    if (file) formData.append("file", file);
    else {
      toast.error("광고는 필수로 선택되어야 합니다.");
      return; // 실패
    }
    const result = await postMedia(formData);

    if (result && result.status === 201) {
      // 사용자 입력 정보 초기화
      toast.success("성공적으로 광고가 등록되었습니다.");
      resetForm();
    } else {
      toast.error("광고 등록에 실패하였습니다.");
    }
  };

  const uploadVideo = () => {
    const inputElement = document.createElement("input");
    inputElement.type = "file";
    inputElement.accept = "video/*";
    inputElement.onchange = handleFileChange;
    inputElement.click();
  };

  const handleFileChange = (e: any) => {
    const video = e.target.files?.[0];
    if (video) {
      setVideo(video);
    }
  };

  const loadLocationList = async () => {
    try {
      const result = await getLocation();

      if (result.status === 200) {
        const newOptions = result.data.data.map((location: LocationInfo) => ({
          label: location.address,
          value: location.locationId,
        }));
        setOptions(newOptions);
      }
    } catch (error) {
      console.error("Error loading location list:", error);
    }
  };

  const loadHistory = async () => {
    const result = await getMedia();
    console.log(result);
    if (result.status === 200) {
      setHistories(result.data.data);
    }
  };

  useEffect(() => {
    loadLocationList();
    loadHistory();
  }, []);

  return (
    <div className="flex h-full min-w-[920px]">
      <div className="flex-1 flex flex-col px-[30px] h-full justify-between">
        <div>
          <Subtitle1 text="등록 타입" color="text-black" />
          <div className="flex gap-1 mt-2 mb-5">
            <button
              onClick={() => setPostMode(PostMode.UPLOAD)}
              className={`${
                postMode === PostMode.UPLOAD
                  ? "text-main border-[1px] border-main"
                  : "text-black border-[1px] border-gray2"
              } w-full p-3 rounded-sm`}
            >
              새로운 광고 등록
            </button>
            <button
              onClick={() => {
                setPostMode(PostMode.HISTORY);
                setSelectedHistory(null);
              }}
              className={`${
                postMode === PostMode.HISTORY
                  ? "text-main border-[1px] border-main"
                  : "text-black border-[1px] border-gray2"
              } w-full p-3 rounded-sm`}
            >
              히스토리에서 광고 선택
            </button>
          </div>
          {postMode === PostMode.UPLOAD ? ( // 업로드 모드
            video === null ? ( // 업로드된 비디오가 없는 경우
              <div
                className="flex flex-col gap-1 items-center justify-center w-full h-[320px] border-gray2 border-[1px] bg-[#fafafa] cursor-pointer"
                onClick={uploadVideo}
              >
                <img className="w-12 h-12 mb-4" src={inbox} />
                <Subtitle1 text="로컬에서 파일 업로드하기" color="text-black" />
                <Body1 text="최대 1.3GB 등록 가능" color="text-gray2" />
              </div>
            ) : (
              // 업로드된 비디오가 있는 경우
              <div className="flex flex-col w-full">
                <button
                  className="flex w-[100px] basis-1 gap-2 px-4 py-1 border-2 border-gray2 rounded-sm justify-center items-center"
                  onClick={uploadVideo}
                >
                  <img className="w-[14px] h-[14px]" src={upload} />
                  <Body1 text="Upload" color="text-black" />
                </button>
                <video
                  className="w-[full] aspect-video bg-black mt-3"
                  src={URL.createObjectURL(video)}
                  controls
                />
              </div>
            )
          ) : (
            // 히스토리 모드
            <div className="border-gray2 divide-y border-[1px] mb-12 min-h-[420px] overflow-y-scroll">
              {histories.map((info) => {
                return (
                  <div
                    onClick={() => {
                      setSelectedHistory(info);
                    }}
                    key={info.mediaId}
                    className={`flex p-2 gap-2 ${
                      info.mediaId === selectedHistory?.mediaId
                        ? "bg-white_sub"
                        : "bg-white"
                    }`}
                  >
                    <img
                      className="w-12 h-12 border-[1px] border-gray rounded bg-white"
                      src={info.mediaLink}
                    />
                    <p>{info.title}</p>
                  </div>
                );
              })}
            </div>
          )}
        </div>

        <button
          className="w-[120px] py-3 text-white bg-main text-sm rounded-[3px]"
          onClick={() => {
            if (postMode === PostMode.UPLOAD) {
              requestPostMedia();
            } else if (postMode === PostMode.HISTORY) {
              requestPostMediaWithHistory();
            }
          }}
        >
          광고 등록하기
        </button>
      </div>
      <div className="flex-1 flex-col px-[30px]">
        <Subtitle1 text="광고 등록일" color="text-black" />
        <RangePicker
          disabledDate={(current) => {
            let customDate = moment().format("YYYY-MM-DD");
            return current && current < moment(customDate, "YYYY-MM-DD");
          }}
          format="YYYY-MM-DD"
          onChange={(_, dateStrings) => setDate(dateStrings)}
          className="mt-2 mb-7"
          style={{ width: "100%" }}
        />
        <Subtitle1 text="디스플레이 선택" color="text-black" />
        <Select
          className="mt-2 mb-7"
          style={{ width: "100%" }}
          placeholder="디스플레이를 선택해주세요"
          options={options}
          onSelect={(value) => {
            setLocationId(value);
          }}
          value={locationId}
        />

        {postMode === PostMode.UPLOAD ? (
          <div className="flex flex-col mt-4">
            <Subtitle1 text="광고 타이틀" color="text-black" />
            <Input
              ref={titleRef}
              className="mt-2 mb-10"
              placeholder="해당 광고의 대시보드 타이틀을 입력해주세요"
            />
            <Subtitle1 text="광고 설명" color="text-black" />
            <Input.TextArea
              ref={descriptionRef}
              className="mt-2"
              style={{ resize: "none" }}
              rows={5}
              placeholder="해당 광고의 대시보드 설명을 입력해주세요"
            />
          </div>
        ) : (
          <div className="flex flex-col">
            <Subtitle1 text="광고 미리보기" color="text-black" />
            <div className="w-full mt-2 aspect-video border-gray2 border-[1px] rounded-lg" />
          </div>
        )}
      </div>
    </div>
  );
};

export default PostMediaScreen;
