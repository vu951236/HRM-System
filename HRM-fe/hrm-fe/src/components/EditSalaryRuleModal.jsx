import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

const DEFAULT_FORMULAS = {
    BASE: "value * 50000",
    BONUS: "value >= 22 ? 1000000 : 0",
    OVERTIME: "value * 75000",
    LATE: "value * 2000",
    LEAVE: "value * 300000",
    DEDUCTION: "value * 0.08"
};

const formatCurrency = (num) => {
    if (!num) return "?";
    return Number(num).toLocaleString("vi-VN");
};

const getFormulaDescription = (type, formula) => {
    if (!formula) return "";
    const cleanFormula = formula.replace(/\s+/g, "");
    switch (type) {
        case "BASE": {
            const number = formula.match(/\d+/g)?.[0];
            return `Lương cơ bản = số giờ làm việc × ${formatCurrency(number)} VNĐ`;
        }
        case "BONUS": {
            const match = cleanFormula.match(/value>=?(\d+)\?(\d+):0/);
            if (match) {
                const days = match[1];
                const amount = match[2];
                return `Thưởng chuyên cần = nếu đi làm ≥ ${days} ngày thì +${formatCurrency(amount)} VNĐ`;
            }
            return "Thưởng chuyên cần (công thức không xác định)";
        }
        case "OVERTIME": {
            const number = formula.match(/\d+/g)?.[0];
            return `Phụ cấp OT = số giờ làm thêm × ${formatCurrency(number)} VNĐ`;
        }
        case "LATE": {
            const number = formula.match(/\d+/g)?.[0];
            return `Đi trễ = số phút đi trễ × ${formatCurrency(number)} VNĐ (trừ)`;
        }
        case "LEAVE": {
            const number = formula.match(/\d+/g)?.[0];
            return `Nghỉ không phép = số ngày nghỉ × ${formatCurrency(number)} VNĐ (trừ)`;
        }
        case "DEDUCTION": {
            const match = formula.match(/value\s*\*\s*(\d*\.?\d+)/);
            if (match) {
                const percent = parseFloat(match[1]) * 100;
                return `Khấu trừ BHXH = ${percent}% lương cơ bản`;
            }
            return "Khấu trừ BHXH (công thức không xác định)";
        }
        default:
            return "";
    }
};

const EditSalaryRuleModal = ({ rule, onClose, onUpdate }) => {
    const { user } = useAuth();

    const [formData, setFormData] = useState({
        ruleName: '',
        description: '',
        formula: '',
        ruleType: '',
        active: true
    });

    const [bonusValue, setBonusValue] = useState({ days: "", amount: "" });
    const [numberValue, setNumberValue] = useState("");
    const [hasAccess, setHasAccess] = useState(true);

    useEffect(() => {
        if (user?.role?.toLowerCase() !== "admin") {
            console.warn("⚠️ Chỉ Admin mới có thể chỉnh sửa quy tắc lương");
            setHasAccess(false);
        }
    }, [user?.role]);

    useEffect(() => {
        if (rule) {
            setFormData({
                ruleName: rule.ruleName || '',
                description: rule.description || '',
                formula: rule.formula || '',
                ruleType: rule.ruleType || '',
                active: rule.active ?? true
            });

            if (rule.ruleType === "BONUS") {
                const matchDays = rule.formula.match(/value\s*>=\s*(\d+)\s*\?/);
                const matchAmount = rule.formula.match(/\?\s*(\d+)\s*:/);
                setBonusValue({
                    days: matchDays ? matchDays[1] : "",
                    amount: matchAmount ? matchAmount[1] : ""
                });
            } else {
                setNumberValue(rule.formula.match(/[\d.]+/)?.[0] || "");
            }
        }
    }, [rule]);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (name === "ruleType" || name === "active") return;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleNumberChange = (e) => {
        const newVal = e.target.value;
        setNumberValue(newVal);

        let newFormula = "";
        switch (formData.ruleType) {
            case "BASE":
            case "OVERTIME":
            case "LATE":
            case "LEAVE":
                newFormula = `value * ${newVal || 0}`;
                break;
            case "DEDUCTION":
                newFormula = newVal ? `value * ${newVal}` : "";
                break;
            default:
                newFormula = formData.formula;
        }
        setFormData(prev => ({ ...prev, formula: newFormula }));
    };

    const handleBonusChange = (field, value) => {
        const updatedBonus = { ...bonusValue, [field]: value };
        setBonusValue(updatedBonus);
        setFormData(prev => ({
            ...prev,
            formula: `value >= ${updatedBonus.days || 0} ? ${updatedBonus.amount || 0} : 0`
        }));
    };

    const handleSubmit = () => {
        onUpdate(rule.id, {
            ruleName: formData.ruleName,
            description: formData.description || null,
            formula: formData.formula || null,
            ruleType: formData.ruleType || null,
            active: formData.active
        });
        onClose();
    };

    if (!hasAccess) {
        return (
            <div className="modal-overlay">
                <div className="modal">
                    <h2>⚠️ Chỉ Admin mới có thể chỉnh sửa quy tắc lương</h2>
                    <button onClick={onClose}>Đóng</button>
                </div>
            </div>
        );
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>✏️ Chỉnh sửa quy tắc lương</h2>

                <div className="form-group">
                    <label>Tên quy tắc</label>
                    <input
                        type="text"
                        name="ruleName"
                        value={formData.ruleName}
                        onChange={handleChange}
                        placeholder="Ví dụ: Thưởng hiệu suất"
                    />
                </div>

                <div className="form-group">
                    <label>Mô tả</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        placeholder="Mô tả quy tắc..."
                    />
                </div>

                <div className="form-group">
                    <label>Công thức</label>
                    {formData.ruleType === "BONUS" ? (
                        <>
                            <div>
                                <label>Số ngày</label>
                                <input
                                    type="number"
                                    value={bonusValue.days}
                                    onChange={(e) => handleBonusChange("days", e.target.value)}
                                />
                            </div>
                            <div>
                                <label>Số tiền</label>
                                <input
                                    type="number"
                                    value={bonusValue.amount}
                                    onChange={(e) => handleBonusChange("amount", e.target.value)}
                                />
                            </div>
                        </>
                    ) : (
                        <div>
                            <label>Hệ số / Số tiền</label>
                            <input
                                type="number"
                                step="0.01"
                                value={numberValue}
                                onChange={handleNumberChange}
                            />
                        </div>
                    )}
                    {formData.ruleType && (
                        <p style={{ fontStyle: "italic", color: "#666", marginTop: "5px" }}>
                            {getFormulaDescription(formData.ruleType, formData.formula)}
                        </p>
                    )}
                </div>

                <div className="form-group">
                    <label>Loại quy tắc</label>
                    <select
                        name="ruleType"
                        value={formData.ruleType}
                        onChange={handleChange}
                        disabled
                    >
                        <option value="BASE">Cơ bản</option>
                        <option value="BONUS">Thưởng</option>
                        <option value="DEDUCTION">Khấu trừ</option>
                        <option value="OVERTIME">Làm thêm giờ</option>
                        <option value="LATE">Đi trễ</option>
                        <option value="LEAVE">Nghỉ phép</option>
                    </select>
                </div>

                <div className="form-group">
                    <label>
                        <input
                            type="checkbox"
                            name="active"
                            checked={formData.active}
                            onChange={handleChange}
                            disabled
                        />
                        Đang hoạt động
                    </label>
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Hủy</button>
                    <button onClick={handleSubmit}>Cập nhật quy tắc</button>
                </div>
            </div>
        </div>
    );
};

export default EditSalaryRuleModal;
