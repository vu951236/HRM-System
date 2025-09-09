import React from "react";
import { useAuth } from "../../context/AuthContext.jsx";

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
                const [_, days, amount] = match;
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
            const match = formula.match(/0\.(\d+)/);
            if (match) {
                const percent = Number(match[1]);
                return `Khấu trừ BHXH = ${percent}% lương cơ bản`;
            }
            return "Khấu trừ BHXH (công thức không xác định)";
        }


        default:
            return "";
    }
};

const SalaryRuleTable = ({ data, onEdit }) => {
    const { user } = useAuth();

    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên quy tắc</th>
                    <th>Mô tả</th>
                    <th>Công thức</th>
                    <th>Trạng thái</th>
                    {user?.role === "admin" && <th>Hành động</th>}
                </tr>
                </thead>
                <tbody>
                {data.map((rule, index) => {
                    const description =
                        rule.description || getFormulaDescription(rule.ruleType, rule.formula);

                    return (
                        <tr key={rule.id} style={{ opacity: rule.active ? 1 : 0.5 }}>
                            <td>{index + 1}</td>
                            <td>{rule.ruleName}</td>
                            <td>{description}</td>
                            <td style={{ fontStyle: "italic", color: "#666" }}>
                                {getFormulaDescription(rule.ruleType, rule.formula)}
                            </td>
                            <td>{rule.active ? "Hoạt động" : "Đã xóa"}</td>
                            {user?.role === "admin" && (
                                <td className="action-icons">
                                    {rule.active && (
                                        <i
                                            className="fa fa-pencil"
                                            style={{ cursor: "pointer", marginRight: "10px" }}
                                            title="Chỉnh sửa"
                                            onClick={() => onEdit(rule)}
                                        />
                                    )}
                                </td>
                            )}
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </div>
    );
};

export default SalaryRuleTable;
