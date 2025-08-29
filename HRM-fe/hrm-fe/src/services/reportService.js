import api from '../api/axiosConfig';

export async function exportAttendanceReport(request = {}) {
    try {
        const response = await api.post('/reports/attendance', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'attendance_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Attendance report:', error);
        throw new Error('Failed to download Attendance report');
    }
}

export async function exportPayrollReport(request = {}) {
    try {
        const response = await api.post('/reports/payroll', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'payroll_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Payroll report:', error);
        throw new Error('Failed to download Payroll report');
    }
}

export async function exportLeaveOvertimeReport(request = {}) {
    try {
        const response = await api.post('/reports/leave-overtime', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'leave_overtime_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Leave & Overtime report:', error);
        throw new Error('Failed to download Leave & Overtime report');
    }
}

export async function exportEmployeeCountReport(request = {}) {
    try {
        const response = await api.post('/reports/employee-count', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'employee_count_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Employee Count report:', error);
        throw new Error('Failed to download Employee Count report');
    }
}

export async function exportContractCountReport(request = {}) {
    try {
        const response = await api.post('/reports/contract-count', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'contract_count_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Contract Count report:', error);
        throw new Error('Failed to download Contract Count report');
    }
}

export async function exportExpiringContractsReport(request = {}) {
    try {
        const response = await api.post('/reports/expiring-contracts', request, {
            responseType: 'blob',
        });

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'expiring_contracts_report.xlsx');
        document.body.appendChild(link);
        link.click();
        link.remove();
    } catch (error) {
        console.error('Lỗi khi tải file Expiring Contracts report:', error);
        throw new Error('Failed to download Expiring Contracts report');
    }
}
