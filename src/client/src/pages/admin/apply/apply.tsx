import { useQuery } from "react-query";
import { ApplyStatus, getApply, patchApply } from "../../../api/admin/apply";
import { Subtitle2 } from "../../../components/text";
import { Table, TableColumnsType } from "antd";
import { Key, useState } from "react";
import { toast } from "react-hot-toast";

interface ApplyTableItem {
  key: number;
  mediaId: number;
  mediaLink: string;
  title: string;
  display: string;
  date: string[];
}

const AdminApplyPage = () => {
  const { data, isSuccess, isError, refetch } = useQuery(
    "admin-get-apply",
    () => getApply()
  );

  const [selectedKeys, setSelectedKeys] = useState<number[]>([]);

  const acceptApply = async (keys: number[]) => {
    const result = await patchApply({
      applyId: keys,
      status: ApplyStatus.ACCEPT,
    });

    if (result && result.status === 200) {
      toast.success("광고 신청 승인에 성공하였습니다.");
    } else {
      toast.error("광고 신청 승인에 실패하였습니다.");
    }
  };

  const rejectApply = async (keys: number[]) => {
    const result = await patchApply({
      applyId: keys,
      status: ApplyStatus.REJECT,
    });

    if (result && result.status === 200) {
      toast.success("광고 신청 거절에 성공하였습니다.");
    } else {
      toast.error("광고 신청 거절에 실패하였습니다.");
    }
  };

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
      title: "광고 등록",
      dataIndex: "key",
      render: (key) => (
        <div className="flex gap-2 items-center text-black/[0.06]">
          <p
            className="cursor-pointer text-[#999999] text-sm"
            onClick={() => rejectApply([key])}
          >
            거절
          </p>
          |
          <p
            className="cursor-pointer text-main text-sm"
            onClick={() => acceptApply([key])}
          >
            승인
          </p>
        </div>
      ),
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

  const rowSelection = {
    selectedKeys,
    onChange: (keys: Key[]) => {
      setSelectedKeys(keys as number[]);
      console.log(keys);
    },
  };

  if (isSuccess && typeof data === "undefined")
    return (
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
          dataSource={tableData}
          pagination={{ pageSize: 8, position: ["bottomCenter"] }}
        />
      </div>
    );

  if (isError || typeof data === "undefined")
    return (
      <div className="flex w-full min-w-[920px] h-full justify-center items-center">
        <button type="button" onClick={() => refetch()}>
          다시 시도하기
        </button>
      </div>
    );
};

export default AdminApplyPage;
