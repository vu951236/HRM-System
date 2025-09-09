import React, { useState, useEffect } from 'react';

const ExtendContractModal = ({ contract, onClose, onExtend }) => {
    const [newEndDate, setNewEndDate] = useState('');

    useEffect(() => {
        if (contract?.endDate) {
            setNewEndDate(contract.endDate.split('T')[0]);
        }
    }, [contract]);

    const handleSubmit = () => {
        onExtend(contract.id, newEndDate);
        onClose();
    };

    if (!contract) return null;

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>🕒 Gia hạn hợp đồng</h2>
                <div className="form-group">
                    <label>Contract Type</label>
                    <input type="text" value={contract.contractTypeName} disabled />
                </div>
                <div className="form-group">
                    <label>Current End Date</label>
                    <input type="date" value={contract.endDate ? contract.endDate.split('T')[0] : ''} disabled />
                </div>
                <div className="form-group">
                    <label>New End Date</label>
                    <input
                        type="date"
                        value={newEndDate}
                        onChange={(e) => setNewEndDate(e.target.value)}
                    />
                </div>
                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Extend</button>
                </div>
            </div>
        </div>
    );
};

export default ExtendContractModal;
