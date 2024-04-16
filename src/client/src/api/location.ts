import axios from "axios";
import Cookies from "universal-cookie";

const API_ENDPOINT = import.meta.env.VITE_APP_API_ENDPOINT;
/**
 *  24.04.16
 *  token이 없을 때
 *  refreshToken만 존재하는 경우 등에 대한 처리가 필요합니다.
 */ 
export const getLocation = async() => {
    const cookies = new Cookies();
    const token = cookies.get('accessToken');

    if (token) {
        const response = await axios.get(`${API_ENDPOINT}/api/v1/location`, {headers:{Authorization: `Bearer ${token}`}, withCredentials: true}).catch((err) => {
            return err.response;
        });
        console.log(response.data);
        return response;
    } else {
        console.error('token 없을 때 예외처리');
        return {status: 400};
    }
}