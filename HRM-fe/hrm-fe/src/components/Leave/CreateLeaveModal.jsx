import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext.jsx';
import { getMyLeavePolicies } from '../../services/DataApi.js';

const CreateLeaveModal = ({ onClose, onCreate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        policyId: '',
        startDate: '',
        endDate: '',
        reason: ''
    });

    const [policies, setPolicies] = useState([]);
    const [hasAccess, setHasAccess] = useState(true);

    useEffect(() => {
        if (!(user?.role?.toLowerCase() === "staff" || user?.role?.toLowerCase() === "hr")) {
            console.warn("Chỉ Staff và HR mới có thể tạo yêu cầu nghỉ phép");
            setHasAccess(false);
        } else {
            (async () => {
                try {
                    const data = await getMyLeavePolicies();
                    setPolicies(data);
                } catch (err) {
                    console.error("Lỗi khi lấy chính sách nghỉ:", err);
                }
            })();
        }
    }, [user?.role]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = () => {
        if (!formData.policyId || !formData.startDate || !formData.endDate) {
            alert("Vui lòng chọn chính sách, ngày bắt đầu và ngày kết thúc!");
            return;
        }
        if (formData.endDate < formData.startDate) {
            alert("Ngày kết thúc không được trước ngày bắt đầu!");
            return;
        }
        onCreate(formData);
        onClose();
    };

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ có nhân viên mới có thể tạo yêu cầu nghỉ phép</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tạo Yêu Cầu Nghỉ Phép</h2>

                <div className="form-group">
                    <label>Nhân viên</label>
                    <input
                        type="text"
                        value={user?.name || 'Tôi'}
                        disabled
                    />
                </div>

                <div className="form-group">
                    <label>Chính sách nghỉ phép</label>
                    <select
                        name="policyId"
                        value={formData.policyId}
                        onChange={handleChange}
                    >
                        <option value="">-- Chọn chính sách --</option>
                        {policies.map(policy => (
                            <option key={policy.id} value={policy.id}>
                                {policy.policyName} ({policy.maxDays} ngày)
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Từ ngày</label>
                    <input
                        type="date"
                        name="startDate"
                        value={formData.startDate}
                        onChange={handleChange}
                    />
                </div>

                <div className="form-group">
                    <label>Đến ngày</label>
                    <input
                        type="date"
                        name="endDate"
                        value={formData.endDate}
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

export default CreateLeaveModal;
