import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext.jsx';

const CreateOvertimeModal = ({ onClose, onCreate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        employeeId: user?.userId || '',
        date: '',
        startTime: '',
        endTime: '',
        reason: ''
    });

    const [hasAccess, setHasAccess] = useState(true);

    // Chỉ cho Staff được tạo OT
    React.useEffect(() => {
        if (user?.positionName !== "Staff") {
            console.warn("Chỉ Staff mới có thể tạo yêu cầu OT");
            setHasAccess(false);
        }
    }, [user?.positionName]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = () => {
        if (!formData.date || !formData.startTime || !formData.endTime) {
            alert("Vui lòng nhập đầy đủ ngày, giờ bắt đầu và giờ kết thúc!");
            return;
        }
        onCreate(formData);
        onClose();
    };

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ có nhân viên mới có thể tạo yêu cầu làm thêm giờ</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo Yêu Cầu Làm Thêm Giờ</h2>

                <div className="form-group">
                    <label>Nhân viên</label>
                    <input
                        type="text"
                        value={user?.name || 'Tôi'}
                        disabled
                    />
                </div>

                <div className="form-group">
                    <label>Ngày</label>
                    <input
                        type="date"
                        name="date"
                        value={formData.date}
                        onChange={handleChange}
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

export default CreateOvertimeModal;
