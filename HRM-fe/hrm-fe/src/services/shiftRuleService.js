import api from '../api/axiosConfig';

export async function fetchAllShiftRules() {
    try {
        const response = await api.get('/shift-rules/getAll');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách quy tắc ca:', error);
        throw new Error('Failed to fetch shift rules');
    }
}

export const createShiftRule = async (ruleData) => {
    try {
        const response = await api.post('/shift-rules/create', ruleData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo quy tắc ca:', error);
        throw error;
    }
};

export const updateShiftRule = async (id, updateData) => {
    try {
        const response = await api.put(`/shift-rules/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật quy tắc ca:', error);
        throw error;
    }
};

export async function deleteShiftRule(id) {
    try {
        await api.delete(`/shift-rules/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa quy tắc ca:', error);
        throw error;
    }
}

export async function restoreShiftRule(id) {
    try {
        await api.put(`/shift-rules/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục quy tắc ca:', error);
        throw error;
    }
}

export const fetchShiftRuleById = async (id) => {
    try {
        const response = await api.get(`/shift-rules/getShiftRule/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi lấy thông tin quy tắc ca:', error);
        throw error;
    }
};
