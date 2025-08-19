import api from '../api/axiosConfig';

export async function fetchAllTemplates() {
    try {
        const response = await api.get('/work-schedule-templates/getAll');
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tải danh sách template lịch làm việc:', error);
        throw new Error('Failed to fetch work schedule templates');
    }
}

export async function fetchTemplateById(id) {
    try {
        const response = await api.get(`/work-schedule-templates/get/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi lấy thông tin template:', error);
        throw error;
    }
}

export async function createTemplate(templateData) {
    try {
        const response = await api.post('/work-schedule-templates/create', templateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi tạo template:', error);
        throw error;
    }
}

export async function updateTemplate(id, updateData) {
    try {
        const response = await api.put(`/work-schedule-templates/update/${id}`, updateData);
        return response.data.data;
    } catch (error) {
        console.error('Lỗi khi cập nhật template:', error);
        throw error;
    }
}

export async function deleteTemplate(id) {
    try {
        await api.delete(`/work-schedule-templates/delete/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi xóa template:', error);
        throw error;
    }
}

export async function restoreTemplate(id) {
    try {
        await api.post(`/work-schedule-templates/restore/${id}`);
        return true;
    } catch (error) {
        console.error('Lỗi khi khôi phục template:', error);
        throw error;
    }
}

export async function applyTemplate(templateId, startDate, endDate, overwrite = false) {
    try {
        await api.post(`/work-schedule-templates/apply/${templateId}`, null, {
            params: { startDate, endDate, overwrite }
        });
        return true;
    } catch (error) {
        console.error('Lỗi khi áp dụng template:', error);
        throw error;
    }
}
