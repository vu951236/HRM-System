import api from '../api/axiosConfig';

export async function fetchAllOvertimeRecords() {
    try {
        const response = await api.get('/overtime/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách OT:', error);
        throw error;
    }
}

export async function fetchOvertimeByDate(date) {
    try {
        const response = await api.get('/overtime/date', {
            params: { date }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải OT theo ngày:', error);
        throw error;
    }
}

export async function createOvertimeRecord(requestData) {
    try {
        const response = await api.post('/overtime/create', requestData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo OT:', error);
        throw error;
    }
}

export async function approveOvertimeRecord(id) {
    try {
        const response = await api.post(`/overtime/approve/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi phê duyệt OT:', error);
        throw error;
    }
}

export async function rejectOvertimeRecord(id) {
    try {
        const response = await api.post(`/overtime/reject/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi từ chối OT:', error);
        throw error;
    }
}

export async function deleteOvertimeRecord(id) {
    try {
        await api.post(`/overtime/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa OT:', error);
        throw error;
    }
}

export async function restoreOvertimeRecord(id) {
    try {
        await api.post(`/overtime/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục OT:', error);
        throw error;
    }
}
