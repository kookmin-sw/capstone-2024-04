import { useEffect, useState } from "react";

export enum Status {
  등록대기,
  등록거절,
  집행예정,
  집행종료,
  집행중,
}

const StatusBadge = ({ status }: any) => {
  const [color, setColor] = useState<string>("");
  const [text, setText] = useState<string>("");

  useEffect(() => {
    switch (status) {
      case Status.등록거절:
        setColor("bg-[#808080]");
        setText("등록거절");
        break;
      case Status.집행예정:
        setColor("bg-[#30B6A7]");
        setText("집행예정");
        break;
      case Status.집행종료:
        setColor("bg-[#EF816A]");
        setText("집행종료");
        break;
      case Status.집행중:
        setColor("bg-[#406EE5]");
        setText("집행중");
        break;
      case Status.등록대기:
        setColor("bg-[#B3B3B3]");
        setText("등록대기");
        break;
    }
    console.log(text);
  }, [status]);

  return (
    <div
      className={`w-[70px] py-2 flex justify-center items-center ${color} text-white text-xs rounded-[30px]`}
    >
      {text}
    </div>
  );
};

export default StatusBadge;
