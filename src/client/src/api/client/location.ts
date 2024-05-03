import privateApi from "../private_api";

export const getLocation = async() => {
    const response = await privateApi.get('/api/v1/location').catch((err) => {
        return err.response;
    });
    console.log(response.data);
    return response;

}