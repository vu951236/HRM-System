import React from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const ShiftRuleTable = ({ data, onEdit, onDelete, onRestore }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên quy tắc</th>
                    <th>Mô tả</th>
                    <th>Max giờ/ngày</th>
                    <th>Hành động</th>
                </tr>
                </thead>
                <tbody>
                {data.map((rule, index) => (
                    <tr key={rule.id} style={{ opacity: rule.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{rule.ruleName}</td>
                        <td>{rule.description || 'N/A'}</td>
                        <td>{rule.maxHoursPerDay ?? 'N/A'}</td>
                        <td className="action-icons">
                            {!rule.isDelete ? (
                                <>
                                    <i
                                        className="fa fa-pencil"
                                        style={{ cursor: 'pointer', marginRight: '10px' }}
                                        title="Chỉnh sửa"
                                        onClick={() => onEdit(rule)}
                                    />
                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa quy tắc ca này?')) {
                                                    onDelete(rule.id);
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
                                        if (window.confirm('Bạn có chắc muốn khôi phục quy tắc ca này?')) {
                                            onRestore(rule.id);
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

export default ShiftRuleTable;
