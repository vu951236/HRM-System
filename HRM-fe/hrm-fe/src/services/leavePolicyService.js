import api from '../api/axiosConfig';

export async function fetchAllLeavePolicies() {
    try {
        const response = await api.get('/leave-policy/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách chính sách nghỉ:', error);
        throw new Error('Failed to fetch leave policies');
    }
}

export const createLeavePolicy = async (policyData) => {
    try {
        const response = await api.post('/leave-policy/create', policyData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo chính sách nghỉ:', error);
        throw error;
    }
};

export const updateLeavePolicy = async (id, updateData) => {
    try {
        const response = await api.put(`/leave-policy/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật chính sách nghỉ:', error);
        throw error;
    }
};

export async function deleteLeavePolicy(id) {
    try {
        await api.delete(`/leave-policy/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa chính sách nghỉ:', error);
        throw error;
    }
}

export const restoreLeavePolicy = async (id) => {
    try {
        const response = await api.put(`/leave-policy/restore/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi khôi phục chính sách nghỉ:', error);
        throw error;
    }
};

export const fetchLeavePolicyById = async (id) => {
    try {
        const response = await api.get(`/leave-policy/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi lấy thông tin chính sách nghỉ:', error);
        throw error;
    }
};
