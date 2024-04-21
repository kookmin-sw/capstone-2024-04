import { Table, TableColumnsType } from "antd";
import { Subtitle2 } from "../../../components/text";
import { MediaInfo } from "../../../interfaces/interface";
import StatusBadge, { Status } from "../../../components/status_badge";

const HistoryScreen = () => {
  const columns: TableColumnsType<MediaInfo> = [
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

  interface MediaInfoWithStatus extends MediaInfo {
    status: Status;
  }

  return (
    <div className="flex flex-col h-full min-w-[920px] px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="black" />
      <Table
        size="small"
        columns={columns}
        dataSource={mediaData}
        pagination={{ pageSize: 8, position: ["bottomCenter"] }}
      />
    </div>
  );
};

export default HistoryScreen;
