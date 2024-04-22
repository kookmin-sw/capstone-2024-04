import { MenuButtonProps } from "../interfaces/interface";

const MenuButton = ({
  title,
  iconWhiteSrc,
  iconBlackSrc,
  isActive,
  onClick,
}: MenuButtonProps) => {
  return (
    <div
      className={`flex flex-col justify-center items-center w-[90px] h-[100px] rounded-[10px] gap-2 cursor-pointer ${
        isActive ? "bg-main" : ""
      }`}
      onClick={onClick}
    >
      <img src={`${isActive ? iconWhiteSrc : iconBlackSrc}`} />
      <p
        className={`${
          isActive ? "text-white" : "text-[#54657b]"
        } text-xs text-center`}
      >
        {title}
      </p>
    </div>
  );
};

export default MenuButton;
