import walk from "../../../assets/icons/walk.svg";
import accountMultiple from "../../../assets/icons/account-multiple.svg";
import exclamationThick from "../../../assets/icons/exclamation-thick.svg";
import gender from "../../../assets/icons/gender.svg";
import eye from "../../../assets/icons/eye-outline.svg";
import focus from "../../../assets/icons/bullseye-arrow.svg";
import attentionPeople from "../../../assets/icons/Vector.svg";
import { Body1, Subtitle2 } from "../../../components/text";
import { Cascader } from "antd";
import { ContentProps, Option } from "../../../interfaces/interface";

const Content = ({ label, value }: ContentProps) => {
  return (
    <div className="flex w-full">
      <div className="flex items-center w-[200px]">{label}</div>
      <div className="flex-1 min-w-[720px]">{value}</div>
    </div>
  );
};

const CompareMediaScreen = () => {
  const infos = [
    { info1: 34305, info2: 37, info3: [43, 57], info4: 2.8 },
    { info1: 22304, info2: 16, info3: [18, 82], info4: 3.2 },
    { info1: 2121 },
  ];

  // 광고 선택 관련
  /** 삭제 예정의 더미 데이터입니다 */
  const options: Option[] = [
    {
      value: "치킨만 포함된 광고",
      label: "치킨만 포함된 광고",
      children: [
        {
          value: "국민대학교 정문 정류장",
          label: "국민대학교 정문 정류장\n24.03.28~24.03.31",
        },
        {
          value: "국민대학교 미래관 4층",
          label: "국민대학교 미래관 4층\n24.03.29~24.03.30",
        },
      ],
    },
    {
      value: "연예인이 포함된 광고",
      label: "연예인이 포함된 광고",
    },
    {
      value: "다른 타이틀이 있는 광고",
      label: "다른 타이틀이 있는 광고",
    },
  ];

  return (
    <div className="h-full flex flex-col divide-y px-[30px] overflow-y-scroll">
      <Content
        label={<></>}
        value={
          <div className="grid grid-cols-3 gap-x-[30px]">
            {infos.map(() => {
              return (
                <div className="flex flex-col gap-4">
                  <div className="flex justify-center items-center aspect-video border-[1px] border-white_sub rounded-md" />
                  <Cascader
                    className="w-full mb-10"
                    options={options}
                    placeholder="선택된 광고"
                  />
                </div>
              );
            })}
          </div>
        }
      />
      {/* 하루 평균 유동인구수 */}
      <Content
        label={
          <div className="flex gap-[18px] items-center">
            <img className="w-[16px] h-[16px]" src={accountMultiple} />
            <Body1 text="하루 평균 유동인구수" color="black" />
          </div>
        }
        value={
          <div className="grid grid-cols-3 gap-x-[30px]">
            {infos.map((info) => {
              return info.info1 !== undefined ? (
                <div className="flex flex-col gap-2 py-6 items-center justify-center min-w-[240px]">
                  <img className="w-[30px] h-[30px]" src={walk} />
                  <Subtitle2 text={`${info.info1}명`} color="black" />
                </div>
              ) : (
                <div className="min-h-[120px]"></div>
              );
            })}
          </div>
        }
      />
      {/* 관심을 보인 비율 */}
      <Content
        label={
          <div className="flex gap-[18px] items-center">
            <img className="w-6 h-6" src={exclamationThick} />
            <Body1 text="관심을 보인 비율" color="black" />
          </div>
        }
        value={
          <div className="grid grid-cols-3 gap-x-[30px]">
            {infos.map((info) => {
              return info.info2 !== undefined ? (
                <div className="flex flex-col gap-2 py-6 items-center justify-center min-w-[240px]">
                  <img className="w-[32px] h-[25px]" src={attentionPeople} />
                  <Subtitle2 text={`${info.info2}%`} color="black" />
                </div>
              ) : (
                <div className="min-h-[120px]"></div>
              );
            })}
          </div>
        }
      />
      {/* 관심도 성비 */}
      <Content
        label={
          <div className="flex gap-[18px] items-center">
            <img className="w-[22px] h-[17px]" src={gender} />
            <Body1 text="관심도 성비" color="black" />
          </div>
        }
        value={
          <div className="grid grid-cols-3 gap-x-[30px]">
            {infos.map((info) => {
              return info.info3 !== undefined ? (
                <div className="flex flex-col gap-2 py-6 items-center justify-center min-w-[240px]">
                  <img className="w-[30px] h-[30px]" src={walk} />
                  <Subtitle2 text={`${info.info3[0]}%`} color="black" />
                </div>
              ) : (
                <div className="min-h-[164px]"></div>
              );
            })}
          </div>
        }
      />
      {/* 인당 평균 시선 고정시간 */}
      <Content
        label={
          <div className="flex gap-[18px] items-center">
            <img className="w-6 h-6" src={eye} />
            <Body1 text="인당 평균 시선 고정시간" color="black" />
          </div>
        }
        value={
          <div className="grid grid-cols-3 gap-x-[30px]">
            {infos.map((info) => {
              return info.info4 !== undefined ? (
                <div className="flex flex-col gap-2 py-6 items-center justify-center min-w-[240px]">
                  <img className="w-6 h-6" src={focus} />
                  <Subtitle2 text={`${info.info4}초`} color="black" />
                </div>
              ) : (
                <div className="min-h-[120px]"></div>
              );
            })}
          </div>
        }
      />
    </div>
  );
};

export default CompareMediaScreen;
