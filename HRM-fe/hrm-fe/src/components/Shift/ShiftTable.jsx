import React from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const ShiftTable = ({ data, onEdit, onDelete, onRestore }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên ca</th>
                    <th>Shift Rule</th>
                    <th>Giờ bắt đầu</th>
                    <th>Giờ kết thúc</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                {data.map((shift, index) => (
                    <tr key={shift.id} style={{ opacity: shift.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{shift.name}</td>
                        <td>{shift.shiftRule ? shift.shiftRule.ruleName : 'N/A'}</td>
                        <td>{shift.startTime || 'N/A'}</td>
                        <td>{shift.endTime || 'N/A'}</td>
                        <td className="action-icons">
                            {!shift.isDelete ? (
                                <>
                                    <i
                                        className="fa fa-pencil"
                                        style={{ cursor: 'pointer', marginRight: '10px' }}
                                        title="Chỉnh sửa"
                                        onClick={() => onEdit(shift)}
                                    />
                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa ca làm việc này?')) {
                                                    onDelete(shift.id);
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
                                        if (window.confirm('Bạn có chắc muốn khôi phục ca làm việc này?')) {
                                            onRestore(shift.id);
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

export default ShiftTable;
