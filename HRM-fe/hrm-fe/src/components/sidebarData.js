export function getSidebarGroups(user) {
    const role = user?.role;

    const groups = [];

    const accountItems = [];
    if (role === 'admin' || role === 'hr' || role === 'staff') {
        accountItems.push('Sửa thông tin cá nhân', 'Đổi mật khẩu');
    }
    if (accountItems.length > 0) {
        groups.push({
            title: 'Tài khoản',
            items: accountItems,
        });
    }

    if (role === 'admin' || role === 'hr') {
        groups.push({
            title: 'Dashboard & Báo cáo',
            items: ['Thống kê tổng số nhân viên', 'Thống kê chuyên cần', 'Thống kê theo phòng ban', 'Xuất bảng lương', 'Xuất bảng công', 'Xuất danh sách nhân viên'],
        });
    }

    const leaveItems = [];
    if (role === 'staff') {
        leaveItems.push('Đăng ký nghỉ phép', 'Theo dõi lịch sử phép');
    }
    if (role === 'hr') {
        leaveItems.push('Phê duyệt nghỉ phép', 'Theo dõi lịch sử phép', 'Cấu hình chính sách phép');
    }
    if (leaveItems.length > 0) {
        groups.push({
            title: 'Nghỉ phép',
            items: leaveItems,
        });
    }

    if (role === 'admin') {
        groups.push({
            title: 'Quản lý hệ thống',
            items: ['Quản lý nhân viên', 'Xem nhật ký hệ thống'],
        });
    }

    if (role === 'hr') {
        groups.push({
            title: 'Quản lý nhân sự',
            items: ['Lưu trữ thông tin nhân viên', 'Cập nhật hồ sơ', 'Tạo hồ sơ nhân viên'],
        });
    }


    const attendanceItems = [];
    if (role === 'hr') {
        attendanceItems.push('Phê duyệt công tăng ca', 'Cài đặt ca làm việc', 'Tổng hợp bảng công', 'Quản lý sai sót chấm công');
    }
    if (role === 'staff') {
        attendanceItems.push('Ghi nhận chấm công');
    }

    if (attendanceItems.length > 0) {
        groups.push({
            title: 'Chấm công',
            items: attendanceItems,
        });
    }

    if (role === 'hr') {
        groups.push({
            title: 'Tính lương',
            items: ['Tính lương', 'Cấu hình bảng lương', 'Kết nối chuyển khoản', 'Xuất phiếu lương', 'Duyệt bảng lương'],
        });
    }

    if (role === 'hr') {
        groups.push({
            title: 'Hợp đồng lao động',
            items: ['Tạo hợp đồng', 'Gia hạn / Chấm dứt hợp đồng', 'Lưu trữ lịch sử hợp đồng'],
        });
    }

    groups.push({
        title: 'User Info',
        isUserInfo: true,
        user: {
            avatarUrl: user?.avatarUrl || 'https://static.vecteezy.com/system/resources/thumbnails/009/398/450/small_2x/man-avatar-clipart-illustration-free-png.png',
            name: user?.fullName || 'Người dùng',
        },
    });

    return groups;
}
