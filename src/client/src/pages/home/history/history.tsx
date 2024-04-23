import { DatePicker, Modal, Select, Table, TableColumnsType } from "antd";
import { Subtitle1, Subtitle2 } from "../../../components/text";
import { MediaInfo } from "../../../interfaces/interface";
import StatusBadge, { Status } from "../../../components/status_badge";
import { useState } from "react";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import dashboardIconWhite from "../../../assets/icons/view-dashboard-whitesub.svg";
import chartIconMain from "../../../assets/icons/chart-timeline-main.svg";

dayjs.extend(customParseFormat);
const dateFormat = "YYYY-MM-DD";

const HistoryScreen = () => {
  const columns: TableColumnsType<MediaInfoWithStatus> = [
    {
      title: "",
      dataIndex: "mediaLink",
      render: (mediaLink) => (
        <img className="w-[50px] h-[50px]" src={mediaLink} />
      ),
    },
    {
      title: "광고 타이틀",
      dataIndex: "title",
    },
    {
      title: "디스플레이",
      dataIndex: "display",
    },
    {
      title: "광고 일시",
      dataIndex: "",
    },
    {
      title: "광고 상태",
      dataIndex: "status",
      render: (status: Status) => <StatusBadge status={status} />,
    },
  ];

  const mediaData: MediaInfoWithStatus[] = [
    {
      mediaId: 1,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 타이틀 1",
      description: "광고 1에 대한 설명이에요",
      dashboard: [],
      status: Status.집행중,
    },
    {
      mediaId: 2,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 타이틀 2",
      description: "광고 2에 대한 설명이에요",
      dashboard: [],
      status: Status.등록대기,
    },
  ];

  const [openModal, setOpenModal] = useState(false);
  const [selectedMedia, setSelectedMedia] =
    useState<MediaInfoWithStatus | null>(null);

  interface MediaInfoWithStatus extends MediaInfo {
    status: Status;
  }

  return (
    <>
      <div className="flex flex-col h-full min-w-[920px] px-[30px] gap-7 overflow-y-scroll">
        <Subtitle2 text="광고 목록" color="black" />
        <Table
          size="small"
          columns={columns}
          dataSource={mediaData}
          pagination={{ pageSize: 8, position: ["bottomCenter"] }}
          onRow={(record) => ({
            onClick: () => {
              console.log(record);
              setSelectedMedia(record);
              setOpenModal(true);
            },
          })}
        />
      </div>
      <Modal
        open={openModal}
        footer={false}
        onCancel={() => setOpenModal(false)}
      >
        <div className="w-full p-12 flex flex-col">
          <Subtitle1 text="광고 타이틀" color="black" />
          <h3 className="px-2 py-3 border-2 mt-[10px] mb-4 border-[#d9d9d9] rounded-[2px]">
            {selectedMedia?.title}
          </h3>
          <img
            className="w-full aspect-video object-cover rounded-[5px] mb-4"
            src={selectedMedia?.mediaLink}
            alt="광고 썸네일"
          />
          <Subtitle1 text="광고 등록일" color="black" />
          <DatePicker.RangePicker
            disabled
            defaultValue={[
              dayjs("2024-04-04", dateFormat),
              dayjs("2024-06-10", dateFormat),
            ]} // TODO: interface 수정 필요
            className="mt-[10px] mb-4"
          />
          <Subtitle1 text="디스플레이" color="black" />
          <Select
            disabled
            defaultValue={"선택된 디스플레이"} // TODO: interface 수정 필요
            className="mt-[10px] mb-4"
          />
          <Subtitle1 text="광고 상태" color="black" />
          <div className="mb-5 mt-2">
            <StatusBadge status={selectedMedia?.status} />
          </div>
          <div className="flex gap-2">
            <button
              className="flex gap-3 w-full justify-center items-center py-3 rounded-[3px] bg-main"
              onClick={() => {
                console.log("대시보드 버튼 클릭");
                setOpenModal(false);
              }}
            >
              <img src={dashboardIconWhite} />
              <p className="text-white">대시보드</p>
            </button>
            <button
              className="flex gap-3 w-full justify-center border-[1px] items-center border-main py-3 rounded-[3px]"
              onClick={() => {
                console.log("인사이트 버튼 클릭");
                setOpenModal(false);
              }}
            >
              <img src={chartIconMain} />
              <p className="text-main">인사이트</p>
            </button>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default HistoryScreen;
