import React from 'react';
import { usePermission } from '../Permission/usePermission.js';
import ActionIcons from '../Permission/ActionIcons.jsx';

const OvertimeRecordTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
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
                    <th>Ngày</th>
                    <th>Thời gian</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Người phê duyệt</th>
                    <th>Ngày tạo</th>
                    {canSeeActionColumn && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((record, index) => (
                    <tr key={record.id} style={{ opacity: record.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{record.employeeFullName} ({record.employeeCode})</td>
                        <td>{record.date}</td>
                        <td>{record.startTime} - {record.endTime}</td>
                        <td>{record.reason || 'N/A'}</td>
                        <td>{statusMap[record.status] || record.status}</td>
                        <td>{record.approvedByFullName || 'N/A'}</td>
                        <td>{record.createdAt ? new Date(record.createdAt).toLocaleString() : 'N/A'}</td>

                        {canSeeActionColumn && (
                            <ActionIcons
                                item={record}
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

export default OvertimeRecordTable;
