import api from '../api/axiosConfig';

export async function fetchAllRecord() {
    try {
        const response = await api.get('/employee-records/getAllRecords');
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to fetch records');
    }
}

export const createRecord = async (recordData) => {
    try {
        const response = await api.post('/employee-records/create', recordData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo nhân viên:', error);
        throw error;
    }
};

export const updateRecord = async (id, updateData) => {
    try {
        const response = await api.put(`/employee-records/update/${id}`, updateData);
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to update');
    }
};

export async function softDeleteRecord(id) {
    try {
        await api.put(`/employee-records/soft-delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa mềm:', error);
        throw error;
    }
}

export async function restoreRecord(id) {
    try {
        await api.put(`/employee-records/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục:', error);
        throw error;
    }
}
