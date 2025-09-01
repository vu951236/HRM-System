import React from 'react';

const AttendanceTable = ({ data }) => {
    return (
        <div className="table-container">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Tên nhân viên</th>
                    <th>Tên ca</th>
                    <th>Check-in</th>
                    <th>Check-out</th>
                    <th>Trạng thái</th>
                    <th>Phương thức check-in</th>
                    <th>Phương thức check-out</th>
                    <th>Thiết bị</th>
                </tr>
                </thead>
                <tbody>
                {data.length > 0 ? (
                    data.map((log, index) => (
                        <tr key={log.id}>
                            <td>{index + 1}</td>
                            <td>{log.userFullName} ({log.employeeCode})</td>
                            <td>{log.shiftName || 'N/A'}</td>
                            <td>{log.checkInTime ? new Date(log.checkInTime).toLocaleString() : 'N/A'}</td>
                            <td>{log.checkOutTime ? new Date(log.checkOutTime).toLocaleString() : 'N/A'}</td>
                            <td>{log.status || 'N/A'}</td>
                            <td>{log.checkInMethod || 'N/A'}</td>
                            <td>{log.checkOutMethod || 'N/A'}</td>
                            <td>{log.deviceName || 'N/A'}</td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="10" style={{ textAlign: 'center' }}>
                            Không có dữ liệu
                        </td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
};

export default AttendanceTable;
