import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const CalculatePayrollModal = ({ onClose, onCalculate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        userId: '',
        month: new Date().getMonth() + 1,
        year: new Date().getFullYear()
    });

    const [hasAccess, setHasAccess] = useState(true);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (user?.role?.toLowerCase() !== 'admin') {
            console.warn("Chỉ Admin mới có thể tính lương");
            setHasAccess(false);
        }
    }, [user?.role]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async () => {
        setLoading(true);
        setError('');
        try {
            await onCalculate(formData.employeeId, formData.month, formData.year);
            onClose();
        } catch (err) {
            console.error('Lỗi khi tính bảng lương:', err);
            setError('Không thể tính bảng lương, thử lại sau.');
        } finally {
            setLoading(false);
        }
    };

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ Admin mới có thể tính lương</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>➕ Tính bảng lương</h2>

                {error && <p style={{ color: 'red' }}>{error}</p>}

                <div className="form-group">
                    <label>ID nhân viên</label>
                    <input
                        type="number"
                        name="employeeId"
                        value={formData.employeeId}
                        onChange={handleChange}
                        placeholder="Nhập ID nhân viên"
                    />
                </div>

                <div className="form-group">
                    <label>Tháng</label>
                    <input
                        type="number"
                        name="month"
                        value={formData.month}
                        onChange={handleChange}
                        min="1"
                        max="12"
                    />
                </div>

                <div className="form-group">
                    <label>Năm</label>
                    <input
                        type="number"
                        name="year"
                        value={formData.year}
                        onChange={handleChange}
                        min="2000"
                        max="2100"
                    />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose} disabled={loading}>Hủy</button>
                    <button onClick={handleSubmit} disabled={loading}>
                        {loading ? 'Đang tính...' : 'Tính bảng lương'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default CalculatePayrollModal;
