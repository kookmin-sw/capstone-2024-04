import { useEffect, useState } from "react";

enum Status {
  등록거절,
  집행예정,
  집행종료,
  집행중,
}

const StatusBadge = (status: Status) => {
  const [color, setColor] = useState<string>("");
  const [text, setText] = useState<string>("");

  useEffect(() => {
    switch (status) {
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
    }
  }, [status]);

  return (
    <div
      className={`w-[70px] py-2 flex justify-center items-center bg-[${color}]`}
    >
      {text}
    </div>
  );
};

export default StatusBadge;
