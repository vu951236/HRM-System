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

    const documentItems = [];
    if (role === 'admin' || role === 'hr') {
        documentItems.push('Quản lý hồ sơ', 'Quản lý hợp đồng');
    }
    if (role === 'admin') {
        documentItems.push('Quản lý nhân viên', 'Xem nhật ký hệ thống');
    }
    if (documentItems.length > 0) {
        groups.push({
            title: 'Quản lý nhân sự',
            items: documentItems,
        });
    }

    const attendanceItems = [];
    if (role === 'admin' || role === 'hr') {
        attendanceItems.push('Quản lý quy tắc ca', 'Quản lý ca làm việc', 'Quản lý mẫu lịch làm việc', 'Quản lý lịch làm việc', 'Quản lý yêu cầu đổi ca' , 'Quản lý tăng ca', 'Quản lý chấm công');
    }
    if (role === 'staff') {
        attendanceItems.push('Quản lý yêu cầu đổi ca', 'Quản lý tăng ca', 'Ghi nhận chấm công');
    }

    if (attendanceItems.length > 0) {
        groups.push({
            title: 'Quản lý thời gian làm việc',
            items: attendanceItems,
        });
    }

    if (role === 'hr') {
        groups.push({
            title: 'Tính lương',
            items: ['Tính lương', 'Cấu hình bảng lương', 'Kết nối chuyển khoản', 'Xuất phiếu lương', 'Duyệt bảng lương'],
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
