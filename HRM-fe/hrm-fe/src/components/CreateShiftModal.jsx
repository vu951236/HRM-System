import React, { useState, useEffect } from 'react';
import { getShiftRules } from '../services/DataApi';

const CreateShiftModal = ({ onClose, onCreate }) => {
    const [formData, setFormData] = useState({
        name: '',
        startTime: '',
        endTime: '',
        breakTime: '',
        description: '',
        shiftRuleId: '',
        isDelete: false
    });

    const [shiftRules, setShiftRules] = useState([]);

    useEffect(() => {
        getShiftRules()
            .then(data => setShiftRules(data))
            .catch(err => console.error("Error fetching shift rules:", err));
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = () => {
        onCreate({
            name: formData.name,
            startTime: formData.startTime || null,
            endTime: formData.endTime || null,
            breakTime: formData.breakTime || null,
            description: formData.description || null,
            shiftRuleId: formData.shiftRuleId ? Number(formData.shiftRuleId) : null,
            isDelete: false
        });
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>🕒 Tạo ca làm việc</h2>

                <div className="form-group">
                    <label>Tên ca</label>
                    <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="Ví dụ: Ca sáng"
                    />
                </div>

                <div className="form-group">
                    <label>Giờ bắt đầu</label>
                    <input
                        type="time"
                        name="startTime"
                        value={formData.startTime}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Giờ kết thúc</label>
                    <input
                        type="time"
                        name="endTime"
                        value={formData.endTime}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Thời gian nghỉ (break)</label>
                    <input
                        type="time"
                        name="breakTime"
                        value={formData.breakTime}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Mô tả</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Mô tả ca làm việc..."
                    />
                </div>

                <div className="form-group">
                    <label>Quy tắc ca</label>
                    <select
                        name="shiftRuleId"
                        value={formData.shiftRuleId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn quy tắc ca --</option>
                        {shiftRules.map(rule => (
                            <option key={rule.id} value={rule.id}>
                                {rule.ruleName}
                            </option>
                        ))}
                    </select>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Lưu ca làm việc</button>
                </div>
            </div>
        </div>
    );
};

export default CreateShiftModal;
