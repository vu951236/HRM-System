import React, { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import 'font-awesome/css/font-awesome.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import logo from '../assets/logo.png';

function getIconClass(item) {
    switch (item) {
        case 'Sửa thông tin cá nhân': return 'fa fa-user-edit';
        case 'Đổi mật khẩu': return 'fa fa-key';
        case 'Quản lý nhân viên': return 'fa fa-users';
        case 'Quản lý hồ sơ': return 'fa fa-id-card';
        case 'Quản lý hợp đồng': return 'fa fa-file-contract';
        case 'Quản lý quy tắc ca': return 'fa fa-clipboard-list';
        case 'Quản lý ca làm việc': return 'fa fa-business-time';
        case 'Quản lý mẫu lịch làm việc': return 'fa fa-calendar-alt';
        case 'Quản lý lịch làm việc': return 'fa fa-calendar-check';
        case 'Quản lý yêu cầu đổi ca': return 'fa fa-exchange-alt';
        case 'Yêu cầu đổi ca': return 'fa fa-random';
        case 'Quản lý tăng ca': return 'fa fa-clock';
        case 'Yêu cầu tăng ca': return 'fa fa-hourglass-half';
        case 'Quản lý nghỉ phép': return 'fa fa-plane-departure';
        case 'Yêu cầu nghỉ phép': return 'fa fa-umbrella-beach';
        case 'Quản lý chính sách nghỉ': return 'fa fa-file-medical';
        case 'Quản lý chấm công': return 'fa fa-fingerprint';
        case 'Quản lý chính sách lương': return 'fa fa-balance-scale';
        case 'Quản lý bảng lương': return 'fa fa-money-bill-wave';
        case 'Thống kê mức chuyên cần': return 'fa fa-chart-line';
        case 'Thống kê nhân viên': return 'fa fa-chart-pie';
        case 'Nhật ký hệ thống': return 'fa fa-history';
        default: return 'fa fa-folder';
    }
}

function getRouteFromItem(item) {
    switch (item) {
        case 'Sửa thông tin cá nhân': return '/editprofile';
        case 'Đổi mật khẩu': return '/changepass';
        case 'Quản lý nhân viên': return '/employee';
        case 'Quản lý hồ sơ': return '/record';
        case 'Quản lý hợp đồng': return '/contract';
        case 'Quản lý quy tắc ca': return '/shiftrule';
        case 'Quản lý ca làm việc': return '/shift';
        case 'Quản lý mẫu lịch làm việc': return '/workscheduletemplate';
        case 'Quản lý lịch làm việc': return '/workschedule';
        case 'Quản lý yêu cầu đổi ca': return '/shiftswap';
        case 'Yêu cầu đổi ca': return '/shiftswap';
        case 'Quản lý tăng ca': return '/overtime';
        case 'Yêu cầu tăng ca': return '/overtime';
        case 'Quản lý nghỉ phép': return '/leave';
        case 'Yêu cầu nghỉ phép': return '/leave';
        case 'Quản lý chính sách nghỉ': return '/leavepolicy';
        case 'Quản lý chấm công': return '/attendance';
        case 'Quản lý chính sách lương': return '/salaryrule';
        case 'Quản lý bảng lương': return '/salary';
        case 'Thống kê mức chuyên cần': return '/attendancechart';
        case 'Thống kê nhân viên': return '/employeechart';
        case 'Nhật ký hệ thống': return '/systemlog';
        default: return '/';
    }
}

const Sidebar = ({ groups, activeItem, onItemClick }) => {
    const { logout } = useAuth();
    const navigate = useNavigate();
    const [showMenu, setShowMenu] = useState(false);
    const menuRef = useRef(null);

    const normalGroups = groups.filter(g => !g.isUserInfo);
    const userGroup = groups.find(g => g.isUserInfo);

    const handleClick = (item) => {
        const route = getRouteFromItem(item);
        onItemClick(item);
        navigate(route);
    };

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setShowMenu(false);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            const activeElement = document.querySelector('.sidebar li.active');
            if (activeElement) {
                activeElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }, 50);
        return () => clearTimeout(timer);
    }, [activeItem]);

    return (
        <div className="sidebar">
            <div className="sidebar-logo">
                <img src={logo} alt="Logo" className="logo-image" />
                <span className="logo-text">HRM</span>
            </div>

            {normalGroups.map(group => (
                <div key={group.title} className="sidebar-group">
                    <hr className="sidebar-separator" />
                    <h3>{group.title}</h3>
                    <ul>
                        {group.items.map(item => (
                            <li
                                key={item}
                                className={activeItem === item ? 'active' : ''}
                                onClick={() => handleClick(item)}
                            >
                                <i className={getIconClass(item)} style={{ marginRight: 8 }} />
                                {item}
                            </li>
                        ))}
                    </ul>
                </div>
            ))}

            {userGroup && userGroup.user && (
                <div className="sidebar-user-info">
                    <img src={userGroup.user.avatarUrl} alt="Avatar" className="user-avatar" />
                    <div className="user-info-text">
                        <div className="user-name">{userGroup.user.name}</div>
                        <div className="view-profile">View Profile</div>
                    </div>

                    <div className="user-settings" ref={menuRef}>
                        <button className="btn-settings" onClick={() => setShowMenu(!showMenu)}>
                            <i className="fa fa-cog" />
                        </button>

                        {showMenu && (
                            <div className="settings-menu">
                                <button onClick={handleLogout}>
                                    <i className="fa fa-sign-out-alt" style={{ marginRight: 8 }} />
                                    Đăng xuất
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

export default Sidebar;
