import { useQuery } from "react-query";
import { getApply } from "../../../api/admin/apply";

const AdminApplyPage = () => {
  const { data, isSuccess, isError, refetch } = useQuery(
    "admin-get-apply",
    () => getApply()
  );
  console.log(data);
  console.log(isSuccess);
  console.log(isError);

  if (isSuccess && typeof data !== "undefined")
    return (
      <div className="flex flex-col min-w-[920px] h-full overflow-y-scroll"></div>
    );

  if (isError || typeof data === "undefined")
    return (
      <div className="flex w-full min-w-[920px] h-full justify-center items-center">
        <button type="button" onClick={() => refetch()}>
          다시 시도하기
        </button>
      </div>
    );
};

export default AdminApplyPage;
