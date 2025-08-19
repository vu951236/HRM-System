import React from 'react';
import {useAuth} from "../context/AuthContext.jsx";

const ContractTable = ({ data, onEdit, onExtend, onTerminate, onSoftDelete, onRestore, onViewHistory }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>User ID</th>
                    <th>User Name</th>
                    <th>Contract Type</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Salary</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {data.map((contract, index) => (
                    <tr key={contract.id} style={{ opacity: contract.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{contract.userId}</td>
                        <td>{contract.userName}</td>
                        <td>{contract.contractTypeName}</td>
                        <td>{contract.startDate ? new Date(contract.startDate).toLocaleDateString() : 'N/A'}</td>
                        <td>{contract.endDate ? new Date(contract.endDate).toLocaleDateString() : 'N/A'}</td>
                        <td>{contract.salary}</td>
                        <td>{contract.status}</td>
                        <td className="action-icons">
                            {!contract.isDelete ? (
                                <>
                                    {contract.status === 'MODIFIED' && (
                                        <i
                                            className="fa fa-history"
                                            style={{ cursor: 'pointer', marginRight: '10px', color: 'blue' }}
                                            title="Xem lịch sử thay đổi"
                                            onClick={() => onViewHistory(contract)}
                                        />
                                    )}

                                    {!['TERMINATED', 'EXPIRED'].includes(contract.status)
                                        && (
                                        <i
                                            className="fa fa-pencil"
                                            style={{ cursor: 'pointer', marginRight: '10px' }}
                                            title="Edit"
                                            onClick={() => onEdit(contract)}
                                        />
                                    )}
                                    {contract.status !== 'TERMINATED' && (
                                        <i
                                            className="fa fa-clock"
                                            style={{ cursor: 'pointer', marginRight: '10px', color: 'orange' }}
                                            title="Extend"
                                            onClick={() => onExtend(contract)}
                                        />
                                    )}
                                    <i
                                        className="fa fa-ban"
                                        style={{ cursor: 'pointer', marginRight: '10px', color: 'red' }}
                                        title="Terminate"
                                        onClick={() => {
                                            if (window.confirm('Bạn có chắc muốn chấm dứt hợp đồng này?')) {
                                                onTerminate(contract.id);
                                            }
                                        }}

                                    />
                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red' }}
                                            title="Soft Delete"
                                            onClick={() => onSoftDelete(contract.id)}
                                        />
                                    )}

                                </>
                            ) : (
                                <i
                                    className="fa fa-undo"
                                    style={{ cursor: 'pointer', color: 'green' }}
                                    title="Restore"
                                    onClick={() => onRestore(contract.id)}
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

export default ContractTable;
