import React, { useEffect, useState } from 'react';
import { fetchContractHistory } from '../../services/contractService.js';
import '../../styles/ContractHistoryViewModal.css';

const ContractHistoryModal = ({ contract, onClose }) => {
    const [history, setHistory] = useState([]);

    useEffect(() => {
        if (contract) {
            fetchContractHistory(contract.id).then(setHistory);
        }
    }, [contract]);

    if (!contract) return null;

    return (
        <div className="chm-modal-overlay">
            <div className="chm-modal-content">
                <div className="chm-modal-header">
                    <h2>Lịch sử thay đổi hợp đồng</h2>
                    <button className="chm-close-btn" onClick={onClose}>&times;</button>
                </div>
                <div className="chm-modal-body">
                    <div className="chm-table-container">
                        <table className="chm-table">
                            <thead>
                            <tr>
                                <th>Ngày thay đổi</th>
                                <th>Loại HĐ cũ</th>
                                <th>Loại HĐ mới</th>
                                <th>Ngày bắt đầu cũ</th>
                                <th>Ngày bắt đầu mới</th>
                                <th>Ngày kết thúc cũ</th>
                                <th>Ngày kết thúc mới</th>
                                <th>Lương cũ</th>
                                <th>Lương mới</th>
                                <th>Trạng thái cũ</th>
                                <th>Trạng thái mới</th>
                                <th>Ghi chú</th>
                            </tr>
                            </thead>
                            <tbody>
                            {history.map((h, idx) => (
                                <tr key={idx}>
                                    <td>{new Date(h.changedAt).toLocaleString()}</td>
                                    <td>{h.oldContractType}</td>
                                    <td>{h.newContractType}</td>
                                    <td>{h.oldStartDate}</td>
                                    <td>{h.newStartDate}</td>
                                    <td>{h.oldEndDate}</td>
                                    <td>{h.newEndDate}</td>
                                    <td>{h.oldSalary}</td>
                                    <td>{h.newSalary}</td>
                                    <td>{h.oldStatus}</td>
                                    <td>{h.newStatus}</td>
                                    <td>{h.note}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div className="chm-modal-footer">
                    <button className="chm-close-btn-footer" onClick={onClose}>Đóng</button>
                </div>
            </div>
        </div>
    );
};

export default ContractHistoryModal;
