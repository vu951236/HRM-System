import React from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const UserTable = ({ data, onEdit, onLock }) => {
    const { user } = useAuth();

    const getRoleLabel = (role) => {
        switch (role) {
            case 'admin':
                return 'Admin';
            case 'hr':
                return 'Quản lý nhân sự';
            case 'staff':
                return 'Nhân viên';
            default:
                return role;
        }
    };

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>UserID</th>
                    <th>Username</th>
                    <th>Full name</th>
                    <th>Email</th>
                    <th>Address</th>
                    <th>Phone</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {data.map((doc, index) => (
                    <tr key={doc.userId}>
                        <td>{index + 1}</td>
                        <td>{doc.userId}</td>
                        <td>{doc.username}</td>
                        <td>{doc.fullName}</td>
                        <td>{doc.email}</td>
                        <td>{doc.address}</td>
                        <td>{doc.phone}</td>
                        <td>{getRoleLabel(doc.role)}</td>
                        <td className="action-icons">
                            <i
                                className="fa fa-pencil"
                                style={{ cursor: 'pointer', marginRight: '10px' }}
                                title="Edit"
                                onClick={() => onEdit(doc)}
                            />
                            {user?.role === 'admin' && (
                                <i
                                    className={doc.isActive ? 'fa fa-unlock' : 'fa fa-lock'}
                                    style={{ cursor: 'pointer', color: doc.isActive ? 'green' : 'red' }}
                                    title={doc.isActive ? 'Khóa tài khoản' : 'Mở khóa tài khoản'}
                                    onClick={() => onLock(doc)}
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

export default UserTable;
