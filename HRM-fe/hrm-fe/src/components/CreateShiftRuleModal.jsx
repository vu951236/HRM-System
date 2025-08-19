import React, { useState } from 'react';

const CreateShiftRuleModal = ({ onClose, onCreate }) => {
    const [formData, setFormData] = useState({
        ruleName: '',
        description: '',
        maxHoursPerDay: '',
        allowOvertime: false,
        nightShiftMultiplier: '',
        isDelete: false
    });

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleSubmit = () => {
        onCreate({
            ruleName: formData.ruleName,
            description: formData.description || null,
            maxHoursPerDay: formData.maxHoursPerDay ? Number(formData.maxHoursPerDay) : null,
            allowOvertime: formData.allowOvertime,
            nightShiftMultiplier: formData.nightShiftMultiplier ? parseFloat(formData.nightShiftMultiplier) : null,
            isDelete: false
        });
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo quy tắc ca</h2>

                <div className="form-group">
                    <label>Tên quy tắc</label>
                    <input
                        type="text"
                        name="ruleName"
                        value={formData.ruleName}
                        onChange={handleChange}
                        placeholder="Ví dụ: Ca sáng"
                    />
                </div>

                <div className="form-group">
                    <label>Mô tả</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Mô tả quy tắc ca..."
                    />
                </div>

                <div className="form-group">
                    <label>Max giờ/ngày</label>
                    <input
                        type="number"
                        name="maxHoursPerDay"
                        value={formData.maxHoursPerDay}
                        onChange={handleChange}
                        min="0"
                    />
                </div>

                <div className="form-group">
                    <label>
                        <input
                            type="checkbox"
                            name="allowOvertime"
                            checked={formData.allowOvertime}
                            onChange={handleChange}
                        />{' '}
                        Cho phép OT
                    </label>
                </div>

                <div className="form-group">
                    <label>Hệ số ca đêm</label>
                    <input
                        type="number"
                        step="0.1"
                        name="nightShiftMultiplier"
                        value={formData.nightShiftMultiplier}
                        onChange={handleChange}
                    />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Lưu quy tắc</button>
                </div>
            </div>
        </div>
    );
};

export default CreateShiftRuleModal;
