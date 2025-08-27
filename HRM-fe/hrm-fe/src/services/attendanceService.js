import api from '../api/axiosConfig';

export async function fetchAllAttendanceLogs() {
    try {
        const response = await api.get('/attendance/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách chấm công:', error);
        throw new Error('Failed to fetch attendance logs');
    }
}



