import React, { useEffect, useState, useRef } from 'react';
import '../styles/Dashboard.css';
import Sidebar from '../components/Sidebar';
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import { fetchLeaveOvertimeChart, fetchPayrollChart, fetchAttendanceChart } from '../services/chartService';
import { getDepartments } from '../services/DataApi';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { Bar, Line } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, PointElement, LineElement, Title, Tooltip, Legend);

const ChartWrapper = ({ type, data, options }) => {
    const chartRef = useRef(null);

    useEffect(() => {
        const chartInstance = chartRef.current;
        return () => {
            if (chartInstance?.chartInstance) chartInstance.chartInstance.destroy();
        };
    }, [data]);

    if (type === 'bar') return <Bar ref={chartRef} data={data} options={options} />;
    if (type === 'line') return <Line ref={chartRef} data={data} options={options} />;
    return null;
};

const AttendanceDashboard = () => {
    const { user } = useAuth();
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Thống kê mức chuyên cần');
    const [departments, setDepartments] = useState([]);

    const [leaveOvertimeData, setLeaveOvertimeData] = useState([]);
    const [payrollData, setPayrollData] = useState([]);
    const [attendanceData, setAttendanceData] = useState([]);

    const [leaveFilter, setLeaveFilter] = useState({ startDate: '2025-01-01', endDate: '2025-12-31', departmentId: user?.departmentId || null });
    const [payrollFilter, setPayrollFilter] = useState({ year: 2025, quarter: null, departmentId: user?.departmentId || null });
    const [attendanceFilter, setAttendanceFilter] = useState({ startDate: '2025-01-01', endDate: '2025-12-31', departmentId: user?.departmentId || null });

    useEffect(() => {
        setSidebarGroups(getSidebarGroups(user || null));
        loadDepartments();
    }, [user]);

    const loadDepartments = async () => {
        try {
            const data = await getDepartments();
            setDepartments(data);
        } catch (err) {
            console.error('Lỗi tải danh sách phòng ban:', err);
        }
    };

    useEffect(() => { loadLeaveChart(); }, [leaveFilter]);
    useEffect(() => { loadPayrollChart(); }, [payrollFilter]);
    useEffect(() => { loadAttendanceChart(); }, [attendanceFilter]);

    const loadLeaveChart = async () => {
        try {
            const res = await fetchLeaveOvertimeChart({
                startDate: leaveFilter.startDate,
                endDate: leaveFilter.endDate,
                departmentId: leaveFilter.departmentId ? Number(leaveFilter.departmentId) : null
            });
            setLeaveOvertimeData(res);
        } catch (error) { console.error('Lỗi tải Leave & Overtime:', error); }
    };

    const loadPayrollChart = async () => {
        try {
            const res = await fetchPayrollChart({
                year: Number(payrollFilter.year),
                quarter: payrollFilter.quarter === '' || payrollFilter.quarter === null ? null : Number(payrollFilter.quarter),
                departmentId: payrollFilter.departmentId ? Number(payrollFilter.departmentId) : null
            });
            setPayrollData(res);
        } catch (error) { console.error('Lỗi tải Payroll:', error); }
    };

    const loadAttendanceChart = async () => {
        try {
            const res = await fetchAttendanceChart({
                startDate: attendanceFilter.startDate,
                endDate: attendanceFilter.endDate,
                departmentId: attendanceFilter.departmentId ? Number(attendanceFilter.departmentId) : null
            });
            setAttendanceData(res);
        } catch (error) { console.error('Lỗi tải Attendance:', error); }
    };

    const handleFilterChange = (setter, e) => {
        const { name, value } = e.target;
        let newValue = value;
        if (name.includes('departmentId') || name === 'year') newValue = value ? Number(value) : null;
        if (name === 'quarter') newValue = value === '' ? null : Number(value);
        setter(prev => ({ ...prev, [name]: newValue }));
    };

    const formatLeaveOvertimeData = (data) => ({
        labels: data.map(d => d.name),
        datasets: [
            { label: 'Leave Days', data: data.map(d => d.leaveDays), backgroundColor: 'rgba(255, 99, 132, 0.5)' },
            { label: 'Overtime Hours', data: data.map(d => d.overtimeHours), backgroundColor: 'rgba(54, 162, 235, 0.5)' }
        ]
    });

    const formatPayrollData = (data) => ({
        labels: data.map(d => d.period),
        datasets: [{
            label: 'Final Salary Total',
            data: data.map(d => d.finalSalaryTotal),
            backgroundColor: 'rgba(75, 192, 192, 0.5)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1,
            pointRadius: data.length <= 1 ? undefined : 6,
            pointHoverRadius: data.length <= 1 ? undefined : 8
        }]
    });

    const formatAttendanceData = (data) => ({
        labels: data.map(d => d.name),
        datasets: [
            { label: 'Late Count', data: data.map(d => d.lateCount), backgroundColor: 'rgba(255, 206, 86, 0.5)' },
            { label: 'Early Leave Count', data: data.map(d => d.earlyLeaveCount), backgroundColor: 'rgba(153, 102, 255, 0.5)' },
            { label: 'Absent Count', data: data.map(d => d.absentCount), backgroundColor: 'rgba(255, 99, 132, 0.5)' }
        ]
    });

    return (
        <div className="dashboard-container">
            <Sidebar groups={sidebarGroups} activeItem={activeSidebarItem} onItemClick={item => setActiveSidebarItem(item)} />
            <div className="main-content">
                <h1>{activeSidebarItem}</h1>
                <div className="cards-wrapper">
                    <div className="card">
                        <h2>Leave & Overtime</h2>
                        <ChartWrapper type="bar" data={formatLeaveOvertimeData(leaveOvertimeData)} options={{ responsive: true }} />
                        <div className="filter-grid">
                            <label>
                                Start Date:
                                <input type="date" name="startDate" value={leaveFilter.startDate} onChange={e => handleFilterChange(setLeaveFilter, e)} />
                            </label>
                            <label>
                                End Date:
                                <input type="date" name="endDate" value={leaveFilter.endDate} onChange={e => handleFilterChange(setLeaveFilter, e)} />
                            </label>
                            <label>
                                Department:
                                <select name="departmentId" value={leaveFilter.departmentId || ''} onChange={e => handleFilterChange(setLeaveFilter, e)}>
                                    <option value="">All</option>
                                    {departments.map(dep => <option key={dep.id} value={dep.id}>{dep.name}</option>)}
                                </select>
                            </label>
                        </div>
                    </div>

                    <div className="card">
                        <h2>Payroll</h2>
                        <ChartWrapper type={payrollData.length <= 1 ? 'bar' : 'line'} data={formatPayrollData(payrollData)} options={{ responsive: true }} />
                        <div className="filter-grid">
                            <label>
                                Year:
                                <input type="number" name="year" value={payrollFilter.year} onChange={e => handleFilterChange(setPayrollFilter, e)} />
                            </label>
                            <label>
                                Quarter:
                                <select name="quarter" value={payrollFilter.quarter || ''} onChange={e => handleFilterChange(setPayrollFilter, e)}>
                                    <option value="">All</option>
                                    <option value="1">Q1</option>
                                    <option value="2">Q2</option>
                                    <option value="3">Q3</option>
                                    <option value="4">Q4</option>
                                </select>
                            </label>
                            <label>
                                Department:
                                <select name="departmentId" value={payrollFilter.departmentId || ''} onChange={e => handleFilterChange(setPayrollFilter, e)}>
                                    <option value="">All</option>
                                    {departments.map(dep => <option key={dep.id} value={dep.id}>{dep.name}</option>)}
                                </select>
                            </label>
                        </div>
                    </div>

                    <div className="card full-width">
                        <h2>Attendance</h2>
                        <ChartWrapper type="bar" data={formatAttendanceData(attendanceData)} options={{ responsive: true }} />
                        <div className="filter-grid">
                            <label>
                                Start Date:
                                <input type="date" name="startDate" value={attendanceFilter.startDate} onChange={e => handleFilterChange(setAttendanceFilter, e)} />
                            </label>
                            <label>
                                End Date:
                                <input type="date" name="endDate" value={attendanceFilter.endDate} onChange={e => handleFilterChange(setAttendanceFilter, e)} />
                            </label>
                            <label>
                                Department:
                                <select name="departmentId" value={attendanceFilter.departmentId || ''} onChange={e => handleFilterChange(setAttendanceFilter, e)}>
                                    <option value="">All</option>
                                    {departments.map(dep => <option key={dep.id} value={dep.id}>{dep.name}</option>)}
                                </select>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AttendanceDashboard;
