import React from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const PayrollTable = ({ data, onApprove }) => {
    const { user } = useAuth();

    const statusMap = {
        PENDING: 'Chờ xử lý',
        APPROVED: 'Đã phê duyệt',
        REJECTED: 'Bị từ chối'
    };

    const isAdmin = user?.role === 'admin';
    const isHrNormal = user?.role === 'hr' && user?.positionName !== 'Head of Department';
    const isHrHod = user?.role === 'hr' && user?.positionName === 'Head of Department';
    const isStaffHod = user?.role === 'staff' && user?.positionName === 'Head of Department';

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Nhân viên</th>
                    <th>Tháng</th>
                    <th>Năm</th>
                    <th>Lương cơ bản</th>
                    <th>Lương làm thêm</th>
                    <th>Trừ trễ</th>
                    <th>Trừ phép</th>
                    <th>Bonus</th>
                    <th>Khoản khác trừ</th>
                    <th>Tổng lương</th>
                    <th>Trạng thái</th>
                    <th>Ngày tạo</th>
                    {(isAdmin || isHrNormal || isHrHod || isStaffHod) && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((payroll, index) => {
                    const canApprove =
                        payroll.status === 'GENERATED' &&
                        (
                            isAdmin ||
                            (isHrNormal && payroll.employeePositionName === 'Staff') ||
                            (isHrHod && payroll.userId !== Number(user.userId)) ||
                            (isStaffHod && payroll.employeePositionName === 'Staff' && payroll.departmentId === user.departmentId)
                        );

                    return (
                        <tr key={payroll.id}>
                            <td>{index + 1}</td>
                            <td>{payroll.employeeName || `User #${payroll.userId}`} ({payroll.employeeCode})</td>
                            <td>{payroll.month}</td>
                            <td>{payroll.year}</td>
                            <td>{payroll.baseSalary?.toLocaleString() || 0}</td>
                            <td>{payroll.overtimeSalary?.toLocaleString() || 0}</td>
                            <td>{payroll.leaveDeduction?.toLocaleString() || 0}</td>
                            <td>{payroll.lateDeduction?.toLocaleString() || 0}</td>
                            <td>{payroll.bonus?.toLocaleString() || 0}</td>
                            <td>{payroll.deduction?.toLocaleString() || 0}</td>
                            <td>{payroll.finalSalary?.toLocaleString() || 0}</td>
                            <td>{statusMap[payroll.status] || payroll.status}</td>
                            <td>{payroll.generatedAt ? new Date(payroll.generatedAt).toLocaleString() : 'N/A'}</td>

                            {(isAdmin || isHrNormal || isHrHod || isStaffHod) && (
                                <td className="action-icons">
                                    {canApprove && (
                                        <i
                                            className="fa fa-check"
                                            style={{ cursor: 'pointer', color: 'green' }}
                                            title="Phê duyệt"
                                            onClick={() => onApprove(payroll.id)}
                                        />
                                    )}
                                </td>
                            )}
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </div>
    );
};

export default PayrollTable;
