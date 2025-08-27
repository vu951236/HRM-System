import api from '../api/axiosConfig';

export async function fetchAllLeaveRequests() {
    try {
        const response = await api.get('/leave/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách nghỉ phép:', error);
        throw error;
    }
}

export async function fetchLeaveByDate(date) {
    try {
        const response = await api.get('/leave/by-date', {
            params: { date }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải nghỉ phép theo ngày:', error);
        throw error;
    }
}

export async function createLeaveRequest(requestData) {
    try {
        const response = await api.post('/leave/create', requestData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo yêu cầu nghỉ phép:', error);
        throw error;
    }
}

export async function approveLeaveRequest(id) {
    try {
        const response = await api.post(`/leave/approve/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi phê duyệt nghỉ phép:', error);
        throw error;
    }
}

export async function rejectLeaveRequest(id) {
    try {
        const response = await api.post(`/leave/reject/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi từ chối nghỉ phép:', error);
        throw error;
    }
}

export async function deleteLeaveRequest(id) {
    try {
        await api.delete(`/leave/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa yêu cầu nghỉ phép:', error);
        throw error;
    }
}

export async function restoreLeaveRequest(id) {
    try {
        const response = await api.put(`/leave/restore/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi khôi phục yêu cầu nghỉ phép:', error);
        throw error;
    }
}