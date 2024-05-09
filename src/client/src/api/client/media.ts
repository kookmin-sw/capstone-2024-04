import privateApi from "../private_api"


export interface PostMediaRequest {
    advertisementTitle: string,
    advertisementDescription: string,
    locationId: number,
    startDate: string,
    endDate: string,
}

export const getMedia = async () => {
    const response = await privateApi.get('/api/v1/media').catch((err) => {
        return err.response;
    });
    return response;
}

export const postMedia = async (formData: FormData) => {
    const response = await privateApi.post('/api/v1/media', formData, {headers: {
        "Content-Type": 'multipart/form-data',
    }}).catch((err) => {
        return err.response;
    });
    return response;
}

export const deleteMedia = async ({mediaId}: any) => {
    const response  = await privateApi.delete(`/api/v1/media/${mediaId}`).catch((err) => {
        return err.response;
    });
    return response;
}