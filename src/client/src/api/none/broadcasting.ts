import publicApi from "../public_api";

export interface GetDailyPlayListProps {
    locationId: number;
}

export const putBroadcast = async () => {
    const response = await publicApi.put('/none/api/broadcast').catch((err) => {return err.response});
    return response;
}

export const getDailyPlayList = async ({locationId}: GetDailyPlayListProps) => {
    const response = await publicApi.get(`/none/api/broadcast/${locationId}`).catch((err) => {return err.response});
    return response;
}