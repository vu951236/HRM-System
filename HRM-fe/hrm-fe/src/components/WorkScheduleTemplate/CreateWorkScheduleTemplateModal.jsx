import React, { useState, useEffect } from 'react';
import { getDepartments, getShifts } from '../../services/DataApi.js';
import { useAuth } from '../../context/AuthContext.jsx';

const daysOfWeek = [
    { value: 1, label: "Thứ 2" },
    { value: 2, label: "Thứ 3" },
    { value: 3, label: "Thứ 4" },
    { value: 4, label: "Thứ 5" },
    { value: 5, label: "Thứ 6" },
    { value: 6, label: "Thứ 7" },
    { value: 7, label: "Chủ nhật" },
];

const CreateWorkScheduleTemplateModal = ({ onClose, onCreate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        templateName: '',
        departmentId: '',
        isDelete: false
    });

    const [departments, setDepartments] = useState([]);
    const [shifts, setShifts] = useState([]);
    const [shiftPattern, setShiftPattern] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                const depData = await getDepartments();
                const shiftData = await getShifts();
                setDepartments(depData || []);
                setShifts(shiftData || []);
            } catch (err) {
                console.error("Lỗi khi tải dữ liệu:", err);
            }
        }
        fetchData();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleAddPattern = () => {
        setShiftPattern([...shiftPattern, { day: 1, shiftId: shifts[0]?.id || null }]);
    };

    const handlePatternChange = (index, field, value) => {
        const updated = [...shiftPattern];
        updated[index][field] = Number(value);
        setShiftPattern(updated);
    };

    const handleSubmit = () => {
        const payload = {
            templateName: formData.templateName.trim(),
            departmentId: formData.departmentId ? Number(formData.departmentId) : null,
            shiftPattern: JSON.stringify(shiftPattern),
            ...(user?.userId ? { createdById: user.userId } : {}),
            isDelete: false
        };

        console.log("Payload gửi API:", payload);
        onCreate(payload);
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo Template Lịch Làm Việc</h2>

                <div className="form-group">
                    <label>Tên Template</label>
                    <input
                        type="text"
                        name="templateName"
                        value={formData.templateName}
                        onChange={handleChange}
                        placeholder="Nhập tên template..."
                    />
                </div>

                <div className="form-group">
                    <label>Phòng ban</label>
                    <select
                        name="departmentId"
                        value={formData.departmentId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn phòng ban --</option>
                        {departments.map(dep => (
                            <option key={dep.id} value={dep.id}>
                                {dep.name}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Mẫu ca (Shift Pattern)</label>
                    {shiftPattern.map((item, index) => (
                        <div key={index} className="pattern-row">
                            <select
                                value={item.day}
                                onChange={e => handlePatternChange(index, 'day', e.target.value)}
                            >
                                {daysOfWeek.map(d => (
                                    <option key={d.value} value={d.value}>{d.label}</option>
                                ))}
                            </select>

                            <select
                                value={item.shiftId}
                                onChange={e => handlePatternChange(index, 'shiftId', e.target.value)}
                            >
                                {shifts.map(s => (
                                    <option key={s.id} value={s.id}>{s.name}</option>
                                ))}
                            </select>
                        </div>
                    ))}
                    <button type="button" onClick={handleAddPattern}>+ Thêm ngày</button>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Lưu Template</button>
                </div>
            </div>
        </div>
    );
};

export default CreateWorkScheduleTemplateModal;
