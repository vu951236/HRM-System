import React from 'react';
import { usePermission } from '../Permission/usePermission.js';
import ActionIcons from '../Permission/ActionIcons.jsx';

const ShiftSwapRequestTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
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
                    <th>Người yêu cầu</th>
                    <th>Ca yêu cầu</th>
                    <th>Người được đổi</th>
                    <th>Ca thay thế</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Người phê duyệt</th>
                    <th>Ngày tạo</th>
                    {canSeeActionColumn && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((request, index) => (
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

                        {canSeeActionColumn && (
                            <ActionIcons
                                item={request}
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

export default ShiftSwapRequestTable;
