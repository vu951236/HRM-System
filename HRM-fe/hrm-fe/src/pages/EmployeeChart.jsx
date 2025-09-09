import React, { useEffect, useState } from 'react';
import '../styles/Chart.css';
import Sidebar from '../components/Dashboard/Sidebar.jsx';
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/Dashboard/sidebarData.js';
import { getDepartments, getContractTypes } from '../services/DataApi';
import {fetchEmployeeByDepartment, fetchContractByType, fetchContractExpiring} from '../services/chartService';
import {exportEmployeeCountReport, exportContractCountReport, exportExpiringContractsReport} from '../services/reportService';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

const EmployeeChart = () => {
    const { user } = useAuth();
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Thống kê nhân viên');
    const [departments, setDepartments] = useState([]);
    const [contractTypes, setContractTypes] = useState([]);

    const [employeeFilter, setEmployeeFilter] = useState({ departmentId: user?.departmentId || null });
    const [contractTypeFilter, setContractTypeFilter] = useState({ contractTypeId: null });
    const [contractExpFilter, setContractExpFilter] = useState({ daysUntilExpiry: 30 });

    const [employeeByDeptData, setEmployeeByDeptData] = useState([]);
    const [contractByTypeData, setContractByTypeData] = useState([]);
    const [contractExpiringData, setContractExpiringData] = useState([]);

    useEffect(() => {
        setSidebarGroups(getSidebarGroups(user || null));
        loadDepartments();
        loadContractTypes();
    }, [user]);

    const loadDepartments = async () => {
        try {
            const data = await getDepartments();
            setDepartments(data);
        } catch (err) {
            console.error('Lỗi tải danh sách phòng ban:', err);
        }
    };

    const loadContractTypes = async () => {
        try {
            const data = await getContractTypes();
            setContractTypes(data);
        } catch (err) {
            console.error('Lỗi tải danh sách loại hợp đồng:', err);
        }
    };

    useEffect(() => { loadEmployeeByDept(); }, [employeeFilter]);
    useEffect(() => { loadContractByType(); }, [contractTypeFilter]);
    useEffect(() => { loadContractExpiring(); }, [contractExpFilter]);

    const loadEmployeeByDept = async () => {
        try {
            const res = await fetchEmployeeByDepartment(employeeFilter);
            setEmployeeByDeptData(res);
        } catch (err) {
            console.error(err);
        }
    };

    const loadContractByType = async () => {
        try {
            const res = await fetchContractByType(contractTypeFilter);
            setContractByTypeData(res);
        } catch (err) {
            console.error(err);
        }
    };

    const loadContractExpiring = async () => {
        try {
            const res = await fetchContractExpiring(contractExpFilter);
            setContractExpiringData(res);
        } catch (err) {
            console.error(err);
        }
    };

    const fixedColors = [
        'rgba(255, 99, 132, 0.6)',
        'rgba(54, 162, 235, 0.6)',
        'rgba(255, 206, 86, 0.6)',
        'rgba(75, 192, 192, 0.6)',
        'rgba(153, 102, 255, 0.6)',
        'rgba(255, 159, 64, 0.6)',
        'rgba(199, 199, 199, 0.6)'
    ];

    const formatBarChart = (data, labelName) => {
        const backgroundColors = data.map((_, i) => fixedColors[i % fixedColors.length]);
        const borderColors = backgroundColors.map(c => c.replace('0.6', '1'));

        return {
            labels: data.map(d => d.label),
            datasets: [{
                label: labelName,
                data: data.map(d => d.count),
                backgroundColor: backgroundColors,
                borderColor: borderColors,
                borderWidth: 1
            }]
        };
    };

    return (
        <div className="dashboard-container">
            <Sidebar
                groups={sidebarGroups}
                activeItem={activeSidebarItem}
                onItemClick={item => setActiveSidebarItem(item)}
            />
            <div className="main-content">
                <h1>{activeSidebarItem}</h1>
                <div className="cards-wrapper">

                    <div className="card">
                        <h2>Contract by Type</h2>
                        <button
                            onClick={() => exportContractCountReport(contractTypeFilter)}
                            className="download-btn"
                        >
                            Tải báo cáo
                        </button>
                        <Bar
                            data={formatBarChart(contractByTypeData, 'Contracts')}
                            options={{ responsive: true, plugins: { legend: { position: 'top' } } }}
                        />
                        <div className="filter-grid">
                            <label>
                                Contract Type:
                                <select
                                    value={contractTypeFilter.contractTypeId || ''}
                                    onChange={e =>
                                        setContractTypeFilter({ contractTypeId: e.target.value ? Number(e.target.value) : null })
                                    }
                                >
                                    <option value="">All</option>
                                    {contractTypes.map(ct =>
                                        <option key={ct.id} value={ct.id}>{ct.name}</option>
                                    )}
                                </select>
                            </label>
                        </div>
                    </div>

                    <div className="card">
                        <h2>Contract Expiring</h2>
                        <button
                            onClick={() => exportExpiringContractsReport(contractExpFilter)}
                            className="download-btn"
                        >
                            Tải báo cáo
                        </button>
                        <Bar
                            data={formatBarChart(contractExpiringData, 'Contracts Expiring')}
                            options={{ responsive: true, plugins: { legend: { position: 'top' } } }}
                        />
                        <div className="filter-grid">
                            <label>
                                Days Until Expiry:
                                <input
                                    type="number"
                                    value={contractExpFilter.daysUntilExpiry}
                                    onChange={e =>
                                        setContractExpFilter({ daysUntilExpiry: e.target.value ? Number(e.target.value) : 30 })
                                    }
                                />
                            </label>
                        </div>
                    </div>

                    <div className="card full-width">
                        <h2>Employee by Department</h2>
                        <button
                            onClick={() => exportEmployeeCountReport(employeeFilter)}
                            className="download-btn"
                        >
                            Tải báo cáo
                        </button>
                        <Bar
                            data={formatBarChart(employeeByDeptData, 'Employee Count')}
                            options={{
                                responsive: true,
                                plugins: { legend: { position: 'top' } },
                                scales: {
                                    x: {
                                        ticks: {
                                            maxRotation: 0,
                                            minRotation: 0
                                        }
                                    }
                                }
                            }}
                        />
                        <div className="filter-grid">
                            <label>
                                Department:
                                <select
                                    value={employeeFilter.departmentId || ''}
                                    onChange={e =>
                                        setEmployeeFilter({ departmentId: e.target.value ? Number(e.target.value) : null })
                                    }
                                >
                                    <option value="">All</option>
                                    {departments.map(dep =>
                                        <option key={dep.id} value={dep.id}>{dep.name}</option>
                                    )}
                                </select>
                            </label>
                        </div>
                    </div>


                </div>
            </div>
        </div>
    );
};

export default EmployeeChart;
