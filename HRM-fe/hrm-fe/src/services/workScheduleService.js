import api from '../api/axiosConfig';

export async function fetchAllWorkSchedules() {
    try {
        const response = await api.get('/work-schedules/getAll');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách lịch làm việc:', error);
        throw new Error('Failed to fetch work schedules');
    }
}

export const createWorkSchedule = async (scheduleData) => {
    try {
        const response = await api.post('/work-schedules/create', scheduleData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo lịch làm việc:', error);
        throw error;
    }
};

export const updateWorkSchedule = async (id, updateData) => {
    try {
        const response = await api.put(`/work-schedules/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật lịch làm việc:', error);
        throw error;
    }
};

export async function deleteWorkSchedule(id) {
    try {
        await api.delete(`/work-schedules/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa lịch làm việc:', error);
        throw error;
    }
}

export async function restoreWorkSchedule(id) {
    try {
        await api.post(`/work-schedules/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục lịch làm việc:', error);
        throw error;
    }
}

export const fetchWorkScheduleById = async (id) => {
    try {
        const response = await api.get(`/work-schedules/getSchedule/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi lấy thông tin lịch làm việc:', error);
        throw error;
    }
};
