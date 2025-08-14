import api from '../api/axiosConfig';

export async function fetchAllContracts() {
    try {
        const response = await api.get('/contracts/getAll');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách hợp đồng:', error);
        throw new Error('Failed to fetch contracts');
    }
}

export const createContract = async (contractData) => {
    try {
        const response = await api.post('/contracts/create', contractData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo hợp đồng:', error);
        throw error;
    }
};

export const updateContract = async (id, updateData) => {
    try {
        const response = await api.put(`/contracts/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật hợp đồng:', error);
        throw error;
    }
};

export const extendContract = async (id, newEndDate) => {
    try {
        const response = await api.put(`/contracts/extend/${id}`, null, {
            params: { newEndDate }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi gia hạn hợp đồng:', error);
        throw error;
    }
};

export const terminateContract = async (id) => {
    try {
        const response = await api.put(`/contracts/terminate/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi chấm dứt hợp đồng:', error);
        throw error;
    }
};

export async function softDeleteContract(id) {
    try {
        await api.delete(`/contracts/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa mềm hợp đồng:', error);
        throw error;
    }
}

export async function restoreContract(id) {
    try {
        await api.put(`/contracts/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục hợp đồng:', error);
        throw error;
    }
}

export const modifyContract = async (id, contractData) => {
    try {
        const response = await api.put(`/contracts/modify/${id}`, contractData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi chỉnh sửa hợp đồng:', error);
        throw error;
    }
};

export const fetchContractHistory = async (contractId) => {
    try {
        const response = await api.get(`/contracts/history/${contractId}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải lịch sử hợp đồng:', error);
        throw error;
    }
};


