import React from 'react';
import { useAuth } from '../context/AuthContext';

const OvertimeRecordTable = ({ data, onApprove, onReject, onDelete, onRestore }) => {
    const { user } = useAuth();

    const statusMap = {
        pending: 'Chờ xử lý',
        approved: 'Đã phê duyệt',
        rejected: 'Bị từ chối'
    };

    const isHr = user?.role === 'hr';
    const isAdmin = user?.role === 'admin';
    const isHeadOfDept = user?.role === 'staff' && user?.positionName === 'Head of Department';
    const isHrHod = user?.role === 'hr' && user?.positionName === 'Head of Department';
    const isHrNormal = user?.role === 'hr' && user?.positionName !== 'Head of Department';

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
                    {(isHr || isAdmin || isHeadOfDept) && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((record, index) => {
                    const canApproveOrReject =
                        !record.isDelete &&
                        record.status === 'pending' &&
                        (
                            isAdmin ||
                            (isHrNormal && record.employeePositionName === 'Staff') ||
                            (isHrHod && record.userId !== Number(user.userId)) ||
                            (isHeadOfDept && record.employeePositionName === 'Staff')
                        );

                    return (
                        <tr key={record.id} style={{ opacity: record.isDelete ? 0.5 : 1 }}>
                            <td>{index + 1}</td>
                            <td>{record.employeeFullName} ({record.employeeCode})</td>
                            <td>{record.date}</td>
                            <td>{record.startTime} - {record.endTime}</td>
                            <td>{record.reason || 'N/A'}</td>
                            <td>{statusMap[record.status] || record.status}</td>
                            <td>{record.approvedByFullName || 'N/A'}</td>
                            <td>{record.createdAt ? new Date(record.createdAt).toLocaleString() : 'N/A'}</td>

                            {(isHr || isAdmin || isHeadOfDept) && (
                                <td className="action-icons">
                                    {canApproveOrReject && (
                                        <>
                                            <i
                                                className="fa fa-check"
                                                style={{ cursor: 'pointer', color: 'green', marginRight: '10px' }}
                                                title="Phê duyệt"
                                                onClick={() => onApprove(record.id)}
                                            />
                                            <i
                                                className="fa fa-times"
                                                style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                                title="Từ chối"
                                                onClick={() => onReject(record.id)}
                                            />
                                        </>
                                    )}

                                    {isAdmin && !record.isDelete && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa bản ghi OT này?')) {
                                                    onDelete(record.id);
                                                }
                                            }}
                                        />
                                    )}

                                    {isAdmin && record.isDelete && (
                                        <i
                                            className="fa fa-undo"
                                            style={{ cursor: 'pointer', color: 'green' }}
                                            title="Khôi phục"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn khôi phục bản ghi OT này?')) {
                                                    onRestore(record.id);
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

export default OvertimeRecordTable;
