import { getApply, patchApply } from "../../../api/admin/apply";
import { Subtitle1, Subtitle2 } from "../../../components/text";
import { DatePicker, Input, Select, Table, TableColumnsType } from "antd";
import { Key, useEffect, useState } from "react";
import { toast } from "react-hot-toast";
import dayjs from "dayjs";
import { TotalApplicationInfo } from "../../../interfaces/interface";
import defaultImageRectangle from "../../../assets/images/default_rectangle.svg";
import defaultImageVideo from "../../../assets/images/default_video.svg";

interface ApplyTableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  title: string;
  display: string;
  date: string[];
  origin: TotalApplicationInfo;
}

const AdminApplyDetail = ({
  application,
}: {
  application: TotalApplicationInfo;
}) => {
  return (
    <div className="flex h-full min-w-[920px]">
      <div className="flex-1 flex flex-col px-[30px] h-full justify-between">
        <div>
          <Subtitle1 text="광고 썸네일" color="text-black" />

          <img
            className="w-full aspect-video bg-black mt-3"
            src={application.media.mediaLink}
            onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
              const target = e.target as HTMLImageElement;
              target.src = defaultImageVideo;
            }}
          />
        </div>

        <div className="flex gap-3">
          <button
            type="button"
            className="px-4 py-3 text-white bg-main text-sm rounded-[3px]"
            onClick={() => {}}
          >
            광고 승인하기
          </button>
          <button
            type="button"
            className="px-4 py-3 text-black bg-[#e7e7e7] text-sm rounded-[3px]"
            onClick={() => {}}
          >
            거절하기
          </button>
        </div>
      </div>
      <div className="flex-1 flex-col px-[30px]">
        <Subtitle1 text="광고 등록일" color="text-black" />
        <DatePicker.RangePicker
          defaultValue={[
            dayjs(application.application.startDate, "YYYY-MM-DD"),
            dayjs(application.application.endDate, "YYYY-MM-DD"),
          ]}
          className="mt-2 mb-7"
          style={{ width: "100%", pointerEvents: "none" }}
        />
        <Subtitle1 text="디스플레이 선택" color="text-black" />
        <Select
          className="mt-2 mb-7"
          style={{ width: "100%", pointerEvents: "none" }}
          defaultValue={application.application.location.address}
        />

        <div className="flex flex-col mt-4">
          <Subtitle1 text="광고 타이틀" color="text-black" />
          <Input
            style={{ pointerEvents: "none" }}
            className="mt-2 mb-10"
            defaultValue={application.media.title}
          />
          <Subtitle1 text="광고 설명" color="text-black" />
          <Input.TextArea
            className="mt-2"
            style={{ resize: "none", pointerEvents: "none" }}
            rows={5}
            defaultValue={application.media.description}
          />
        </div>
      </div>
    </div>
  );
};

const AdminApplyPage = () => {
  const loadApplyList = async () => {
    const result = await getApply();
    const data: TotalApplicationInfo[] = result.data.data.filter(
      (item: TotalApplicationInfo) => item.application.status === "WAITING"
    );
    console.log(data);
    setDataSource(
      data.map((item: TotalApplicationInfo) => ({
        key: item.application.applicationId,
        mediaId: item.media.mediaId,
        mediaLink: item.media.mediaLink,
        title: item.media.title,
        display: item.application.location.address,
        date: [item.application.startDate, item.application.endDate],
        origin: item,
      }))
    );
  };

  useEffect(() => {
    loadApplyList();
  }, []);

  const [dataSource, setDataSource] = useState<ApplyTableItem[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<number[]>([]);
  const [currApplication, setCurrApplication] =
    useState<TotalApplicationInfo | null>(null);

  const acceptApply = async (keys: number[]) => {
    const result = await patchApply({
      applyId: keys,
      status: "ACCEPT",
    });

    if (result && result.status === 200) {
      toast.success("광고 신청 승인에 성공하였습니다.");
      setDataSource(dataSource.filter((item) => !keys.includes(item.key)));
    } else {
      toast.error("광고 신청 승인에 실패하였습니다.");
    }
  };

  const rejectApply = async (keys: number[]) => {
    const result = await patchApply({
      applyId: keys,
      status: "REJECT",
    });

    if (result && result.status === 200) {
      toast.success("광고 신청 거절에 성공하였습니다.");
      setDataSource(dataSource.filter((item) => !keys.includes(item.key)));
    } else {
      toast.error("광고 신청 거절에 실패하였습니다.");
    }
  };

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
      title: "광고 등록",
      dataIndex: "key",
      render: (key) => (
        <div className="flex gap-2 items-center text-black/[0.06]">
          <p
            className="cursor-pointer text-[#999999] text-sm"
            onClick={(e) => {
              e.stopPropagation();
              rejectApply([key]);
            }}
          >
            거절
          </p>
          |
          <p
            className="cursor-pointer text-main text-sm"
            onClick={(e) => {
              e.stopPropagation();
              acceptApply([key]);
            }}
          >
            승인
          </p>
        </div>
      ),
    },
  ];

  const rowSelection = {
    selectedKeys,
    onChange: (keys: Key[]) => {
      setSelectedKeys(keys as number[]);
      console.log(keys);
    },
  };

  return currApplication === null ? (
    <div className="flex flex-col min-w-[920px] h-full px-[30px] gap-7 overflow-y-scroll">
      <div className="flex justify-between">
        <Subtitle2 text="승인 대기 중인 목록" color="text-black" />
        <div className="flex gap-4">
          <button
            className="px-4 py-2 border-[1px] border-[#d9d9d9] rounded-sm"
            type="button"
            disabled={selectedKeys.length === 0}
            onClick={() => rejectApply(selectedKeys)}
          >
            거절하기
          </button>
          <button
            className="px-4 py-2 border-[1px] bg-main text-white border-main rounded-sm"
            type="button"
            disabled={selectedKeys.length === 0}
            onClick={() => acceptApply(selectedKeys)}
          >
            승인하기
          </button>
        </div>
      </div>
      <Table
        size="small"
        rowSelection={rowSelection}
        columns={columns}
        dataSource={dataSource}
        pagination={{ pageSize: 8, position: ["bottomCenter"] }}
        onRow={(record) => ({
          onClick: () => {
            setCurrApplication(record.origin);
          },
        })}
      />
    </div>
  ) : (
    <AdminApplyDetail application={currApplication} />
  );
};

export default AdminApplyPage;
