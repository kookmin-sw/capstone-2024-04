import privateApi from "../private_api"

export interface patchApplyProps {
    applyId: number[],
    status: string, 
}

export const getApply = async () => {
    const response = await privateApi.get('/api/v2/apply').catch((err) => {return err.response});
    console.log(response);
    return response;
}

export const patchApply = async ({applyId, status}: patchApplyProps) => { 
    console.log({applyId, status});
    const response = await privateApi.patch('/api/v2/apply', {applyId, status}).catch((err) => {return err.response});
    return response;
}