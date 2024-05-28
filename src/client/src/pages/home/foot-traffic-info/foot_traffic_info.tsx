import footTraffic from "../../../assets/images/foot_traffic.jpg";
import guideImage1 from "../../../assets/images/guide_image_1.jpg";
import guideImage2 from "../../../assets/images/guide_image_2.jpg";
import guideImage3 from "../../../assets/images/guide_image_3.jpg";
import { FootTrafficAgeChart } from "./foot_traffic_age";
import { FootTrafficGenderChart } from "./foot_traffic_gender";

const FootTrafficInfo = () => {
  return (
    <div className="flex flex-col h-full min-w-[920px] px-[30px] overflow-y-scroll">
      <h1 className="font-medium text-xl">유동인구 정보 분석</h1>
      <p className="text-sm mt-4 mb-3">
        지하철 2호선 열차 내부 광고 집행시, 광고에 노출되는 사람들의 정보는
        다음과 같아요.
      </p>
      <div className="grid grid-cols-[1fr,1.3fr,1.3fr] gap-2">
        <div className="flex flex-col border-[1px] border-black/6 p-7 justify-between items-end rounded-md">
          <div className="w-full">
            <h2 className="text-base font-medium">하루 평균 유동인구수</h2>
            <p className="text-3xl font-medium mt-3">2,164,838</p>
          </div>
          <img width={156} src={footTraffic} />
        </div>
        <div className="flex flex-col justify-between border-[1px] border-black/6 p-7 h-[360px] rounded-md">
          <h2 className="text-base font-medium">유동인구 성비</h2>
          <div className="mt-10 flex-1">
            <FootTrafficGenderChart />
          </div>
        </div>
        <div className="flex flex-col justify-between border-[1px] border-black/6 p-7 h-[360px] rounded-md">
          <h2 className="text-base font-medium">유동인구 나이대</h2>
          <div className="mt-10 flex-1">
            <FootTrafficAgeChart />
          </div>
        </div>
      </div>

      <>
        <div className="flex gap-2 mt-[78px]">
          <h3 className="text-main text-base font-bold">Q1</h3>
          <h3 className="text-black text-base font-medium">
            유동인구 정보는 어떻게 수집되나요?
          </h3>
        </div>
        <div className="flex basis-0 w-full gap-20">
          <div className="mt-3 h-min w-1/2 ml-6 px-4 border-l-2 border-main">
            유동인구 정보는 지하철 2호선 출입문 위 디스플레이 상단에 설치된
            웹캠으로 수집한 데이터를 가공하여 제공됩니다.
            <br />
            <br />
            객체 추적 모델을 사용하여 광고 앞을 지나가는 유동인구를 탐지 및
            추적하고, 보행자 속성 인식 모델을 사용하여 그들의 성별과 연령대를
            추론합니다.
          </div>
          <div className="w-1/2 flex flex-col gap-3 items-center">
            <img src={guideImage1} />
            <p className="text-sm">
              광고 디스플레이 및 유동인구 탐지 가능 구역
            </p>
          </div>
        </div>
      </>

      <>
        <div className="flex gap-2 mt-[78px]">
          <h3 className="text-main text-base font-bold">Q2</h3>
          <h3 className="text-black text-base font-medium">
            광고 관심도 측정 기준이 뭔가요?
          </h3>
        </div>
        <div className="flex basis-0 w-full gap-20">
          <div className="mt-3 mb-14 h-min w-1/2 ml-6 px-4 border-l-2 border-main">
            광고 앞을 지나가는 유동인구 중 광고를 쳐다보는가를 판단하여 관심도를
            측정합니다.
          </div>
        </div>
        <div className="flex basis-0 gap-4 w-full">
          <div className="flex flex-col gap-3 items-center">
            <img src={guideImage2} />
            <p className="text-sm">관심 인구에 포함되지 않는 경우</p>
          </div>
          <div className="flex flex-col gap-3 items-center">
            <img src={guideImage3} />
            <p className="text-sm">관심 인구에 포함되는 경우</p>
          </div>
        </div>
      </>
    </div>
  );
};

export default FootTrafficInfo;
