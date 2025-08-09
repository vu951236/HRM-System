import api from '../api/axiosConfig';

export async function fetchUserInfo() {
    try {
        const response = await api.get('/user/getinfo');
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to fetch user info');
    }
}

export const sendOtpToEmail = async (email) => {
    try {
        await api.post('/user/forgot-password', { email });
        return true;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to send OTP');
    }
};

export const updateProfile = async (userId, formData) => {
    try {
        const response = await api.put(`/user/${userId}/update-profile`, formData);
        return response.data;  // hoặc true nếu bạn muốn
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to update');
    }
};


export const verifyOtpCode = async (email, code) => {
    try {
        const response = await api.post('/user/verify-code', { email, code });
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to verify OTP');
    }
};

export const resetPassword = async (email, code, newPassword) => {
    try {
        const response = await api.post('/user/reset-password', { email, code, newPassword });
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to reset password');
    }
};

export const changePassword = async (oldPassword, newPassword) => {
    try {
        const response = await api.put('/user/updatePass', { oldPassword, newPassword });
        return response.data.data;
        // eslint-disable-next-line no-unused-vars
    } catch (error) {
        throw new Error('Failed to update password');
    }
};
