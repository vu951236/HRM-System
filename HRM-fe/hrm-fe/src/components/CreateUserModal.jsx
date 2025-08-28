import React, { useState } from 'react';
import '../styles/main.css';

const CreateUserModal = ({ onClose, onCreate }) => {
    const [userData, setUserData] = useState({
        username: '',
        fullName: '',
        email: '',
        phone: '',
        address: '',
        roleName: '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUserData(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = () => {
        onCreate(userData);
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>👤 Thêm nhân viên</h2>

                <div className="form-group">
                    <label>Username</label>
                    <input name="username" value={userData.username} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Full name</label>
                    <input name="fullName" value={userData.fullName} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input name="email" value={userData.email} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Phone</label>
                    <input name="phone" value={userData.phone} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Address</label>
                    <input name="address" value={userData.address} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Role</label>
                    <select name="roleName" value={userData.roleName} onChange={handleChange}>
                        <option value="">-- Chọn quyền --</option>
                        <option value="admin">Admin</option>
                        <option value="hr">Quản lý nhân sự</option>
                        <option value="staff">Nhân viên</option>
                    </select>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Save profile</button>
                </div>
            </div>
        </div>
    );
};

export default CreateUserModal;
