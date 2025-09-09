import React from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const RecordTable = ({ data, onEdit, onSoftDelete, onRestore }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Employee Code</th>
                    <th>User Name</th>
                    <th>Profile Name</th>
                    <th>Department</th>
                    <th>Position</th>
                    <th>Employment Type</th>
                    <th>Supervisor</th>
                    <th>Hire Date</th>
                    <th>Termination Date</th>
                    <th>Work Location</th>
                    <th>Note</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {data.map((doc, index) => (
                    <tr key={doc.id} style={{ opacity: doc.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{doc.employeeCode}</td>
                        <td>{doc.userName}</td>
                        <td>{doc.profileName}</td>
                        <td>{doc.departmentName}</td>
                        <td>{doc.positionName}</td>
                        <td>{doc.employmentTypeName}</td>
                        <td>{doc.supervisorName}</td>
                        <td>{doc.hireDate ? new Date(doc.hireDate).toLocaleDateString() : 'N/A'}</td>
                        <td>{doc.terminationDate ? new Date(doc.terminationDate).toLocaleDateString() : 'N/A'}</td>
                        <td>{doc.workLocation}</td>
                        <td>{doc.note}</td>
                        <td className="action-icons">
                            {!doc.isDelete ? (
                                <>
                                    <i
                                        className="fa fa-pencil"
                                        style={{ cursor: 'pointer', marginRight: '10px' }}
                                        title="Edit"
                                        onClick={() => onEdit(doc)}
                                    />
                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red' }}
                                            title="Soft Delete"
                                            onClick={() => onSoftDelete(doc.id)}
                                        />
                                    )}

                                </>
                            ) : (
                                <i
                                    className="fa fa-undo"
                                    style={{ cursor: 'pointer', color: 'green' }}
                                    title="Restore"
                                    onClick={() => onRestore(doc.id)}
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

export default RecordTable;
