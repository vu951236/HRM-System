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
        case 'Thống kê tổng số nhân viên': return 'fa fa-users';
        case 'Thống kê chuyên cần': return 'fa fa-calendar-check';
        case 'Thống kê theo phòng ban': return 'fa fa-building';
        case 'Xuất bảng lương': return 'fa fa-file-invoice-dollar';
        case 'Xuất bảng công': return 'fa fa-file-alt';
        case 'Xuất danh sách nhân viên': return 'fa fa-list';
        case 'Đăng ký nghỉ phép': return 'fa fa-calendar-plus';
        case 'Tự động trừ phép': return 'fa fa-calculator';
        case 'Theo dõi lịch sử phép': return 'fa fa-history';
        case 'Phê duyệt nghỉ phép': return 'fa fa-check-circle';
        case 'Cấu hình chính sách phép': return 'fa fa-cogs';
        case 'Quản lý nhân viên': return 'fa fa-user';
        case 'Quản lý hồ sơ': return 'fa fa-file';
        case 'Quản lý hợp đồng': return 'fa fa-file-text';
        case 'Lưu trữ thông tin nhân viên': return 'fa fa-archive';
        case 'Ghi nhận chấm công': return 'fa fa-clipboard-check';
        case 'Phê duyệt công tăng ca': return 'fa fa-user-clock';
        case 'Cài đặt ca làm việc': return 'fa fa-sliders-h';
        case 'Tổng hợp bảng công': return 'fa fa-chart-pie';
        case 'Quản lý sai sót chấm công': return 'fa fa-exclamation-triangle';
        case 'Tính lương': return 'fa fa-money-bill-wave';
        case 'Cấu hình bảng lương': return 'fa fa-wrench';
        case 'Kết nối chuyển khoản': return 'fa fa-university';
        case 'Xuất phiếu lương': return 'fa fa-file-invoice';
        case 'Duyệt bảng lương': return 'fa fa-thumbs-up';
        case 'Tạo hợp đồng': return 'fa fa-file-signature';
        case 'Gia hạn / Chấm dứt hợp đồng': return 'fa fa-handshake';
        case 'Lưu trữ lịch sử hợp đồng': return 'fa fa-folder-open';
        default: return 'fa fa-folder';
    }
}

function getRouteFromItem(item) {
    switch (item) {
        case 'Thống kê tổng số nhân viên': return '/dashboard/employees';
        case 'Sửa thông tin cá nhân': return '/editprofile';
        case 'Đổi mật khẩu': return '/changepass';
        case 'Thống kê chuyên cần': return '/dashboard/attendance';
        case 'Thống kê theo phòng ban': return '/dashboard/department';
        case 'Xuất bảng lương': return '/export/salary';
        case 'Xuất bảng công': return '/export/attendance';
        case 'Xuất danh sách nhân viên': return '/export/employees';
        case 'Đăng ký nghỉ phép': return '/leave/register';
        case 'Tự động trừ phép': return '/leave/auto-deduct';
        case 'Theo dõi lịch sử phép': return '/leave/history';
        case 'Phê duyệt nghỉ phép': return '/leave/approve';
        case 'Cấu hình chính sách phép': return '/leave/policy';
        case 'Quản lý nhân viên': return '/employee';
        case 'Quản lý hồ sơ': return '/record';
        case 'Quản lý hợp đồng': return '/contract';
        case 'Lưu trữ thông tin nhân viên': return '/employee/archive';
        case 'Ghi nhận chấm công': return '/attendance/log';
        case 'Phê duyệt công tăng ca': return '/attendance/overtime-approve';
        case 'Cài đặt ca làm việc': return '/attendance/schedule';
        case 'Tổng hợp bảng công': return '/attendance/summary';
        case 'Quản lý sai sót chấm công': return '/attendance/errors';
        case 'Tính lương': return '/salary/calculate';
        case 'Cấu hình bảng lương': return '/salary/config';
        case 'Kết nối chuyển khoản': return '/salary/banking';
        case 'Xuất phiếu lương': return '/salary/payslip';
        case 'Duyệt bảng lương': return '/salary/approve';
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
