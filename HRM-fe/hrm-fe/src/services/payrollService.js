import api from '../api/axiosConfig';

export async function fetchAllPayrolls() {
    try {
        const response = await api.get('/payroll/all');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách bảng lương:', error);
        throw new Error('Failed to fetch payrolls');
    }
}

export async function calculatePayroll(employeeId, month, year) {
    try {
        const response = await api.post('/payroll/calculate', null, {
            params: { employeeId, month, year }
        });
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tính toán bảng lương:', error);
        throw error;
    }
}

export async function approvePayroll(id) {
    try {
        const response = await api.post(`/payroll/approve/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi phê duyệt bảng lương:', error);
        throw error;
    }
}
