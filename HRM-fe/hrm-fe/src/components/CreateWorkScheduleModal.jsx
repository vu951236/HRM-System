import React, { useState, useEffect } from 'react';
import { getEmployees, getShifts } from '../services/DataApi';

const CreateWorkScheduleModal = ({ onClose, onCreate }) => {
    const [formData, setFormData] = useState({
        employeeId: '',
        shiftId: '',
        workDate: '',
        status: 'planned',
        note: '',
        isDelete: false
    });

    const [employees, setEmployees] = useState([]);
    const [shifts, setShifts] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                const [empData, shiftData] = await Promise.all([
                    getEmployees(),
                    getShifts()
                ]);
                setEmployees(empData || []);
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

    const handleSubmit = () => {
        onCreate({
            employeeId: formData.employeeId ? Number(formData.employeeId) : null,
            shiftId: formData.shiftId ? Number(formData.shiftId) : null,
            workDate: formData.workDate,
            status: formData.status,
            note: formData.note || null,
            isDelete: false
        });
        onClose();
    };

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo lịch làm việc</h2>

                <div className="form-group">
                    <label>Nhân viên</label>
                    <select
                        name="employeeId"
                        value={formData.employeeId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn nhân viên --</option>
                        {employees.map(emp => (
                            <option key={emp.id} value={emp.id}>
                                {emp.employeeCode} - {emp.fullName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Ca làm</label>
                    <select
                        name="shiftId"
                        value={formData.shiftId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn ca --</option>
                        {shifts.map(shift => (
                            <option key={shift.id} value={shift.id}>
                                {shift.name} ({shift.startTime} - {shift.endTime})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Ngày làm</label>
                    <input
                        type="date"
                        name="workDate"
                        value={formData.workDate}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Trạng thái</label>
                    <select
                        name="status"
                        value={formData.status}
                        onChange={handleChange}
                    >
                        <option value="planned">Kế hoạch</option>
                        <option value="completed">Hoàn thành</option>
                        <option value="changed">Thay đổi</option>
                        <option value="absent">Vắng mặt</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Ghi chú</label>
                    <textarea
                        name="note"
                        value={formData.note}
                        onChange={handleChange}
                        placeholder="Ghi chú (nếu có)..."
                    />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Lưu lịch làm</button>
                </div>
            </div>
        </div>
    );
};

export default CreateWorkScheduleModal;
