export function getSidebarGroups(user) {
    const role = user?.role;

    const groups = [];

    const accountItems = [];
    if (role === 'admin' || role === 'hr' || role === 'staff') {
        accountItems.push('Sửa thông tin cá nhân', 'Đổi mật khẩu');
    }
    if (role === 'admin') {
        accountItems.push('Nhật ký hệ thống');
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
            items: ['Thống kê nhân viên', 'Thống kê mức chuyên cần'],
        });
    }

    const documentItems = [];
    if (role === 'admin' || role === 'hr') {
        documentItems.push('Quản lý hồ sơ', 'Quản lý hợp đồng');
    }
    if (role === 'admin') {
        documentItems.push('Quản lý nhân viên');
    }
    if (documentItems.length > 0) {
        groups.push({
            title: 'Quản lý nhân sự',
            items: documentItems,
        });
    }

    const workItems = [];
    if (role === 'admin' || role === 'hr') {
        workItems.push('Quản lý quy tắc ca', 'Quản lý ca làm việc', 'Quản lý mẫu lịch làm việc', 'Quản lý lịch làm việc', 'Quản lý yêu cầu đổi ca' , 'Quản lý tăng ca', 'Quản lý chính sách nghỉ', 'Quản lý nghỉ phép');
    }
    if (role === 'staff') {
        workItems.push('Quản lý lịch làm việc', 'Yêu cầu đổi ca', 'Yêu cầu tăng ca', 'Yêu cầu nghỉ phép');
    }

    if (workItems.length > 0) {
        groups.push({
            title: 'Quản lý công việc',
            items: workItems,
        });
    }

    const salaryItems = [];
    if (role === 'admin' || role === 'hr') {
        salaryItems.push('Quản lý chính sách lương', 'Quản lý chấm công', 'Quản lý bảng lương');
    }
    if (role === 'staff') {
        salaryItems.push('Quản lý chấm công', 'Quản lý bảng lương');
    }

    if (salaryItems.length > 0) {
        groups.push({
            title: 'Quản lý lương',
            items: salaryItems,
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
