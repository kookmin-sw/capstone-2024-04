import privateApi from "./private_api"

export const patchProfile = async (formData: FormData) => {
    const response = await privateApi.patch('/api/v1/user/profile', formData, {headers: {
        "Content-Type": "multipart/form-data",
    }}).catch((err) => {return err.response});
    return response;
}