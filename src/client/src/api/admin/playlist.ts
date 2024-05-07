import privateApi from "../private_api"

export const getPlayList = async (locationId: number) => {
    const response = await privateApi.get(`/api/v2/playlist/${locationId}`).catch((err)=> {return err.response});
    console.log(response);
    return response;
}

export const postPlayList = async () => {
    const response = await privateApi.post('/api/v2/playlist').catch((err) => {return err.response});
    return response;
}