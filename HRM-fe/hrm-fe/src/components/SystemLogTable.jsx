import React from "react";

const formatDateTime = (dateTime) => {
    if (!dateTime) return "?";
    const date = new Date(dateTime);
    return date.toLocaleString("vi-VN");
};

const SystemLogTable = ({ data }) => {
    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Người thực hiện</th>
                    <th>Hành động</th>
                    <th>Thời gian</th>
                    <th>Mô tả</th>
                    <th>IP</th>
                </tr>
                </thead>
                <tbody>
                {data.map((log, index) => (
                    <tr key={log.id} style={{ opacity: 1 }}>
                        <td>{index + 1}</td>
                        <td>{log.username || "?"}</td>
                        <td>{log.action || "?"}</td>
                        <td>{formatDateTime(log.logTime)}</td>
                        <td>{log.description || ""}</td>
                        <td>{log.ipAddress || ""}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default SystemLogTable;
