import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import SearchBox from "../components/SearchBox.jsx";
import '../styles/Dashboard.css';
import { getSidebarGroups } from '../components/sidebarData';
import { useAuth } from '../context/AuthContext';
import {fetchUserInfo, updateProfile} from '../services/userService.js';


const EditProfilePage = () => {
    const { user } = useAuth();
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');

    const [formData, setFormData] = useState({
        email: '',
        fullName: '',
        gender: '',
        dob: '',
        phone: '',
        address: '',
        imageUrl: ''
    });

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        fetchUserInfo()
            .then(data => {
                let dobFormatted = '';
                if(data?.dob){
                    dobFormatted = data.dob.split('T')[0];
                }

                setFormData({
                    email: data?.email || '',
                    fullName: data?.fullName || '',
                    gender: data?.gender || '',
                    dob: dobFormatted,
                    phone: data?.phone || '',
                    address: data?.address || '',
                    imageUrl: data?.imageUrl || ''
                });
            })
            .catch(err => {
                console.error(err);
                alert('Lấy thông tin người dùng thất bại');
            });
    }, []);


    const handleChange = (e) => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleUpdateProfile = async () => {
        if (!user || !user.userId) {
            alert('Bạn chưa đăng nhập');
            return;
        }
        try {
            await updateProfile(user.userId, formData);
            alert('Cập nhật thông tin thành công!');
        } catch (err) {
            console.error('Lỗi cập nhật profile:', err);
            alert('Cập nhật thông tin thất bại!');
        }
    };

    return (
        <div style={{ display: 'flex', height: '100vh' }}>
            <Sidebar groups={sidebarGroups} activeItem="Sửa thông tin cá nhân" onItemClick={() => {}} />

            <div className="main">
                <div className="top-bar">
                    <SearchBox searchTerm={searchTerm} setSearchTerm={setSearchTerm} />
                    <div className="icons">
                        <i className="fa fa-bell" />
                        <i className="fa fa-question-circle" />
                    </div>
                </div>

                <div className="page-header">
                    <h1>Sửa thông tin cá nhân</h1>
                </div>

                <div className="profile-form">
                    <div className="form-group">
                        <label>Email:</label>
                        <input
                            type="email"
                            name="email"
                            placeholder="Email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Họ tên:</label>
                        <input
                            type="text"
                            name="fullName"
                            placeholder="Họ tên"
                            value={formData.fullName}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Giới tính:</label>
                        <input
                            type="text"
                            name="gender"
                            placeholder="Giới tính"
                            value={formData.gender}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Ngày sinh:</label>
                        <input
                            type="date"
                            name="dob"
                            value={formData.dob}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Số điện thoại:</label>
                        <input
                            type="tel"
                            name="phone"
                            placeholder="Số điện thoại"
                            value={formData.phone}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Địa chỉ:</label>
                        <input
                            type="text"
                            name="address"
                            placeholder="Địa chỉ"
                            value={formData.address}
                            onChange={handleChange}
                        />
                    </div>

                    <div className="form-group">
                        <label>Ảnh đại diện (URL):</label>
                        <input
                            type="text"
                            name="imageUrl"
                            placeholder="URL ảnh"
                            value={formData.imageUrl}
                            onChange={handleChange}
                        />
                    </div>

                    <button onClick={handleUpdateProfile}>
                        Cập nhật thông tin
                    </button>
                </div>

            </div>
        </div>
    );
};

export default EditProfilePage;
