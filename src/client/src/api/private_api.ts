import axios, { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from "axios";
import toast from 'react-hot-toast';
import Cookies from "universal-cookie";
import { ErrorResponse } from "../interfaces/interface";
import { tokenRefresh } from "./auth";


const API_ENDPOINT = import.meta.env.VITE_APP_API_ENDPOINT;

const privateApi = axios.create({
    baseURL: API_ENDPOINT,
    withCredentials: true,
    headers: {
        "Content-Type": "application/json",
    }
});

privateApi.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const cookies = new Cookies();
    const accessToken = cookies.get('accessToken');

    if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
    } else {
        // token 모두 제거 후, 초기 페이지로 redirect 할 예정
        toast.error('로그인 시간 제한이 만료되었습니다.');
    }

    return config;
}, (error: AxiosError) => {
    return Promise.reject(error);
});

privateApi.interceptors.response.use((response: AxiosResponse) => {
    return response;
}, async (error: AxiosError) => {
    const cookies = new Cookies();

    const config = error.config;

    const errorResponse = error.response?.data as ErrorResponse;

    const accessToken = cookies.get('accessToken');
    const refreshToken = cookies.get('refreshToken');    

    if (errorResponse.status === 401)
    {
        if (refreshToken) {
            const tokens =  await tokenRefresh({accessToken, refreshToken});

            if (config) {
                config.headers.Authorization = `Bearer ${tokens[0]}`;
                // 새로운 accessToken을 할당하여 이전 요청을 재시도
                return privateApi(config);
            } else { // config가 undefined 일때 -> 발생하면 안됨
                console.error('previous config is not founded :(');
            }

        } else {
            // token 모두 제거 후, 초기 페이지로 redirect 할 예정
            console.error('refreshToken이 만료되어 사라짐');
            toast.error('로그인 시간 제한이 만료되었습니다.');
        }
    }
});

export default privateApi;