import publicApi from "./public_api";

export interface SignUpAPIProps {
    email: string;
    password: string;
    company: string;
}

export interface SignInAPIProps {
    email: string;
    password: string;
}

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

