import React from 'react';
import { useAuth } from '../context/AuthContext';

const WorkScheduleTable = ({ data, onEdit, onDelete, onRestore }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Nhân viên</th>
                    <th>Ca làm</th>
                    <th>Quy tắc ca</th>
                    <th>Ngày làm</th>
                    <th>Trạng thái</th>
                    <th>Ghi chú</th>
                    <th>Ngày tạo</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                {data.map((schedule, index) => (
                    <tr key={schedule.id} style={{ opacity: schedule.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{schedule.employeeName} ({schedule.employeeCode})</td>
                        <td>{schedule.shiftName}</td>
                        <td>{schedule.shiftRuleName}</td>
                        <td>{schedule.workDate}</td>
                        <td>{schedule.status}</td>
                        <td>{schedule.note || 'N/A'}</td>
                        <td>{schedule.createdAt ? new Date(schedule.createdAt).toLocaleString() : 'N/A'}</td>
                        <td className="action-icons">
                            {!schedule.isDelete ? (
                                <>
                                    {(user?.role === 'admin' || (user?.role === 'hr' && schedule.userId !== Number(user.userId))) && (
                                        <i
                                        className="fa fa-pencil"
                                        style={{ cursor: 'pointer', marginRight: '10px' }}
                                        title="Chỉnh sửa"
                                        onClick={() => onEdit(schedule)}
                                    />
                                    )}

                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa lịch làm việc này?')) {
                                                    onDelete(schedule.id);
                                                }
                                            }}
                                        />
                                    )}
                                </>
                            ) : (
                                <i
                                    className="fa fa-undo"
                                    style={{ cursor: 'pointer', color: 'green' }}
                                    title="Khôi phục"
                                    onClick={() => {
                                        if (window.confirm('Bạn có chắc muốn khôi phục lịch làm việc này?')) {
                                            onRestore(schedule.id);
                                        }
                                    }}
                                />
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default WorkScheduleTable;
