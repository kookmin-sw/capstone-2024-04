import axios from "axios";

const API_ENDPOINT = import.meta.env.VITE_APP_API_ENDPOINT;

interface SignUpAPIProps {
    email: string;
    password: string;
    company: string;
}

interface SignInAPIProps {
    email: string;
    password: string;
}

export const signup = async ({email, password, company}: SignUpAPIProps) => {
    const response = await axios.post(`${API_ENDPOINT}/api/v1/auth/signup`, {
        email,
        password,
        company,
    });
    return response;
}

export const signin = async ({email, password}: SignInAPIProps) => {
    const response = await axios.post(`${API_ENDPOINT}/api/v1/auth/signup`, {
        email,
        password,
    });
    return response;
}