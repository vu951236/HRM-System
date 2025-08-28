import React, { useState, useEffect } from 'react';
import '../styles/main.css';

const EditUserModal = ({ user, onClose, onUpdate }) => {
    const [userData, setUserData] = useState({
        email: '',
        password: '',
        fullName: '',
        phone: '',
        address: '',
        roleName: '',
    });

    useEffect(() => {
        if (user) {
            setUserData({
                email: user.email || '',
                password: '',
                fullName: user.fullName || '',
                phone: user.phone || '',
                address: user.address || '',
                roleName: user.roleName || '',
            });
        }
    }, [user]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = () => {
        const passwordToSend = userData.password.trim() || '12345678@aA';

        const dataToUpdate = {
            ...userData,
            password: passwordToSend,
        };

        onUpdate(user.userId, dataToUpdate);
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>✏️ Chỉnh sửa thông tin nhân viên</h2>

                <div className="form-group">
                    <label>Full name</label>
                    <input
                        name="fullName"
                        value={userData.fullName}
                        onChange={handleChange}
                        placeholder="User full name"
                    />
                </div>

                <div className="form-group">
                    <label>New Password (bỏ trống nếu chuyển về mặc định)</label>
                    <input
                        name="password"
                        type="password"
                        value={userData.password}
                        onChange={handleChange}
                        placeholder="Enter new password"
                    />
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input
                        name="email"
                        value={userData.email}
                        onChange={handleChange}
                        placeholder="User email"
                    />
                </div>

                <div className="form-group">
                    <label>Phone number</label>
                    <input
                        name="phone"
                        value={userData.phone}
                        onChange={handleChange}
                        placeholder="User phone number"
                    />
                </div>

                <div className="form-group">
                    <label>Address</label>
                    <input
                        name="address"
                        value={userData.address}
                        onChange={handleChange}
                        placeholder="User address"
                    />
                </div>

                <div className="form-group">
                    <label className="form-label">Role</label>
                    <select
                        name="roleName"
                        value={userData.roleName}
                        onChange={handleChange}
                        className="form-select"
                    >
                        <option value="">-- Chọn quyền --</option>
                        <option value="admin">Admin</option>
                        <option value="hr">Quản lý nhân sự</option>
                        <option value="staff">Nhân viên</option>
                    </select>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Update profile</button>
                </div>
            </div>
        </div>
    );
};

export default EditUserModal;
