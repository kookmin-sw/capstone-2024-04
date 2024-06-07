import Cookies from "universal-cookie";
import { SignInAPIProps, SignUpAPIProps } from "../../interfaces/interface";
import publicApi from "../public_api";


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
    const response: any = await publicApi.post('/api/v1/auth/reissue', { accessToken,refreshToken });

    if (response.status === 500 && response.divisionCode === "G999") { // Redis에 RefreshToken이 존재하지 않는 경우
    // 에러 처리 필요 
    }

    const newAccessToken = response.data.data.accessToken;
    const newRefreshToken = response.data.data.refreshToken;
    
    // AccessToken, RefreshToken 업데이트
    cookies.set('accessToken', newAccessToken);
    cookies.set('refreshToken', newRefreshToken);

    return [newAccessToken, newRefreshToken];
}