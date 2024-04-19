import axios from "axios";

const API_ENDPOINT = import.meta.env.VITE_APP_API_ENDPOINT;

const publicApi = axios.create({
    baseURL: API_ENDPOINT,
    withCredentials: true,
    headers: {
        "Content-Type": "application/json",
    }
});

export default publicApi;