import api from '../api/axiosConfig';

export async function fetchAllShiftSwapRequests() {
    try {
        const response = await api.get('/shift-swap/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách yêu cầu đổi ca:', error);
        throw error;
    }
}

export async function fetchShiftSwapRequestsByStatus(status) {
    try {
        const response = await api.get('/shift-swap/status', {
            params: { status }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách yêu cầu theo trạng thái:', error);
        throw error;
    }
}

export async function createShiftSwapRequest(requestData) {
    try {
        const response = await api.post('/shift-swap/create', requestData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo yêu cầu đổi ca:', error);
        throw error;
    }
}

export async function approveShiftSwapRequest(id, approverId) {
    try {
        const response = await api.post(`/shift-swap/approve/${id}`, null, {
            params: { approverId }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi phê duyệt yêu cầu:', error);
        throw error;
    }
}

export async function rejectShiftSwapRequest(id, approverId) {
    try {
        const response = await api.post(`/shift-swap/reject/${id}`, null, {
            params: { approverId }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi từ chối yêu cầu:', error);
        throw error;
    }
}

export async function deleteShiftSwapRequest(id) {
    try {
        await api.post(`/shift-swap/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa yêu cầu:', error);
        throw error;
    }
}

export async function restoreShiftSwapRequest(id) {
    try {
        await api.post(`/shift-swap/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục yêu cầu:', error);
        throw error;
    }
}
