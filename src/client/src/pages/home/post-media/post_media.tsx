import { useEffect, useState } from "react";
import inbox from "../../../assets/icons/Inbox.svg";
import upload from "../../../assets/icons/Upload.svg";
import { DatePicker, Select, Input, SelectProps } from "antd";
import { Body1, Subtitle1 } from "../../../components/text";
import { getLocation } from "../../../api/location";

interface LocationInfo {
  locationId: number;
  address: string;
}

const PostMediaScreen = () => {
  const enum PostMode {
    "UPLOAD", // 새로운 광고 등록
    "HISTORY", // 히스토리에서 광고 선택
  }

  const [postMode, setPostMode] = useState<PostMode>(PostMode.UPLOAD);
  const [video, setVideo] = useState<File | null>(null);
  const [options, setOptions] = useState<SelectProps["options"]>([]);
  const { RangePicker } = DatePicker;

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

  useEffect(() => {
    loadLocationList();
  }, []);

  return (
    <div className="flex h-full min-w-[920px]">
      <div className="flex-1 flex flex-col px-[30px] h-full justify-between">
        <div>
          <Subtitle1 text="등록 타입" color="black" />
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
              onClick={() => setPostMode(PostMode.HISTORY)}
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
                <Subtitle1 text="로컬에서 파일 업로드하기" color="black" />
                <Body1 text="최대 1.3GB 등록 가능" color="gray2" />
              </div>
            ) : (
              // 업로드된 비디오가 있는 경우
              <div className="flex flex-col w-full">
                <button
                  className="flex w-[100px] basis-1 gap-2 px-4 py-1 border-2 border-gray2 rounded-sm justify-center items-center"
                  onClick={uploadVideo}
                >
                  <img className="w-[14px] h-[14px]" src={upload} />
                  <Body1 text="Upload" color="black" />
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
            <div className="border-gray2 border-[1px] mb-12 min-h-[420px]">
              {/* 히스토리가 존재하지 않은 경우에도 스크린이 필요 */}
            </div>
          )}
        </div>

        <button className="w-[120px] py-3 text-white bg-main text-sm rounded-[3px]">
          광고 등록하기
        </button>
      </div>
      <div className="flex-1 flex-col px-[30px]">
        <Subtitle1 text="광고 등록일" color="black" />
        <RangePicker className="mt-2 mb-7" style={{ width: "100%" }} />
        <Subtitle1 text="디스플레이 선택" color="black" />
        <Select
          className="mt-2 mb-7"
          style={{ width: "100%" }}
          placeholder="디스플레이를 선택해주세요"
          options={options}
        />

        {postMode === PostMode.UPLOAD ? (
          <div className="flex flex-col mt-4">
            <Subtitle1 text="광고 타이틀" color="black" />
            <Input
              className="mt-2 mb-10"
              placeholder="해당 광고의 대시보드 타이틀을 입력해주세요"
            />
            <Subtitle1 text="광고 설명" color="black" />
            <Input.TextArea
              className="mt-2"
              style={{ resize: "none" }}
              rows={5}
              placeholder="해당 광고의 대시보드 설명을 입력해주세요"
            />
          </div>
        ) : (
          <div className="flex flex-col">
            <Subtitle1 text="광고 이미지 미리보기" color="black" />
            <div className="w-full mt-2 aspect-video border-gray2 border-[1px] rounded-lg" />
          </div>
        )}
      </div>
    </div>
  );
};

export default PostMediaScreen;
