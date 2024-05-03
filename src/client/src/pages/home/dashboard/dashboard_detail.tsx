import { Select } from "antd";

const DashBoardDetail = () => {
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
        <img alt="광고 썸네일" src="" />
      </div>
      <div className="flex flex-col">
        <div className="flex justify-between">
          <p className="text-base">광고 관심도 분석 결과</p>
          <Select />
        </div>
        <div className="grid grid-cols-4">{/* 총 유동인구수 라인 */}</div>
        <div></div>
      </div>
    </div>
  );
};

export default DashBoardDetail;
