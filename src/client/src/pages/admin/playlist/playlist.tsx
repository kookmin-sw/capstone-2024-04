import { useEffect, useState } from "react";
import { LocationInfo } from "../../../interfaces/interface";
import { getLocation } from "../../../api/client/location";
import { postPlayList } from "../../../api/admin/playlist";
import { toast } from "react-hot-toast";
import { useNavigate } from "react-router-dom";

const PlayListPage = () => {
  const [locationList, setLocationList] = useState<LocationInfo[]>([]);

  const navigate = useNavigate();

  const loadLocationList = async () => {
    const result = await getLocation();
    if (result.status === 200) {
      setLocationList(result.data.data);
    }
  };

  const registerPlayList = async () => {
    const result = await postPlayList();
    if (result.status === 201) {
      toast.success("금일 플레이리스트 등록이 완료되었습니다.");
    } else {
      toast.error("금일 플레이리스트 등록에 실패하였습니다.");
    }
  };

  useEffect(() => {
    loadLocationList();
  }, []);

  return (
    <div className="grid grid-cols-4 gap-4 w-full min-w-[920px] overflow-y-scroll">
      {locationList.map((loc) => (
        <div
          key={loc.locationId}
          className="p-4 border-[1px] border-placeholder rounded-md"
          onClick={() => {
            // setLocation(loc);
            navigate(`/playlist/${loc.locationId}`);
          }}
        >
          {loc.address}
        </div>
      ))}
      <button
        type="button"
        onClick={registerPlayList}
        className=" bg-main text-white rounded-lg"
      >
        금일 신청 플레이리스트 등록하기
      </button>
    </div>
  );
};

export default PlayListPage;
