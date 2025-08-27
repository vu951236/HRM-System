import React from 'react';
import { useAuth } from '../context/AuthContext';

const ShiftSwapRequestTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
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
                    <th>Người yêu cầu</th>
                    <th>Ca yêu cầu</th>
                    <th>Người được đổi</th>
                    <th>Ca thay thế</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Người phê duyệt</th>
                    <th>Ngày tạo</th>
                    {(isAdmin || isHrNormal || isHrHod || isStaffHod) && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((request, index) => {
                    const canApproveOrReject =
                        !request.isDelete &&
                        request.status === 'pending' &&
                        (
                            isAdmin ||
                            (isHrNormal && request.requesterPositionName === 'Staff') ||
                            (isHrHod && request.userId !== Number(user.userId)) ||
                            (isStaffHod && request.requesterPositionName === 'Staff' && request.departmentId === user.departmentId)
                        );

                    return (
                        <tr key={request.id} style={{ opacity: request.isDelete ? 0.5 : 1 }}>
                            <td>{index + 1}</td>
                            <td>{request.requesterFullName} ({request.requesterCode})</td>
                            <td>{request.requestedShiftName} - {request.requestedShiftTime}</td>
                            <td>{request.targetEmployeeFullName} ({request.targetEmployeeCode})</td>
                            <td>{request.targetShiftName} - {request.targetShiftTime}</td>
                            <td>{request.reason || 'N/A'}</td>
                            <td>{statusMap[request.status] || request.status}</td>
                            <td>{request.approvedByFullName || 'N/A'}</td>
                            <td>{request.createdAt ? new Date(request.createdAt).toLocaleString() : 'N/A'}</td>

                            {(isAdmin || isHrNormal || isHrHod || isStaffHod) && (
                                <td className="action-icons">
                                    {canApproveOrReject && (
                                        <>
                                            <i
                                                className="fa fa-check"
                                                style={{ cursor: 'pointer', color: 'green', marginRight: '10px' }}
                                                title="Phê duyệt"
                                                onClick={() => onApprove(request.id)}
                                            />
                                            <i
                                                className="fa fa-times"
                                                style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                                title="Từ chối"
                                                onClick={() => onReject(request.id)}
                                            />
                                        </>
                                    )}

                                    {isAdmin && !request.isDelete && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa yêu cầu này?')) {
                                                    onDelete(request.id);
                                                }
                                            }}
                                        />
                                    )}

                                    {isAdmin && request.isDelete && (
                                        <i
                                            className="fa fa-undo"
                                            style={{ cursor: 'pointer', color: 'green' }}
                                            title="Khôi phục"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn khôi phục yêu cầu này?')) {
                                                    onRestore(request.id);
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

export default ShiftSwapRequestTable;
