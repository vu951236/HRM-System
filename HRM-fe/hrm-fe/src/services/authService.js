import api from '../api/axiosConfig';
import {fetchUserInfo} from './userService';

export const login = async (username, password) => {
    try {
        const response = await api.post('/auth', { username, password }, {
            withCredentials: true,
        });

        const token = response.data.data.token;

        const refreshToken = response.data.data.refreshToken;

        const user = await fetchUserInfo();

        return { token, user, refreshToken };
    } catch (error) {
        const message = error.response?.data?.message || 'Login failed';
        throw new Error(message);
    }
};

export const refreshToken = async () => {
    try {
        const response = await api.post('/auth/refresh-token', {}, {
            withCredentials: true
        });
        const token = response.data.data.token;
        if (!token) throw new Error('No token in refresh token response');
        return token;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to refresh token');
    }
};


export const logout = async (accessToken, refreshToken) => {
    try {
        await api.post('/auth/logout', {
            token: accessToken,
            refreshToken: refreshToken,
        });
        return true;
    } catch (error) {
        const message = error.response?.data || 'Logout failed';
        console.error('Logout failed response:', message);
        throw new Error('Logout failed');
    }
};
