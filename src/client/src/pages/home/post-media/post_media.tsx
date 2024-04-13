import { useState } from "react";
import inbox from "../../../assets/icons/Inbox.svg";
import upload from "../../../assets/icons/Upload.svg";
import { DatePicker, Select, Input } from "antd";

const PostMediaScreen = () => {
  const enum PostMode {
    "UPLOAD", // 새로운 광고 등록
    "HISTORY", // 히스토리에서 광고 선택
  }

  const [postMode, setPostMode] = useState<PostMode>(PostMode.UPLOAD);
  const [video, setVideo] = useState<File | null>(null);
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

  return (
    <div className="flex h-full">
      <div className="flex-1 flex flex-col px-[30px] h-full justify-between">
        <div>
          <h3>등록 타입</h3>
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
                className="flex flex-col items-center justify-center w-full h-[320px] border-gray2 border-[1px] bg-[#fafafa] cursor-pointer"
                onClick={uploadVideo}
              >
                <img className="w-12 h-12" src={inbox} />
                <p>로컬에서 파일 업로드하기</p>
                <p>최대 1.3GB 등록 가능</p>
              </div>
            ) : (
              // 업로드된 비디오가 있는 경우
              <div className="flex flex-col w-full">
                <button
                  className="flex w-[100px] basis-1 gap-2 px-4 py-1 border-2 border-gray2 rounded-sm justify-center items-center"
                  onClick={uploadVideo}
                >
                  <img className="w-[14px] h-[14px]" src={upload} />
                  <p>Upload</p>
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
        <p>광고 등록일</p>
        <RangePicker style={{ width: "100%" }} />
        <p>디스플레이 선택</p>
        <Select style={{ width: "100%" }} />

        {postMode === PostMode.UPLOAD ? (
          <div className="flex flex-col">
            <p>광고 타이틀</p>
            <Input />
            <p>광고 설명</p>
            <Input.TextArea style={{ resize: "none" }} rows={5} />
          </div>
        ) : (
          <div className="flex flex-col">
            <p>광고 이미지 미리보기</p>
            <div className="w-full aspect-video border-gray2 border-[1px] rounded-lg" />
          </div>
        )}
      </div>
    </div>
  );
};

export default PostMediaScreen;
