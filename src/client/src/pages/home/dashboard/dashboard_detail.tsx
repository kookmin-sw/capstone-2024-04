import { Select } from "antd";
import { DashboardDataInfo } from "../../../interfaces/interface";
import ApexCharts from "apexcharts";
import { InterestPeopleChart } from "./pie";

const DashBoardDetail = () => {
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
    maleInterestCnt: 150,
    femaleInterestCnt: 104,
    maleCnt: 200,
  };

  return (
    <div className="min-w-[920px] w-full h-full overflow-y-scroll flex flex-col">
      <div className="flex justify-between items-start">
        <div className="flex flex-col gap-4">
          <h1 className="text-2xl font-medium text-black text-ellipsis">
            치킨만 포함된 치킨광고 대시보드
          </h1>
          <h2 className=" text-base font-normal text-black text-ellipsis">
            연예인이 포함된 치킨광고와 치킨만 포함된 광고 A/B 테스트
          </h2>
          <Select />
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
          src=""
        />
      </div>
      <div className="flex flex-col gap-6">
        <div className="flex justify-between">
          <p className="text-base">광고 관심도 분석 결과</p>
          <Select />
        </div>
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
          <div className="flex flex-col w-[360px] px-7 py-5 border-[1px] border-black/0.06 rounded gap-2">
            <p className="text-base font-medium">관심인구 성비</p>
            <p className="text-sm text-[#6b6b6b]">
              광고에 관심을 보인 사람의 성비를 분석했어요.
            </p>
            <InterestPeopleChart
              interestPeopleCount={[
                dummy.maleInterestCnt,
                dummy.femaleInterestCnt,
              ]}
            />
          </div>
          <div className="flex flex-col flex-1 px-7 py-5 border-[1px] border-black/0.06 rounded"></div>
        </div>
        <div className="px-7 py-5 border-[1px] border-black/0.06 rounded w-full"></div>
      </div>
    </div>
  );
};

export default DashBoardDetail;
