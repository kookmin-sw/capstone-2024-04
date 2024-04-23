import Cookies from "universal-cookie";
import { SignInAPIProps, SignUpAPIProps } from "../interfaces/interface";
import privateApi from "./private_api";
import publicApi from "./public_api";


export const signup = async ({email, password, company}: SignUpAPIProps) => {
    const response = await publicApi.post('/api/v1/auth/signup', {
        email,
        password,
        company,
    }).catch((err) => {
        return err.response;
    });
    return response;
}

export const signin = async ({email, password}: SignInAPIProps) => {
    const response = await publicApi.post('/api/v1/auth/signin', {
        email,
        password,
    }).catch((err) => {
        return err.response;
    });
    return response;
}

export const tokenRefresh = async ({accessToken, refreshToken}: any) => {
    const cookies = new Cookies();
    const response = await publicApi.post('/api/v1/auth/reissue', { accessToken,refreshToken });

    const newAccessToken = response.data.data.accessToken;
    const newRefreshToken = response.data.data.refreshToken;
    
    // AccessToken, RefreshToken 업데이트
    cookies.set('accessToken', newAccessToken);
    cookies.set('refreshToken', newRefreshToken);

    // 토큰 갱신 테스트용 코드
    console.log(accessToken, '->', newAccessToken);
    console.log(refreshToken, '->', newRefreshToken);

    return [newAccessToken, newRefreshToken];
}