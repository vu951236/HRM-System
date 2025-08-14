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
