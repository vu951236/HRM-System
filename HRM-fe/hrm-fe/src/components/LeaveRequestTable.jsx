import React from 'react';
import { useAuth } from '../context/AuthContext';

const LeaveRequestTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
    const { user } = useAuth();

    const statusMap = {
        pending: 'Chờ xử lý',
        approved: 'Đã phê duyệt',
        rejected: 'Bị từ chối'
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
                    <th>Chính sách nghỉ phép</th>
                    <th>Từ ngày</th>
                    <th>Đến ngày</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Người phê duyệt</th>
                    <th>Ngày tạo</th>
                    {(isAdmin || isHrNormal || isHrHod || isStaffHod) && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((leave, index) => {
                    const canApproveOrReject =
                        !leave.isDelete &&
                        leave.status === 'pending' &&
                        (
                            isAdmin ||
                            (isHrNormal && leave.employeePositionName === 'Staff') ||
                            (isHrHod && leave.userId !== Number(user.userId)) ||
                            (isStaffHod && leave.employeePositionName === 'Staff' && leave.departmentId === user.departmentId)
                        );

                    return (
                        <tr key={leave.id} style={{ opacity: leave.isDelete ? 0.5 : 1 }}>
                            <td>{index + 1}</td>
                            <td>
                                {leave.employeeFullName
                                    ? `${leave.employeeFullName} (${leave.employeeCode || 'N/A'})`
                                    : `User #${leave.userId}`}
                            </td>
                            <td>{leave.leavePolicyName || `Policy #${leave.leavePolicyId}`}</td>
                            <td>{leave.startDate}</td>
                            <td>{leave.endDate}</td>
                            <td>{leave.reason || 'N/A'}</td>
                            <td>{statusMap[leave.status] || leave.status}</td>
                            <td>{leave.approvedByName || 'N/A'}</td>
                            <td>{leave.createdAt ? new Date(leave.createdAt).toLocaleString() : 'N/A'}</td>

                            {(isAdmin || isHrNormal || isHrHod || isStaffHod) && (
                                <td className="action-icons">
                                    {canApproveOrReject && (
                                        <>
                                            <i
                                                className="fa fa-check"
                                                style={{ cursor: 'pointer', color: 'green', marginRight: '10px' }}
                                                title="Phê duyệt"
                                                onClick={() => onApprove(leave.id)}
                                            />
                                            <i
                                                className="fa fa-times"
                                                style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                                title="Từ chối"
                                                onClick={() => onReject(leave.id)}
                                            />
                                        </>
                                    )}

                                    {isAdmin && !leave.isDelete && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa yêu cầu nghỉ phép này?')) {
                                                    onDelete(leave.id);
                                                }
                                            }}
                                        />
                                    )}

                                    {isAdmin && leave.isDelete && (
                                        <i
                                            className="fa fa-undo"
                                            style={{ cursor: 'pointer', color: 'green' }}
                                            title="Khôi phục"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn khôi phục yêu cầu nghỉ phép này?')) {
                                                    onRestore(leave.id);
                                                }
                                            }}
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

export default LeaveRequestTable;
