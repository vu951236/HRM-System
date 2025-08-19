import api from '../api/axiosConfig';

export async function getDepartments() {
    const res = await api.get('/api/departments');
    return res.data.data;
}

export async function getPositions() {
    const res = await api.get('/api/positions');
    return res.data.data;
}

export async function getEmployeeTypes() {
    const res = await api.get('/api/employeetypes');
    return res.data.data;
}

export async function getContractTypes() {
    const res = await api.get('/api/contracttypes');
    return res.data.data;
}

export async function getShiftRules() {
    const res = await api.get('/api/shiftrules');
    return res.data.data;
}

export async function getEmployees() {
    const res = await api.get('/api/employees');
    return res.data.data;
}

export async function getShifts() {
    const res = await api.get('/api/shifts');
    return res.data.data;
}

export async function getShiftSwapOptions(requesterId, targetEmployeeId) {
    let url = `/api/shift-swap-options/${requesterId}`;
    if (targetEmployeeId) {
        url += `?targetEmployeeId=${targetEmployeeId}`;
    }
    const res = await api.get(url);
    return res.data.data;
}
