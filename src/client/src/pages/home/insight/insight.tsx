import { useEffect, useState } from "react";
import { Subtitle2 } from "../../../components/text";
import GridViewMedia from "../gridview-media/gridview_media";
import { DashboardDataInfo, MediaInfo } from "../../../interfaces/interface";
import { getMedia } from "../../../api/client/media";
import defaultImageVideo from "../../../assets/images/default_video.svg";
import { TargetInterestChart } from "./target_interest";
import { TotalBar } from "../dashboard/totalBar";
import { InterestPeopleChart } from "../dashboard/pie";
import { Modal } from "antd";
import { toast } from "react-hot-toast";
import { getFilteredDashboardWithAgeAndGender } from "../../../api/client/dashboard";

export enum InsightMode {
  LIST,
  DETAIL,
}

interface UpdateAgeRangesWithIndexProps {
  index: number;
  status: boolean;
}

interface FilteredInfo {
  totalPepleCount: number;
  avgStaringTime: number;
  attentionRatio: number;
  interestPeopleCnt: number;
}

const Insight = ({ mode, setMode }: any) => {
  const [detailInfo, setDetailInfo] = useState<MediaInfo | null>(null);
  const [medias, setMedias] = useState<MediaInfo[]>([]);
  const [openModal, setOpenModal] = useState(false);
  const [male, setMale] = useState<boolean>(false);
  const [female, setFemale] = useState<boolean>(false);
  const [ageRanges, setAgeRanges] = useState<boolean[]>(Array(6).fill(false));
  const [ageRangesCount, setAgeRangesCount] = useState<number>(0);
  const [filteredData, setFilteredData] = useState<FilteredInfo | null>(null);

  const updateAgeRangesWithIndex = ({
    index,
    status,
  }: UpdateAgeRangesWithIndexProps) => {
    const newAgeRanges = [...ageRanges];

    if (status && ageRangesCount === 2) {
      toast.error("나이대는 최대 2개까지 선택 가능합니다.");
      return false;
    }

    status
      ? setAgeRangesCount(ageRangesCount + 1)
      : setAgeRangesCount(ageRangesCount - 1);

    newAgeRanges[index] = status;
    setAgeRanges(newAgeRanges);

    console.log(ageRanges);
  };

  useEffect(() => {
    loadInsightList();
  }, []);

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

  const loadInsightList = async () => {
    const response = await getMedia({ filter: "ACCEPT" });

    if (response.status === 200) {
      console.log(response);
      setMedias(response.data.data);
    }
  };

  return mode === InsightMode.LIST ? (
    <div className="flex flex-col h-full min-w-[920px] w-full px-[30px] gap-7 overflow-y-scroll">
      <Subtitle2 text="광고 목록" color="text-black" />
      <GridViewMedia
        mediaList={medias}
        setMode={setMode}
        setDetailInfo={setDetailInfo}
      />
    </div>
  ) : (
    <div className="flex flex-col h-full min-w-[920px] w-full py-[30px] overflow-y-scroll">
      <div className="flex w-full justify-between items-center">
        <div className="flex flex-col gap-4 px-[30px]">
          <h1 className="text-2xl font-medium">{`${detailInfo?.title} 인사이트`}</h1>
          <h2 className="text-base font-normal">{detailInfo!.description}</h2>
          <button
            className="border-[1px] border-main rounded-[3px] text-main text-sm px-4 py-3"
            type="button"
          >
            대시보드 바로가기
          </button>
        </div>
        <img
          src=""
          className="h-32 aspect-video"
          alt="광고 썸네일"
          onError={(e: React.SyntheticEvent<HTMLImageElement, Event>) => {
            const target = e.target as HTMLImageElement;
            target.src = defaultImageVideo;
          }}
        />
        <Modal
          open={openModal}
          footer={false}
          onCancel={() => setOpenModal(false)}
        >
          <div className="flex flex-col w-full p-15">
            <h3 className="text-base mb-5">광고 타겟층 설정</h3>
            <h3 className="text-base">성별</h3>
            <div className="grid grid-cols-3 gap-2">
              <div
                onClick={() => setMale(!male)}
                className={`text-center border-[1px]
                male ? "border-main text-main" : ""
                col-span-1 rounded py-4 px-7`}
              >
                남자
              </div>
              <div
                onClick={() => setFemale(!female)}
                className={`text-center border-[1px] ${
                  female ? "border-main text-main" : ""
                } col-span-1 rounded py-4 px-7`}
              >
                여자
              </div>
            </div>
            <h3 className="text-base mt-11">나이대</h3>
            <div className="grid grid-cols-3 gap-2">
              {Array.from({ length: 6 }).map((_, index) => {
                return (
                  <div
                    key={index}
                    onClick={() =>
                      updateAgeRangesWithIndex({
                        index,
                        status: !ageRanges[index],
                      })
                    }
                    className={`${
                      ageRanges[index] ? "border-main text-main" : ""
                    } text-center border-[1px] col-span-1 rounded py-4 px-7 cursor-pointer`}
                  >
                    {index === 5 ? `60대 이상` : `${index + 1}0대`}
                  </div>
                );
              })}
            </div>
            <div className="flex">
              <button
                type="button"
                className="mt-[72px] bg-main text-white py-3 px-10 rounded"
                onClick={async () => {
                  if (detailInfo) {
                    const result = await getFilteredDashboardWithAgeAndGender({
                      dashboardId: detailInfo?.dashboardId,
                      male,
                      female,
                      ageRanges,
                    });

                    if (result.status === 200) {
                      setFilteredData(result.data.data);
                    }
                  }
                }}
              >
                설정하기
              </button>
            </div>
          </div>
        </Modal>
      </div>
      <h2 className="text-xl font-medium px-[30px] pt-4 pb-2">
        광고 타겟층 분석
      </h2>
      <div className="relative px-[30px] py-4">
        <div className="flex">
          <p className="text-base">해당 광고의 타겟층은 </p>
          <p className="text-base text-main font-semibold">
            10대 남자 10대 여자
          </p>
          <p className="text-base">입니다.</p>
        </div>
        <div className="grid grid-rows-4 grid-cols-4 grid-flow-col gap-4">
          <div className="flex flex-col p-6 gap-3 border-[1px] row-span-2 col-span-2 rounded">
            <h3 className="text-base font-medium">전체 타겟층의 수</h3>
            <p className="text-[40px] font-light">
              {`${filteredData ? filteredData.totalPepleCount : 0}명`}
            </p>
            <p className="text-[#6b6b6b] text-xs">
              해당 광고 앞을 지나간 사람이 광고 타겟층인 경우
            </p>
          </div>
          <div className="flex flex-col p-6 gap-3 border-[1px] row-span-2 col-span-1 rounded">
            <h3 className="text-base font-medium">관심을 가진 타겟층의 수</h3>
            <p className="text-[40px] font-light">
              {`${filteredData ? filteredData.interestPeopleCnt : 0}명`}
            </p>
            <p className="text-[#6b6b6b] text-xs">
              해당 광고를 본 사람이 광고 타겟층인 경우
            </p>
          </div>
          <div className="flex flex-col p-6 gap-3 border-[1px] row-span-2 col-span-1 rounded">
            <h3 className="text-base font-medium">타겟층의 시선 고정 시간</h3>
            <p className="text-[40px] font-light">
              {`${filteredData ? filteredData.avgStaringTime : 0}초`}
            </p>
            <p className="text-[#6b6b6b] text-xs">
              평균적으로 광고에 시선을 고정한 시간
            </p>
          </div>
          <div className="flex flex-col p-6 border-[1px] row-span-4 col-span-2 rounded">
            <h3 className="text-base font-medium">타겟층의 광고 관심도</h3>
            <p className="text-[40px]">
              {`${filteredData ? filteredData.attentionRatio : 0}%`}
            </p>
            <TargetInterestChart
              series={
                filteredData
                  ? [
                      filteredData?.interestPeopleCnt,
                      filteredData?.totalPepleCount -
                        filteredData?.interestPeopleCnt,
                    ]
                  : [10, 30]
              }
            />
          </div>
        </div>
        <div
          style={{ backdropFilter: "blur(10px)" }}
          className={`flex flex-col justify-center items-center absolute inset-0 z-10 w-full h-full gap-5 ${
            filteredData ? "hidden" : ""
          }`}
        >
          <p className="text-sm">
            해당 광고의 타겟층을 설정하면
            <br />
            인사이트를 확인할 수 있습니다.
          </p>
          <button
            className="py-5 px-[60px] bg-main text-white text-sm font-medium"
            type="button"
            onClick={() => setOpenModal(true)}
          >
            광고 타겟층 설정하기
          </button>
        </div>
      </div>

      <div className="px-[30px] gap-6">
        <h2 className="text-xl font-medium">전체 기간 유동인구 분석</h2>
        <p className="mt-2 mb-7">
          해당 광고를 집행하는 기간동안 집계된 전체 유동인구 데이터를 기반으로
          분석한 결과예요.
        </p>
        <div className="flex gap-6">
          <div className="flex flex-col w-[360px] px-7 py-5 border-[1px] border-black/0.06 rounded gap-2">
            <p className="text-base font-medium">유동인구 성비</p>
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
            <p className="text-base font-medium">유동인구 나이대</p>
            <p className="text-sm text-[#6b6b6b]">
              광고에 관심을 보인 사람의 나이대를 분석했어요.
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
        <div className="grid grid-cols-4 gap-6 mt-6">
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
      </div>
    </div>
  );
};

export default Insight;
