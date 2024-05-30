import { DatePicker, Modal, Select, Table, TableColumnsType } from "antd";
import { Subtitle1, Subtitle2 } from "../../../components/text";
import {
  DashboardDataInfo,
  TotalApplicationInfo,
} from "../../../interfaces/interface";
import StatusBadge from "../../../components/status_badge";
import { useEffect, useState } from "react";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";
import dashboardIconWhite from "../../../assets/icons/view-dashboard-whitesub.svg";
import chartIconMain from "../../../assets/icons/chart-timeline-main.svg";
import { toast } from "react-hot-toast";
import defaultImageRectangle from "../../../assets/images/default_rectangle.svg";
import { getApplies } from "../../../api/client/apply";
import { getAdUnitDashboard } from "../../../api/client/dashboard";
import { DashBoardMode } from "../dashboard/dashboard";
import { InsightMode } from "../insight/insight";

dayjs.extend(customParseFormat);
const dateFormat = "YYYY-MM-DD";

export interface HistoryTableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  title: string;
  display: string;
  date: string[];
  status: string;
  origin: TotalApplicationInfo;
}

const HistoryScreen = ({
  setMenuIndex,
  setDashboardMode,
  setInsightMode,
  setDashboardDetailProps,
  setInsightDetailProps,
}: any) => {
  const columns: TableColumnsType<HistoryTableItem> = [
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

  const [historyData, setHistoryData] = useState<HistoryTableItem[]>([]);
  const [openModal, setOpenModal] = useState(false);
  const [selectedMedia, setSelectedMedia] =
    useState<TotalApplicationInfo | null>(null);

  const loadHistory = async () => {
    try {
      const result = await getApplies();

      if (result.status === 200) {
        setHistoryData(
          result.data.data.map((app: TotalApplicationInfo) => ({
            key: app.media.mediaId,
            mediaId: app.media.mediaId,
            mediaLink: app.media.mediaLink,
            title: app.media.title,
            display: app.application.location.address,
            date: [app.application.startDate, app.application.endDate],
            status: app.application.status,
            origin: app,
          }))
        );
      }
    } catch (error) {
      toast.error("히스토리 조회에 실패하였습니다.");
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  return (
    <>
      <div className="flex flex-col h-full min-w-[920px] px-[30px] gap-7 overflow-y-scroll">
        <Subtitle2 text="광고 목록" color="text-black" />
        <Table
          size="small"
          columns={columns}
          dataSource={historyData}
          pagination={{ pageSize: 8, position: ["bottomCenter"] }}
          onRow={(record) => ({
            onClick: () => {
              setSelectedMedia(record.origin);
              setOpenModal(true);
            },
          })}
        />
      </div>
      {selectedMedia ? (
        <Modal
          open={openModal}
          footer={false}
          onCancel={() => {
            setOpenModal(false);
            setSelectedMedia(null);
          }}
        >
          <div className="w-full p-12 flex flex-col">
            <Subtitle1 text="광고 타이틀" color="text-black" />
            <h3 className="px-2 py-3 border-2 mt-[10px] mb-4 border-[#d9d9d9] rounded-[2px]">
              {selectedMedia.media.title}
            </h3>
            <img
              className="w-full aspect-video object-cover rounded-[5px] mb-4"
              src={selectedMedia.media.mediaLink}
              alt="광고 썸네일"
              onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
                const target = e.target as HTMLImageElement;
                target.src = defaultImageRectangle;
              }}
            />
            <Subtitle1 text="광고 집행일" color="text-black" />
            <DatePicker.RangePicker
              style={{ pointerEvents: "none" }}
              defaultValue={[
                dayjs(selectedMedia.application.startDate, dateFormat),
                dayjs(selectedMedia.application.endDate, dateFormat),
              ]}
              className="mt-[10px] mb-4"
            />
            <Subtitle1 text="디스플레이" color="text-black" />
            <Select
              style={{ pointerEvents: "none" }}
              defaultValue={selectedMedia.application.location.address}
              className="mt-[10px] mb-4"
            />
            <Subtitle1 text="광고 상태" color="text-black" />
            <div className="mb-5 mt-2">
              <StatusBadge
                status={selectedMedia!.application.status}
                date={[
                  selectedMedia.application.startDate,
                  selectedMedia.application.endDate,
                ]}
              />
            </div>
            <div className="flex gap-2">
              <button
                className="flex gap-3 w-full justify-center items-center py-3 rounded-[3px] bg-main"
                onClick={async () => {
                  if (dayjs() < dayjs(selectedMedia.application.startDate)) {
                    toast.error(
                      "해당 광고는 집행 예정으로 대시보드가 생성되지 않았습니다."
                    );
                    return;
                  }
                  const result = await getAdUnitDashboard({
                    dashboardId: selectedMedia.media.mediaId,
                  });
                  if (result.status === 200) {
                    setDashboardDetailProps({
                      dashboardTitle: selectedMedia.media.title,
                      dashboardData: result.data.data as DashboardDataInfo,
                      dashboardId: selectedMedia.media.mediaId,
                    });
                    setDashboardMode(DashBoardMode.DETAIL);
                    setMenuIndex(0);
                    setOpenModal(false);
                  } else {
                    toast.error("현재 대시보드에 접근할 수 없습니다.");
                  }
                }}
              >
                <img src={dashboardIconWhite} />
                <p className="text-white">대시보드</p>
              </button>
              <button
                className="flex gap-3 w-full justify-center border-[1px] items-center border-main py-3 rounded-[3px]"
                onClick={() => {
                  setInsightDetailProps(selectedMedia.media);
                  setInsightMode(InsightMode.DETAIL);
                  setMenuIndex(1);
                  setOpenModal(false);
                }}
              >
                <img src={chartIconMain} />
                <p className="text-main">인사이트</p>
              </button>
            </div>
          </div>
        </Modal>
      ) : (
        <></>
      )}
    </>
  );
};

export default HistoryScreen;
