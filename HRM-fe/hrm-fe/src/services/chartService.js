import api from '../api/axiosConfig';

export async function fetchLeaveOvertimeChart(request = {}) {
    try {
        const response = await api.post('/charts/leave-overtime', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Leave & Overtime:', error);
        throw new Error('Failed to fetch Leave & Overtime chart');
    }
}

export async function fetchPayrollChart(request = {}) {
    try {
        const response = await api.post('/charts/payroll', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Payroll:', error);
        throw new Error('Failed to fetch Payroll chart');
    }
}

export async function fetchAttendanceChart(request = {}) {
    try {
        const response = await api.post('/charts/attendance', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Attendance:', error);
        throw new Error('Failed to fetch Attendance chart');
    }
}

export async function fetchEmployeeByDepartment(request = {}) {
    try {
        const response = await api.post('/charts/employee-department', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Employee by Department:', error);
        throw new Error('Failed to fetch Employee by Department chart');
    }
}

export async function fetchContractByType(request = {}) {
    try {
        const response = await api.post('/charts/contract-type', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Contract by Type:', error);
        throw new Error('Failed to fetch Contract by Type chart');
    }
}

export async function fetchContractExpiring(request = {}) {
    try {
        const response = await api.post('/charts/contract-expiring', request);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải dữ liệu Contract Expiring:', error);
        throw new Error('Failed to fetch Contract Expiring chart');
    }
}