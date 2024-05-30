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
  // createLocationDashboard,
  getAdUnitDashboard,
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

  // const loadDashboardWithLocation = async () => {
  //   if (locationId) {
  //     const result = await createLocationDashboard({ locationId: locationId });
  //     if (result.status === 200) {
  //       setData(result.data.data);
  //     }
  //   }
  // };

  const loadDashboard = async () => {
    if (dashboardId) {
      const result = await getAdUnitDashboard({ dashboardId: dashboardId });
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
        setData(result.data.data);
      }
    }
  };

  useEffect(() => {
    setData(dashboardData);
    loadDashboardList();
  }, [dashboardData, dashboardId]);

  useEffect(() => {
    setSelectedDate(null);
    // loadDashboardWithLocation();
    loadDashboard();
  }, [locationId]);

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
          maxDate={dayjs(date[1]) < dayjs() ? dayjs(date[1]) : dayjs()}
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
              {data.totalPeopleCount
                ? data.totalPeopleCount.toLocaleString("ko-KR")
                : 0}
              명
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">관심 인구수</p>
            <p className="my-4 text-center font-light text-[26px]">
              {(
                (data.maleInterestCnt ? data.maleInterestCnt : 0) +
                (data.femaleInterestCnt ? data.femaleInterestCnt : 0)
              ).toLocaleString("ko-KR")}
              명
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">광고 관심도</p>
            <p className="my-4 text-center font-light text-[26px]">
              {data.totalPeopleCount === null || data.totalPeopleCount === 0
                ? "0.0"
                : (
                    (((data.maleInterestCnt ? data.maleInterestCnt : 0) +
                      (data.femaleInterestCnt ? data.femaleInterestCnt : 0)) /
                      data.totalPeopleCount) *
                    100
                  ).toFixed(1)}
              %
            </p>
          </div>
          <div className="flex flex-col px-7 py-5 border-[1px] border-black/0.06 rounded">
            <p className="text-base font-medium">시선 고정시간</p>
            <p className="my-4 text-center font-light text-[26px]">
              {data.avgStaringTime}초
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
                  data.maleInterestCnt,
                  data.femaleInterestCnt,
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
              interestAge={
                data.interestedPeopleAgeRangeCount !== null
                  ? [
                      {
                        name: "10대",
                        data: [data.interestedPeopleAgeRangeCount[0]],
                      },
                      {
                        name: "20대",
                        data: [data.interestedPeopleAgeRangeCount[1]],
                      },
                      {
                        name: "30대",
                        data: [data.interestedPeopleAgeRangeCount[2]],
                      },
                      {
                        name: "40대",
                        data: [data.interestedPeopleAgeRangeCount[3]],
                      },
                      {
                        name: "50대",
                        data: [data.interestedPeopleAgeRangeCount[4]],
                      },
                      {
                        name: "60대 이상",
                        data: [
                          data.interestedPeopleAgeRangeCount[5] +
                            data.interestedPeopleAgeRangeCount[6] +
                            data.interestedPeopleAgeRangeCount[7] +
                            data.interestedPeopleAgeRangeCount[8],
                        ],
                      },
                    ]
                  : []
              }
            />
            <p className="text-base font-medium">전체 유동인구 나이대</p>
            <p className="text-sm text-[#6b6b6b]">
              광고 디스플레이 앞을 지나간 모든 사람의 나이대는 다음과 같아요.
            </p>
            <TotalBar
              totalAge={
                data.totalPeopleAgeRangeCount
                  ? [
                      {
                        name: "10대",
                        data: [data.totalPeopleAgeRangeCount[0]],
                      },
                      {
                        name: "20대",
                        data: [data.totalPeopleAgeRangeCount[1]],
                      },
                      {
                        name: "30대",
                        data: [data.totalPeopleAgeRangeCount[2]],
                      },
                      {
                        name: "40대",
                        data: [data.totalPeopleAgeRangeCount[3]],
                      },
                      {
                        name: "50대",
                        data: [data.totalPeopleAgeRangeCount[4]],
                      },
                      {
                        name: "60대 이상",
                        data: [
                          data.totalPeopleAgeRangeCount[5] +
                            data.totalPeopleAgeRangeCount[6] +
                            data.totalPeopleAgeRangeCount[7] +
                            data.totalPeopleAgeRangeCount[8],
                        ],
                      },
                    ]
                  : []
              }
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
            total={data.hourlyPassedCount ? data.hourlyPassedCount : []}
            interest={
              data.hourlyPassedCount !== null &&
              data.hourlyInterestedCount !== null
                ? data.hourlyPassedCount.map((passed, index) => {
                    const interested = data.hourlyInterestedCount![index];
                    return passed === 0 ? 0 : ((interested / passed) * 100) | 0; // float가 아닌 int 형태
                  })
                : []
            }
          />
        </div>
      </div>
    </div>
  );
};

export default DashBoardDetail;
