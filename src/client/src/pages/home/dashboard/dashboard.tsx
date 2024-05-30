import { useEffect, useState } from "react";
import { Subtitle2 } from "../../../components/text";
import { Table, TableColumnsType } from "antd";
import DashBoardDetail from "./dashboard_detail";
import { getApplies } from "../../../api/client/apply";
import {
  DashboardDataInfo,
  TotalApplicationInfo,
} from "../../../interfaces/interface";
import StatusBadge from "../../../components/status_badge";
import defaultImageRectangle from "../../../assets/images/default_rectangle.svg";
import {
  GetAdUnitDashboardProps,
  getAdUnitDashboard,
} from "../../../api/client/dashboard";
import { toast } from "react-hot-toast";

export enum DashBoardMode {
  LIST,
  DETAIL,
}

export interface TableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  title: string;
  display: string;
  date: string[];
  status: string;
}

const DashBoard = ({ mode, setMode, detailProps }: any) => {
  const [applies, setApplies] = useState<TableItem[]>([]);
  const [selectedData, setSelectedData] = useState<DashboardDataInfo | null>(
    null
  );
  const [dashboardTitle, setDashboardTitle] = useState<string>("");
  const [currDashboardId, setCurrDashboardId] = useState<number | null>(null);

  const loadDetailData = async ({ dashboardId }: GetAdUnitDashboardProps) => {
    const result = await getAdUnitDashboard({ dashboardId });
    console.log(result);
    if (result.status === 200) {
      setCurrDashboardId(dashboardId);
      result.data.data as DashboardDataInfo;
      setSelectedData(result.data.data);
      setMode(DashBoardMode.DETAIL);
      return true;
    } else if (result.status === 500) {
      toast.error("현재 대시보드에 접근할 수 없습니다.");
    }
    return false;
  };

  const columns: TableColumnsType<TableItem> = [
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

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    const response = await getApplies({ filter: "ACCEPT" });
    if (response.status === 200) {
      const totalApplications: TotalApplicationInfo[] = response.data.data;

      setApplies(
        totalApplications.map((application) => ({
          key: application.application.applicationId,
          mediaId: application.media.mediaId,
          mediaLink: application.media.mediaLink,
          title: application.media.title,
          display: application.application.location.address,
          date: [
            application.application.startDate,
            application.application.endDate,
          ],
          status: "ACCEPT",
        })) as TableItem[]
      );
    }
  };

  return mode === DashBoardMode.LIST ? (
    <div className="flex flex-col h-full min-w-[920px] px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <Table
        size="small"
        columns={columns}
        dataSource={applies}
        pagination={{ pageSize: 8, position: ["bottomCenter"] }}
        onRow={(record) => ({
          onClick: () => {
            setDashboardTitle(record.title);
            loadDetailData({ dashboardId: record.mediaId });
          },
        })}
      />
    </div>
  ) : (
    <DashBoardDetail
      dashboardTitle={dashboardTitle || detailProps.dashboardTitle}
      dashboardData={selectedData! || detailProps.dashboardData}
      dashboardId={currDashboardId! || detailProps.dashboardId}
    />
  );
};

export default DashBoard;
