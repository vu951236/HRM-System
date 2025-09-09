import React, { useState, useEffect } from 'react';
import { getShiftSwapOptions } from '../../services/DataApi.js';
import { useAuth } from '../../context/AuthContext.jsx';

const CreateShiftSwapRequestModal = ({ onClose, onCreate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        requesterId: user?.userId || '',
        requestedShiftId: '',
        targetEmployeeId: '',
        targetShiftId: '',
        reason: ''
    });

    const [requesterShifts, setRequesterShifts] = useState([]);
    const [targetEmployees, setTargetEmployees] = useState([]);
    const [targetShifts, setTargetShifts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [hasAccess, setHasAccess] = useState(true);

    useEffect(() => {
        async function fetchInitialData() {
            if (user?.positionName !== "Staff") {
                console.warn("Chỉ Staff mới có thể tạo yêu cầu đổi ca");
                setHasAccess(false);
                setLoading(false);
                return;
            }

            try {
                const data = await getShiftSwapOptions(user.userId);
                setRequesterShifts(data.requesterShifts || []);
                setTargetEmployees(data.targetEmployees || []);
            } catch (err) {
                console.error("Lỗi khi tải dữ liệu:", err);
            } finally {
                setLoading(false);
            }
        }

        if (user?.userId) {
            fetchInitialData();
        }
    }, [user?.userId, user?.positionName]);

    useEffect(() => {
        async function fetchTargetShifts() {
            if (!formData.targetEmployeeId || user?.positionName !== "Staff") {
                setTargetShifts([]);
                return;
            }

            try {
                const data = await getShiftSwapOptions(user.userId, formData.targetEmployeeId);
                setTargetShifts(data.targetShifts || []);
            } catch (err) {
                console.error("Lỗi khi tải targetShifts:", err);
            }
        }

        fetchTargetShifts();
    }, [formData.targetEmployeeId, user?.userId, user?.positionName]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: Number(value) || value
        }));
    };

    const handleSubmit = () => {
        if (!formData.requestedShiftId || !formData.targetEmployeeId || !formData.targetShiftId) {
            alert("Vui lòng chọn đầy đủ các trường bắt buộc!");
            return;
        }
        onCreate(formData);
        onClose();
    };

    if (loading) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <p>Đang tải dữ liệu...</p>
                </div>
            </div>
        );
    }

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ có nhân viên mới có thể tạo yêu cầu đổi ca</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo Yêu Cầu Đổi Ca</h2>

                <div className="form-group">
                    <label>Người yêu cầu</label>
                    <select name="requesterId" value={formData.requesterId} disabled>
                        <option value={user?.userId}>{user?.name || 'Tôi'}</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>Ca hiện tại</label>
                    <select
                        name="requestedShiftId"
                        value={formData.requestedShiftId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn ca hiện tại --</option>
                        {requesterShifts.map(s => (
                            <option key={s.id} value={s.id}>
                                {s.shiftName} ({s.workDate})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Người muốn đổi</label>
                    <select
                        name="targetEmployeeId"
                        value={formData.targetEmployeeId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn nhân viên --</option>
                        {targetEmployees.map(emp => (
                            <option key={emp.id} value={emp.id}>
                                {emp.fullName || emp.employeeCode}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Ca thay thế</label>
                    <select
                        name="targetShiftId"
                        value={formData.targetShiftId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn ca thay thế --</option>
                        {targetShifts.map(s => (
                            <option key={s.id} value={s.id}>
                                {s.shiftName} ({s.workDate})
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Lý do</label>
                    <textarea
                        name="reason"
                        value={formData.reason}
                        onChange={handleChange}
                        placeholder="Nhập lý do..."
                    />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Gửi yêu cầu</button>
                </div>
            </div>
        </div>
    );
};

export default CreateShiftSwapRequestModal;
