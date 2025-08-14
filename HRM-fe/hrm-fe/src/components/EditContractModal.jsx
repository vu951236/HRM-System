import React, { useState, useEffect } from 'react';
import { getContractTypes } from '../services/DataApi';

const EditContractModal = ({ contract, onClose, onUpdate }) => {
    const [formData, setFormData] = useState({
        contractTypeName: '',
        startDate: '',
        endDate: '',
        salary: ''
    });

    const [contractTypes, setContractTypes] = useState([]);

    useEffect(() => {
        if (contract) {
            setFormData({
                contractTypeName: contract.contractTypeName || '',
                startDate: contract.startDate || '',
                endDate: contract.endDate || '',
                salary: contract.salary || ''
            });
        }

        getContractTypes().then(types => setContractTypes(types))
            .catch(err => console.error(err));
    }, [contract]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        onUpdate(contract.id, {
            contractTypeName: formData.contractTypeName,
            startDate: formData.startDate || null,
            endDate: formData.endDate || null,
            salary: formData.salary ? Number(formData.salary) : null
        });
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>✏️ Chỉnh sửa hợp đồng</h2>

                <div className="form-group">
                    <label>Contract Type</label>
                    <select name="contractTypeName" value={formData.contractTypeName} onChange={handleChange}>
                        <option value="">-- Chọn loại hợp đồng --</option>
                        {contractTypes.map((type) => (
                            <option key={type.id} value={type.name}>{type.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Start Date</label>
                    <input type="date" name="startDate" value={formData.startDate} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>End Date</label>
                    <input type="date" name="endDate" value={formData.endDate} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Salary</label>
                    <input type="number" name="salary" value={formData.salary} onChange={handleChange} />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Update Contract</button>
                </div>
            </div>
        </div>
    );
};

export default EditContractModal;
