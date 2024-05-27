import { DatePicker, Select } from "antd";
import { DashboardDataInfo } from "../../../interfaces/interface";
import { InterestPeopleChart } from "./pie";
import { InterestBar } from "./interestBar";
import { TotalBar } from "./totalBar";
import MixedChart from "./mixedChart";
import defaultImageVideo from "../../../assets/images/default_video.svg";
import { useEffect, useState } from "react";
import {
  createDailyDashboard,
  createLocationDashboard,
  getDashboardListByAdUnit,
} from "../../../api/client/dashboard";
import { SelectProps } from "antd/lib";
import dayjs from "dayjs";

export interface DashboardDetailProps {
  dashboardData: DashboardDataInfo;
  dashboardTitle: string;
  dashboardId: number;
}

export interface DashboardSelectInfo {
  address: string;
  description: null | string;
  mediaApplicationId: number;
  locationId: number;
  startDate: string;
  endDate: string;
}

const DashBoardDetail = ({
  dashboardTitle,
  dashboardData,
  dashboardId,
}: DashboardDetailProps) => {
  const [data, setData] = useState<DashboardDataInfo>(dashboardData);
  const [date, setDate] = useState<string[]>([]); // 신청 단위의 시작 일자, 종료 일자가 담긴 리스트
  const [selectedDate, setSelectedDate] = useState<string | null>(null); // 일별 조회 시
  const [description, setDescription] = useState<string | null>(null);
  const [options, setOptions] = useState<SelectProps["options"]>([]);
  const [mediaApplicationId, setMediaApplicationId] = useState<number | null>(
    null
  );
  const [locationId, setLocationId] = useState<number | null>(null);

  console.log(data);

  const loadDashboardList = async () => {
    const result = await getDashboardListByAdUnit({ dashboardId: dashboardId });
    if (result.status === 200) {
      const today = new Date();
      const newOptions = result.data.data
        .filter(
          (dashboard: DashboardSelectInfo) =>
            new Date(dashboard.startDate) <= today
        )
        .map((dashboard: DashboardSelectInfo) => ({
          label: `${dashboard.address} (${dashboard.startDate}~${dashboard.endDate})`,
          value: dashboard.mediaApplicationId,
          locationId: dashboard.locationId,
          description: dashboard.description,
          startDate: dashboard.startDate,
          endDate: dashboard.endDate,
        }));
      setOptions(newOptions);
    }
  };

  const loadDashboardWithLocation = async () => {
    if (locationId) {
      const result = await createLocationDashboard({ locationId: locationId });
      if (result.status === 200) {
        setData(result.data.data);
      }
    }
  };

  useEffect(() => {
    loadDashboardWithDate();
  }, [selectedDate]);

  const loadDashboardWithDate = async () => {
    if (selectedDate && mediaApplicationId) {
      const result = await createDailyDashboard({
        dashboardId: dashboardId,
        mediaApplicationId: mediaApplicationId,
        date: selectedDate,
      });
      if (result.status === 200) {
        setDate(result.data.data);
      }
    }
  };

  useEffect(() => {
    console.log("대시보드 초기 데이터 수신");
    setData(dashboardData);
    loadDashboardList();
  }, [dashboardData, dashboardId]);

  useEffect(() => {
    setSelectedDate(null);
    loadDashboardWithLocation();
  }, [locationId]);

  const dummy: DashboardDataInfo = {
    mediaAppsCnt: 25,
    hourlyInterestedCount: [
      20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20, 31, 50, 20, 30, 6, 20,
      31, 50, 20, 30, 6,
    ],
    hourlyPassedCount: [
      15, 28, 45, 20, 25, 7, 19, 30, 48, 18, 27, 5, 20, 31, 50, 20, 30, 6, 20,
      31, 50, 20, 30, 6,
    ],
    hourlyAvgStaringTime: [
      2.7, 3.6, 3.3, 9.2, 1.1, 1.2, 2.7, 3.6, 3.3, 9.2, 1.1, 1.2, 2.7, 3.6, 3.3,
      9.2, 1.1, 1.2, 2.7, 3.6, 3.3, 9.2, 1.1, 1.2,
    ],
    totalPeopleCount: 254,
    avgStaringTime: 3.1,
    avgAge: 27.2,
    maleInterestCnt: 102,
    femaleInterestCnt: 72,
    maleCnt: 200,
  };

  return (
    <div className="min-w-[920px] w-full h-full overflow-y-scroll flex flex-col">
      <div className="flex justify-between items-start">
        <div className="flex flex-col gap-4">
          <h1 className="text-2xl font-medium text-black text-ellipsis">
            {`${dashboardTitle} 대시보드`}
          </h1>
          <h2 className="text-base h-5 font-normal text-black text-ellipsis ">
            {description}
          </h2>
          <Select
            placeholder={"상세보기를 원하는 광고를 선택해주세요."}
            options={options}
            onSelect={(_, record) => {
              setDescription(record.description);
              setLocationId(record.locationId as number);
              setMediaApplicationId(record.value as number);
              setDate([record.startDate, record.endDate]);
            }}
          />
          <button
            className="w-min text-nowrap p-4 border-[1px] border-main rounded-[3px] bg-white text-main"
            type="button"
          >
            인사이트 바로가기
          </button>
        </div>
        <img
          className="border-[1px] rounded h-full aspect-video"
          alt="광고 썸네일"
          onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
            const target = e.target as HTMLImageElement;
            target.src = defaultImageVideo;
          }}
          src=""
        />
      </div>
      <div className="flex justify-between mt-10 mb-3">
        <p className="text-base">광고 관심도 분석 결과</p>
        <DatePicker
          minDate={dayjs(date[0])}
          maxDate={dayjs(date[1])}
          disabled={date.length === 0}
          onChange={(_, dateString) => {
            setSelectedDate(dateString as string);
          }}
        />
      </div>
      <div className="flex flex-col gap-6">
        <div className="grid grid-cols-4 gap-6">
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">총 유동인구수</p>
            <p className="my-4 text-center font-light text-[26px]">
              {dummy.totalPeopleCount.toLocaleString("ko-KR")}명
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">관심 인구수</p>
            <p className="my-4 text-center font-light text-[26px]">
              {(dummy.maleInterestCnt + dummy.femaleInterestCnt).toLocaleString(
                "ko-KR"
              )}
              명
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">광고 관심도</p>
            <p className="my-4 text-center font-light text-[26px]">
              {(
                ((dummy.maleInterestCnt + dummy.femaleInterestCnt) /
                  dummy.totalPeopleCount) *
                100
              ).toFixed(1)}
              %
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">시선 고정시간</p>
            <p className="my-4 text-center font-light text-[26px]">
              {dummy.avgStaringTime}초
            </p>
          </div>
        </div>
        <div className="flex gap-6">
          <div className="flex flex-col w-[360px]  px-7 py-5 border-[1px] border-black/0.06 rounded gap-2">
            <p className="text-base font-medium">관심인구 성비</p>
            <p className="text-sm text-[#6b6b6b]">
              광고에 관심을 보인 사람의 성비를 분석했어요.
            </p>
            <div className="flex h-full justify-center items-center">
              <InterestPeopleChart
                interestPeopleCount={[
                  dummy.maleInterestCnt,
                  dummy.femaleInterestCnt,
                ]}
              />
            </div>
          </div>
          <div className="flex flex-col flex-1 px-7 py-5 border-[1px] border-black/0.06 rounded gap-2">
            <p className="text-base font-medium">관심인구 나이대</p>
            <p className="text-sm text-[#6b6b6b]">
              광고에 관심을 보인 사람의 나이대를 분석했어요.
            </p>
            <InterestBar
              interestAge={[
                { name: "10대", data: [10] },
                { name: "20대", data: [13] },
              ]}
            />
            <p className="text-base font-medium">전체 유동인구 나이대</p>
            <p className="text-sm text-[#6b6b6b]">
              광고 디스플레이 앞을 지나간 모든 사람의 나이대는 다음과 같아요.
            </p>
            <TotalBar
              totalAge={[
                { name: "10대", data: [10] },
                { name: "20대", data: [13] },
                { name: "30대", data: [1] },
                { name: "40대", data: [19] },
              ]}
            />
          </div>
        </div>
        <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded w-full gap-2">
          <p className="text-base font-medium">
            시간대별 광고 관심도 및 유동인구 추이
          </p>
          <p className="text-sm text-[#6b6b6b]">
            광고 관심도는 디스플레이 앞을 지나간 모든 사람 중 관심을 가진 사람이
            얼마나 많았는지를 알려주는 지표예요.
          </p>
          <MixedChart
            total={[
              6, 7, 8, 12, 10, 11, 0, 1, 20, 32, 13, 25, 16, 8, 13, 9, 10, 11,
            ]}
            interest={[
              3, 3, 4, 3, 7, 5, 0, 0, 15, 23, 10, 20, 9, 6, 8, 7, 6, 7,
            ]}
          />
        </div>
      </div>
    </div>
  );
};

export default DashBoardDetail;
