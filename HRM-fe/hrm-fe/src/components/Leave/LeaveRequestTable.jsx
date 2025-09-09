import React from 'react';
import { usePermission } from '../Permission/usePermission.js';
import ActionIcons from '../Permission/ActionIcons.jsx';

const LeaveRequestTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
    const { canApproveOrReject, canSeeActionColumn, isAdmin } = usePermission();

    const statusMap = {
        pending: 'Chờ xử lý',
        approved: 'Đã phê duyệt',
        rejected: 'Bị từ chối'
    };

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
                    {canSeeActionColumn && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((leave, index) => (
                    <tr key={leave.id} style={{ opacity: leave.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{leave.employeeFullName ? `${leave.employeeFullName} (${leave.employeeCode || 'N/A'})` : `User #${leave.userId}`}</td>
                        <td>{leave.leavePolicyName || `Policy #${leave.leavePolicyId}`}</td>
                        <td>{leave.startDate}</td>
                        <td>{leave.endDate}</td>
                        <td>{leave.reason || 'N/A'}</td>
                        <td>{statusMap[leave.status] || leave.status}</td>
                        <td>{leave.approvedByName || 'N/A'}</td>
                        <td>{leave.createdAt ? new Date(leave.createdAt).toLocaleString() : 'N/A'}</td>

                        {canSeeActionColumn && (
                            <ActionIcons
                                item={leave}
                                canApproveOrReject={canApproveOrReject}
                                isAdmin={isAdmin}
                                onApprove={onApprove}
                                onReject={onReject}
                                onDelete={onDelete}
                                onRestore={onRestore}
                            />
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LeaveRequestTable;
