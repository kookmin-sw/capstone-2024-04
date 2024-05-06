import { useEffect, useState } from "react";

export interface StatusBadgeProps {
  status: string;
  date: string[];
}

const StatusBadge = ({ status, date }: StatusBadgeProps) => {
  const [color, setColor] = useState<string>("");
  const [text, setText] = useState<string>("");

  useEffect(() => {
    if (status === "REJECT") {
      setColor("bg-[#808080]");
      setText("등록거절");
    } else if (status === "ACCEPT") {
      setColor("bg-[#30B6A7]");
      setText("집행예정");
    } else if (status === "ACCEPT") {
      setColor("bg-[#EF816A]");
      setText("집행종료");
    } else if (status === "ACCEPT") {
      setColor("bg-[#406EE5]");
      setText("집행중");
    } else if (status === "WAITING") {
      setColor("bg-[#B3B3B3]");
      setText("등록대기");
    }
    console.log(text);
  }, [color, text]);

  return (
    <div
      className={`w-[70px] py-2 flex justify-center items-center ${color} text-white text-xs rounded-[30px]`}
    >
      {text}
    </div>
  );
};

export default StatusBadge;
