import api from '../api/axiosConfig';

export const fetchAllUsers = async () => {
    try {
        const response = await api.get('/admin/allUser');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi gọi API /admin:', error);
        throw error;
    }
};

export const createUser = async (userData) => {
    try {
        const response = await api.post('/admin/createUser', userData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo nhân viên:', error);
        throw error;
    }
};

export const updateUser = async (userId, updateData) => {
    try {
        const response = await api.put(`/admin/update/${userId}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật nhân viên:', error);
        throw error;
    }
};

export const lockOrUnlockUser = async (userId, lockData) => {
    try {
        const response = await api.put(`/admin/${userId}/lock`, lockData);
        return response.data.data;
    } catch (error) {
        console.error(`Lỗi khi khóa/mở khóa user ${userId}:`, error);
        throw error;
    }
};

