import { Subtitle2 } from "../../../components/text";
import { Table, TableColumnsType } from "antd";
import StatusBadge from "../../../components/status_badge";
import { useEffect, useState } from "react";
import { getApply } from "../../../api/admin/apply";
import { TotalApplicationInfo } from "../../../interfaces/interface";
import defaultImageRectangle from "../../../assets/images/default_rectangle.svg";

interface ApplyTableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  company: string;
  title: string;
  display: string;
  date: string[];
  status: string;
}

const AdminHistoryPage = () => {
  const loadApplyList = async () => {
    const result = await getApply();
    const data: TotalApplicationInfo[] = result.data.data;
    setDataSource(
      data.map((item: TotalApplicationInfo) => ({
        key: item.application.applicationId,
        mediaId: item.media.mediaId,
        mediaLink: item.media.mediaLink,
        company: item.application.user.company,
        title: item.media.title,
        display: item.application.location.address,
        date: [item.application.startDate, item.application.endDate],
        status: item.application.status,
      }))
    );
  };

  useEffect(() => {
    loadApplyList();
  }, []);

  const [dataSource, setDataSource] = useState<ApplyTableItem[]>([]);

  const columns: TableColumnsType<ApplyTableItem> = [
    {
      title: "",
      dataIndex: "mediaLink",
      render: (mediaLink) => (
        <img
          className="w-[50px] h-[50px]"
          src={mediaLink}
          onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
            const target = e.target as HTMLImageElement;
            target.src = defaultImageRectangle;
          }}
        />
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
      dataIndex: "status",
      render: (status, record) => (
        <StatusBadge status={status} date={record.date} />
      ),
    },
  ];

  return (
    <div className="flex flex-col min-w-[920px] h-full px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <Table
        size="small"
        columns={columns}
        dataSource={dataSource}
        pagination={{ pageSize: 8, position: ["bottomCenter"] }}
      />
    </div>
  );
};

export default AdminHistoryPage;
