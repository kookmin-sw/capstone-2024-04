import StatusBadge, { Status } from "./status_badge";
import { Subtitle2 } from "./text";

export interface MediaCardProps {
  img: string;
  title: string;
  description: string;
  onClick: any;
}

const MediaCard = ({ img, title, description, onClick }: MediaCardProps) => {
  return (
    <div
      onClick={onClick}
      className="flex flex-col w-full border-[1px] border-gray2 divide-x cursor-pointer"
    >
      <img className="w-full aspect-video" src={img} />
      <div className="flex flex-col p-5 gap-3">
        <h3 className="text-base font-medium text-black">{title}</h3>
        <p className="line-clamp-2 text-ellipsis">{description}</p>
      </div>
      {/* 현재 Status에 대한 정보가 없어서 임시 '등록거절'로 두었습니다. */}
      <StatusBadge status={Status.등록거절} />
    </div>
  );
};

export default MediaCard;
