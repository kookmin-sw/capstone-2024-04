import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export enum Status {
  등록대기,
  등록거절,
  집행예정,
  집행종료,
  집행중,
}

const StatusBadge = ({ status: Status }: any) => {
  const params = useParams();
  const [color, setColor] = useState<string>("");
  const [text, setText] = useState<string>("");

  useEffect(() => {
    switch (params.status) {
      case Status.등록거절:
        setColor("#808080");
        setText("등록거절");
        break;
      case Status.집행예정:
        setColor("#30B6A7");
        setText("집행예정");
        break;
      case Status.집행종료:
        setColor("#EF816A");
        setText("집행종료");
        break;
      case Status.집행중:
        setColor("#406EE5");
        setText("집햅중");
        break;
      case Status.등록대기:
        setColor("#B3B3B3");
        setText("등록대기");
        break;
    }
  }, [params.status]);

  return (
    <div
      className={`w-[70px] py-2 flex justify-center items-center bg-[${color}]`}
    >
      {text}
    </div>
  );
};

export default StatusBadge;
