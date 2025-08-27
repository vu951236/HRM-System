import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const CreateLeavePolicyModal = ({ onClose, onCreate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        policyName: '',
        maxDays: '',
        description: '',
        rolePosition: ''
    });

    const [hasAccess, setHasAccess] = useState(true);

    useEffect(() => {
        if (user?.role?.toLowerCase() !== "admin") {
            console.warn("Chỉ Admin mới có thể tạo chính sách nghỉ");
            setHasAccess(false);
        }
    }, [user?.role]);

    const rolePositionMap = {
        'Nhân viên': { roleName: 'staff', positionName: 'Staff' },
        'Trưởng phòng': { roleName: 'staff', positionName: 'Head of Department' },
        'Nhân viên HR': { roleName: 'hr', positionName: 'Staff' },
        'Trưởng phòng HR': { roleName: 'hr', positionName: 'Head of Department' }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = () => {
        const mapping = rolePositionMap[formData.rolePosition] || {};

        onCreate({
            policyName: formData.policyName,
            maxDays: formData.maxDays ? Number(formData.maxDays) : null,
            description: formData.description || null,
            positionName: mapping.positionName || null,
            roleName: mapping.roleName || null
        });
        onClose();
    };

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ Admin mới có thể tạo chính sách nghỉ</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo chính sách nghỉ</h2>

                <div className="form-group">
                    <label>Tên chính sách</label>
                    <input
                        type="text"
                        name="policyName"
                        value={formData.policyName}
                        onChange={handleChange}
                        placeholder="Ví dụ: Nghỉ phép năm"
                    />
                </div>

                <div className="form-group">
                    <label>Mô tả</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Mô tả chính sách..."
                    />
                </div>

                <div className="form-group">
                    <label>Số ngày tối đa</label>
                    <input
                        type="number"
                        name="maxDays"
                        value={formData.maxDays}
                        onChange={handleChange}
                        min="0"
                    />
                </div>

                <div className="form-group">
                    <label>Áp dụng cho</label>
                    <select
                        name="rolePosition"
                        value={formData.rolePosition}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn đối tượng áp dụng --</option>
                        <option value="Nhân viên">Nhân viên</option>
                        <option value="Trưởng phòng">Trưởng phòng</option>
                        <option value="Nhân viên HR">Nhân viên HR</option>
                        <option value="Trưởng phòng HR">Trưởng phòng HR</option>
                    </select>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Lưu chính sách</button>
                </div>
            </div>
        </div>
    );
};

export default CreateLeavePolicyModal;
