import privateApi from "./private_api"


export interface PostMediaRequest {
    advertisementTitle: string,
    advertisementDescription: string,
    locationId: number,
    startDate: string,
    endDate: string,
}
interface PostMediaProps {
    request: PostMediaRequest,
    file: string,
}

export const postMedia = async ({request, file}: PostMediaProps) => {
    const response = await privateApi.post('/api/v1/media', {
        request, file
    }).catch((err) => {
        return err.response;
    });
    return response;
}