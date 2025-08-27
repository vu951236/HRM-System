import api from '../api/axiosConfig';

export async function fetchAllSalaryRules() {
    try {
        const response = await api.get('/salaryRules/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách quy tắc lương:', error);
        throw new Error('Failed to fetch salary rules');
    }
}

export const updateSalaryRule = async (id, updateData) => {
    try {
        const response = await api.put(`/salaryRules/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật quy tắc lương:', error);
        throw error;
    }
};

