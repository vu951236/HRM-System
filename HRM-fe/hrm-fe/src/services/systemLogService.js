import api from '../api/axiosConfig';

export async function fetchAllSystemLogs() {
    try {
        const response = await api.get('/api/system-logs');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi fetch system logs:', error);
        throw new Error('Failed to fetch system logs');
    }
}
