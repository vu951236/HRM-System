import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import SearchBox from "../components/SearchBox.jsx";
import '../styles/Dashboard.css';
import { getSidebarGroups } from '../components/sidebarData';
import { useAuth } from '../context/AuthContext';
import { changePassword } from '../services/userService.js';

const ChangePasswordPage = () => {
    const { user } = useAuth();
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    const [passwordData, setPasswordData] = useState({
        oldPassword: '',
        newPassword: ''
    });

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    const handlePasswordChange = (e) => {
        setPasswordData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleUpdatePassword = async () => {
        try {
            // Gọi hàm changePassword với oldPassword, newPassword lấy từ passwordData
            await changePassword(passwordData.oldPassword, passwordData.newPassword);

            alert('Đổi mật khẩu thành công!');
            setPasswordData({ oldPassword: '', newPassword: '' });
        } catch (err) {
            console.error('Lỗi đổi mật khẩu:', err);
            alert('Đổi mật khẩu thất bại!');
        }
    };

    return (
        <div style={{ display: 'flex', height: '100vh' }}>
            <Sidebar groups={sidebarGroups} activeItem="Đổi mật khẩu" onItemClick={() => {}} />

            <div className="main">
                <div className="top-bar" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '10px 20px' }}>
                    <SearchBox searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
                    <div className="icons">
                        <i className="fa fa-bell" />
                        <i className="fa fa-question-circle" />
                    </div>
                </div>

                <div className="page-header" style={{ padding: '10px 20px' }}>
                    <h1>Đổi mật khẩu</h1>
                </div>

                <div className="profile-form" style={{ maxWidth: 600, margin: '20px auto', padding: 20 }}>
                    <input
                        type="password"
                        name="oldPassword"
                        placeholder="Mật khẩu cũ"
                        value={passwordData.oldPassword}
                        onChange={handlePasswordChange}
                        style={{ width: '100%', padding: 8, marginBottom: 12 }}
                    />
                    <input
                        type="password"
                        name="newPassword"
                        placeholder="Mật khẩu mới"
                        value={passwordData.newPassword}
                        onChange={handlePasswordChange}
                        style={{ width: '100%', padding: 8, marginBottom: 20 }}
                    />
                    <button onClick={handleUpdatePassword} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                        Cập nhật mật khẩu
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ChangePasswordPage;
