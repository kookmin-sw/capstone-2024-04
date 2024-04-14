import walk from "../../../assets/icons/walk.svg";
import accountMultiple from "../../../assets/icons/account-multiple.svg";
import exclamationThick from "../../../assets/icons/exclamation-thick.svg";
import gender from "../../../assets/icons/gender.svg";
import eye from "../../../assets/icons/eye-outline.svg";
import focus from "../../../assets/icons/bullseye-arrow.svg";
import attentionPeople from "../../../assets/icons/Vector.svg";
import { Body1, Subtitle2 } from "../../../components/text";

interface ContentProps {
  label: JSX.Element;
  value: JSX.Element;
}

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

  return (
    <div className="flex flex-col divide-y px-[30px]">
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
