import { Subtitle2 } from "../../../components/text";
import { Table, TableColumnsType } from "antd";
import StatusBadge, { Status } from "../../../components/status_badge";

interface ApplyTableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  title: string;
  display: string;
  date: string[];
}

const AdminHistoryPage = () => {
  const columns: TableColumnsType<ApplyTableItem> = [
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
      title: "회사명",
      dataIndex: "",
    },
    {
      title: "디스플레이",
      dataIndex: "display",
    },
    {
      title: "광고 일시",
      dataIndex: "date",
      render: (date) => (
        <p>
          {date[0]} ~ {date[1]}
        </p>
      ),
    },
    {
      title: "광고 상태",
      dataIndex: "",
      render: () => <StatusBadge status={Status.등록거절} />,
    },
  ];

  const tableData: ApplyTableItem[] = [
    {
      key: 1,
      mediaId: 1,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 테스트 1",
      display: "미래관 4층",
      date: ["2024-04-18", "2024-04-21"],
    },
    {
      key: 2,
      mediaId: 2,
      mediaLink:
        "https://wink.kookmin.ac.kr/_next/image?url=https%3A%2F%2Fgithub.com%2FChoi-Jiwon-38.png&w=256&q=75",
      title: "광고 테스트 2",
      display: "과학관 1층 카페",
      date: ["2024-04-17", "2024-04-18"],
    },
  ];

  return (
    <div className="flex flex-col min-w-[920px] h-full px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <Table
        size="small"
        columns={columns}
        dataSource={tableData}
        pagination={{ pageSize: 8, position: ["bottomCenter"] }}
      />
    </div>
  );
};

export default AdminHistoryPage;
