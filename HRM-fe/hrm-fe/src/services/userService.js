import api from '../api/axiosConfig';

export async function fetchUserInfo() {
    try {
        const response = await api.get('/user/getinfo');
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to fetch user info');
    }
}