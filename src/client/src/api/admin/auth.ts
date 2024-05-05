import publicApi from "../public_api";

export const adminSignIn = async ({email, password}: SignInAPIProps) => {
    const response = await publicApi.post('/api/v2/auth/signin', {
        email,
        password,
    }).catch((err) => {
        return err.response;
    });    
    return response;
}