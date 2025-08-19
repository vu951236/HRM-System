import api from '../api/axiosConfig';

export async function fetchAllShifts() {
    try {
        const response = await api.get('/shifts/getAll');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách ca làm việc:', error);
        throw new Error('Failed to fetch shifts');
    }
}

export const createShift = async (shiftData) => {
    try {
        const response = await api.post('/shifts/create', shiftData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo ca làm việc:', error);
        throw error;
    }
};

export const updateShift = async (id, updateData) => {
    try {
        const response = await api.put(`/shifts/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật ca làm việc:', error);
        throw error;
    }
};

export async function deleteShift(id) {
    try {
        await api.delete(`/shifts/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa ca làm việc:', error);
        throw error;
    }
}

export async function restoreShift(id) {
    try {
        await api.put(`/shifts/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục ca làm việc:', error);
        throw error;
    }
}

export const fetchShiftById = async (id) => {
    try {
        const response = await api.get(`/shifts/getShift/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi lấy thông tin ca làm việc:', error);
        throw error;
    }
};
