import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllSalaryRules,
    updateSalaryRule,
} from '../services/salaryRuleService';
import SalaryRuleTable from '../components/SalaryRuleTable';
import EditSalaryRuleModal from '../components/EditSalaryRuleModal';

const SalaryRuleManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý chính sách lương');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [salaryRules, setSalaryRules] = useState([]);
    const [editingRule, setEditingRule] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);

    const { user } = useAuth();

    const loadSalaryRules = async () => {
        try {
            const data = await fetchAllSalaryRules();
            setSalaryRules(data);
        } catch (error) {
            console.error('Không thể tải danh sách quy tắc lương:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý chính sách lương') {
            loadSalaryRules();
        }
    }, [activeSidebarItem]);

    const filteredRules = salaryRules.filter(r =>
        r.ruleName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredRules.length / itemsPerPage);
    const currentRules = filteredRules.slice(
        (activePage - 1) * itemsPerPage,
        activePage * itemsPerPage
    );

    const handleUpdate = async (id, data) => {
        try {
            await updateSalaryRule(id, data);
            await loadSalaryRules();
            setShowEditModal(false);
            setEditingRule(null);
            alert('Cập nhật quy tắc lương thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật quy tắc lương thất bại!');
        }
    };

    const handleOpenEditModal = (rule) => {
        setEditingRule(rule);
        setShowEditModal(true);
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
                </div>

                {activeSidebarItem === 'Quản lý chính sách lương' && (
                    <>
                        <SalaryRuleTable
                            data={currentRules}
                            onEdit={handleOpenEditModal}
                        />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {showEditModal && editingRule && (
                    <EditSalaryRuleModal
                        rule={editingRule}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default SalaryRuleManagement;
