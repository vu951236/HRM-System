import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext.jsx';
import { applyTemplate } from '../../services/workScheduleTemplateService.js';
import '../../styles/WorkScheduleTemplateTable.css';

const WorkScheduleTemplateTable = ({ data, onEdit, onDelete, onRestore }) => {
    const { user } = useAuth();
    const [modalOpen, setModalOpen] = useState(false);
    const [selectedTemplate, setSelectedTemplate] = useState(null);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [loading, setLoading] = useState(false);

    const dayMap = {
        1: "Thứ 2",
        2: "Thứ 3",
        3: "Thứ 4",
        4: "Thứ 5",
        5: "Thứ 6",
        6: "Thứ 7",
        7: "Chủ nhật"
    };

    const renderShiftPattern = (patternDetail) => {
        if (!patternDetail || patternDetail.length === 0) return <span style={{ color: "gray" }}>Không có dữ liệu</span>;
        return (
            <ul style={{ margin: 0, paddingLeft: "20px" }}>
                {patternDetail.map((item, idx) => (
                    <li key={idx}>{dayMap[item.day] || `Ngày ${item.day}`} → {item.shiftName}</li>
                ))}
            </ul>
        );
    };

    const openApplyModal = (template) => {
        setSelectedTemplate(template);
        setStartDate('');
        setEndDate('');
        setModalOpen(true);
    };

    const [overwrite, setOverwrite] = useState(false);


    const closeApplyModal = () => {
        setModalOpen(false);
        setSelectedTemplate(null);
    };

    const handleApply = async () => {
        if (!startDate || !endDate) {
            alert("Vui lòng chọn đầy đủ ngày bắt đầu và kết thúc!");
            return;
        }

        if (!window.confirm(`Áp dụng template này từ ${startDate} đến ${endDate}?${overwrite ? " (Ghi đè lịch hiện có)" : ""}`)) return;

        try {
            setLoading(true);
            await applyTemplate(selectedTemplate.id, startDate, endDate, overwrite);
            alert("Áp dụng template thành công!");
            closeApplyModal();
        } catch (error) {
            console.error(error);
            alert("Có lỗi khi áp dụng template.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên template</th>
                    <th>Phòng ban</th>
                    <th>Mẫu ca</th>
                    <th>Người tạo</th>
                    <th>Ngày tạo</th>
                    <th>Hành động</th>
                    <th>Áp dụng template</th>
                </tr>
                </thead>
                <tbody>
                {data.map((template, index) => (
                    <tr key={template.id} style={{ opacity: template.isDelete ? 0.5 : 1 }}>
                        <td>{index + 1}</td>
                        <td>{template.templateName}</td>
                        <td>{template.departmentName || 'N/A'}</td>
                        <td>{renderShiftPattern(template.shiftPatternDetail)}</td>
                        <td>{template.createdByName || 'N/A'}</td>
                        <td>{template.createdAt ? new Date(template.createdAt).toLocaleString() : 'N/A'}</td>
                        <td className="action-icons">
                            {!template.isDelete ? (
                                <>
                                    <i
                                        className="fa fa-pencil"
                                        style={{ cursor: 'pointer', marginRight: '10px' }}
                                        title="Chỉnh sửa"
                                        onClick={() => onEdit(template)}
                                    />
                                    {user?.role === 'admin' && (
                                        <i
                                            className="fa fa-trash"
                                            style={{ cursor: 'pointer', color: 'red', marginRight: '10px' }}
                                            title="Xóa mềm"
                                            onClick={() => {
                                                if (window.confirm('Bạn có chắc muốn xóa template này?')) {
                                                    onDelete(template.id);
                                                }
                                            }}
                                        />
                                    )}
                                </>
                            ) : (
                                <i
                                    className="fa fa-undo"
                                    style={{ cursor: 'pointer', color: 'green' }}
                                    title="Khôi phục"
                                    onClick={() => {
                                        if (window.confirm('Bạn có chắc muốn khôi phục template này?')) {
                                            onRestore(template.id);
                                        }
                                    }}
                                />
                            )}
                        </td>
                        <td>
                            {!template.isDelete && (
                                <button className="apply-btn" onClick={() => openApplyModal(template)}>
                                    Áp dụng
                                </button>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            {modalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h3>Áp dụng template: {selectedTemplate.templateName}</h3>
                        <label>
                            Ngày bắt đầu:
                            <input type="date" value={startDate} onChange={e => setStartDate(e.target.value)} />
                        </label>
                        <label>
                            Ngày kết thúc:
                            <input type="date" value={endDate} onChange={e => setEndDate(e.target.value)} />
                        </label>
                        <label style={{ display: 'block', marginTop: '10px' }}>
                            <input
                                type="checkbox"
                                checked={overwrite}
                                onChange={e => setOverwrite(e.target.checked)}
                            />
                            Ghi đè lịch hiện có
                        </label>
                        <div style={{ marginTop: '10px' }}>
                            <button onClick={handleApply} disabled={loading}>
                                {loading ? 'Đang áp dụng...' : 'Áp dụng'}
                            </button>
                            <button onClick={closeApplyModal} style={{ marginLeft: '10px' }}>Hủy</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default WorkScheduleTemplateTable;
