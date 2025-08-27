import React from 'react';
import { useAuth } from '../context/AuthContext';

const LeavePolicyTable = ({ data, onEdit, onDelete, onRestore }) => {
    const { user } = useAuth();

    const getDisplayPosition = (role, position) => {
        if (role === 'staff' && position === 'Staff') return 'Nhân viên';
        if (role === 'staff' && position === 'Head of Department') return 'Trưởng phòng';
        if (role === 'hr' && position === 'Staff') return 'Nhân viên HR';
        if (role === 'hr' && position === 'Head of Department') return 'Trưởng phòng HR';
        return position ?? 'N/A';
    };

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên chính sách</th>
                    <th>Mô tả</th>
                    <th>Max ngày nghỉ</th>
                    <th>Vị trí áp dụng</th>
                    {user?.role === 'admin' && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((policy, index) => (
                    <tr key={policy.id} style={{ opacity: policy.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{policy.policyName}</td>
                        <td>{policy.description || 'N/A'}</td>
                        <td>{policy.maxDays ?? 'N/A'}</td>
                        <td>{getDisplayPosition(policy.roleName?.toLowerCase(), policy.positionName)}</td>
                        {user?.role === 'admin' && (
                            <td className="action-icons">
                                {!policy.isDelete ? (
                                    <>
                                        <i
                                            className="fa fa-pencil"
                                            style={{ cursor: 'pointer', marginRight: '10px' }}
                                            title="Chỉnh sửa"
                                            onClick={() => onEdit(policy)}
                                        />
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                            title="Xóa"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa chính sách nghỉ này?')) {
                                                    onDelete(policy.id);
                                                }
                                            }}
                                        />
                                    </>
                                ) : (
                                    <i
                                        className="fa fa-undo"
                                        style={{ cursor: 'pointer', color: 'green' }}
                                        title="Khôi phục"
                                        onClick={() => {
                                            if (window.confirm('Bạn có chắc muốn khôi phục chính sách nghỉ này?')) {
                                                onRestore(policy.id);
                                            }
                                        }}
                                    />
                                )}
                            </td>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default LeavePolicyTable;
