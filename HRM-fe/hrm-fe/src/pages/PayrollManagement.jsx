import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllPayrolls,
    calculatePayroll,
    approvePayroll
} from '../services/payrollService';
import PayrollTable from '../components/PayrollTable';
import CalculatePayrollModal from '../components/CalculatePayrollModal';

const PayrollManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý bảng lương');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [payrolls, setPayrolls] = useState([]);
    const [showCalculateModal, setShowCalculateModal] = useState(false);

    const { user } = useAuth();

    const loadPayrolls = async () => {
        try {
            const data = await fetchAllPayrolls();
            setPayrolls(data);
        } catch (error) {
            console.error('Không thể tải danh sách bảng lương:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý bảng lương') {
            loadPayrolls();
        }
    }, [activeSidebarItem]);

    const filteredPayrolls = payrolls.filter(p =>
        p.employeeName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredPayrolls.length / itemsPerPage);
    const currentPayrolls = filteredPayrolls.slice(
        (activePage - 1) * itemsPerPage,
        activePage * itemsPerPage
    );

    const handleApprove = async (id) => {
        try {
            await approvePayroll(id);
            await loadPayrolls();
            alert('Duyệt bảng lương thành công!');
        } catch (error) {
            console.error(error);
            alert('Duyệt bảng lương thất bại!');
        }
    };

    const handleCalculatePayroll = async (userId, month, year) => {
        try {
            await calculatePayroll(userId, month, year);
            await loadPayrolls();
            setShowCalculateModal(false);
            alert('Tính lương thành công!');
        } catch (error) {
            console.error(error);
            alert('Tính lương thất bại!');
        }
    };

    return (
        <div style={{ display: 'flex', height: '100vh' }}>
            <Sidebar
                groups={sidebarGroups}
                activeItem={activeSidebarItem}
                onItemClick={item => {
                    setActiveSidebarItem(item);
                    setActivePage(1);
                }}
            />

            <div className="main">
                <div className="top-bar">
                    <SearchBox
                        searchTerm={searchTerm}
                        setSearchTerm={(value) => {
                            setSearchTerm(value);
                            setActivePage(1);
                        }}
                    />
                </div>

                <div className="page-header">
                    <h1>{activeSidebarItem}</h1>
                    <button onClick={() => setShowCalculateModal(true)}>
                        Tính lương
                    </button>
                </div>

                {activeSidebarItem === 'Quản lý bảng lương' && (
                    <>
                        <PayrollTable
                            data={currentPayrolls}
                            onApprove={handleApprove} // approve trực tiếp trên table
                        />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {showCalculateModal && (
                    <CalculatePayrollModal
                        onClose={() => setShowCalculateModal(false)}
                        onCalculate={handleCalculatePayroll}
                    />
                )}
            </div>
        </div>
    );
};

export default PayrollManagement;
