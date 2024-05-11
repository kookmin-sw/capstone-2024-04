import privateApi from "../private_api"

/** 신청 취소 */
export const deleteApply = async ({mediaId, applyId}: any) => {
    const response = await privateApi.delete(`/api/v1/media/${mediaId}/apply/${applyId}}`).catch((err) => {
        return err.response;
    })
    return response;
}

/** 신청 리스트 조회 */
export const getApplies = async () => {
    const response = await privateApi.get('/api/v1/media/applies').catch((err) => {
        return err.response;
    })
    return response;
}

/** id 별 신청 데이터 조회 */
export const getApply = async ({mediaId}: any) => {
    const response  = await privateApi.get(`/api/v1/media/${mediaId}/apply`).catch((err) => {
        return err.response;
    })
    return response;
}

/** 광고 신청 */
export const postApply = async ({mediaId, requestBody}: any) => {
    const response  = await privateApi.post(`/api/v1/media/${mediaId}/apply`, requestBody).catch((err) => {
        return err.response;
    })
    return response;
}