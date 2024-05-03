import privateApi from "../private_api"

interface patchPasswordProps {
    password: string,
    updatePassword: string,
}

export const patchProfile = async (formData: FormData) => {
    const response = await privateApi.patch('/api/v1/user/profile', formData, {headers: {
        "Content-Type": "multipart/form-data",
    }}).catch((err) => {return err.response});
    return response;
}

export const patchPassword = async ({password, updatePassword}: patchPasswordProps) => {
    const response = await privateApi.patch('/api/v1/user/password', {password, updatePassword}).catch((err) => {return err.response})
    return response;
}

export const verifyPassword = async (password: string) => {
    const response = await privateApi.post('/api/v1/user/verify-password', {password}).catch((err) => {
        throw new Error("비밀번호가 일치하지 않습니다.");
    });
    return response;
}
