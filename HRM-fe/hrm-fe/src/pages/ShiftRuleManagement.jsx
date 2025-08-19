import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllShiftRules,
    createShiftRule,
    updateShiftRule,
    deleteShiftRule,
    restoreShiftRule
} from '../services/shiftRuleService';

import ShiftRuleTable from '../components/ShiftRuleTable';
import CreateShiftRuleModal from '../components/CreateShiftRuleModal';
import EditShiftRuleModal from '../components/EditShiftRuleModal';

const ShiftRuleManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý quy tắc ca');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [shiftRules, setShiftRules] = useState([]);
    const [editingRule, setEditingRule] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadShiftRules = async () => {
        try {
            const data = await fetchAllShiftRules();
            setShiftRules(data);
        } catch (error) {
            console.error('Không thể tải danh sách quy tắc ca:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý quy tắc ca') {
            loadShiftRules();
        }
    }, [activeSidebarItem]);

    const filteredRules = shiftRules.filter(r =>
        r.ruleName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredRules.length / itemsPerPage);
    const currentRules = filteredRules.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreate = async (data) => {
        try {
            await createShiftRule(data);
            await loadShiftRules();
            setShowCreateModal(false);
            alert('Tạo quy tắc ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo quy tắc ca thất bại!');
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateShiftRule(id, data);
            await loadShiftRules();
            setShowEditModal(false);
            setEditingRule(null);
            alert('Cập nhật quy tắc ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật quy tắc ca thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteShiftRule(id);
            await loadShiftRules();
            alert('Xóa quy tắc ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa quy tắc ca thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreShiftRule(id);
            await loadShiftRules();
            alert('Khôi phục quy tắc ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục quy tắc ca thất bại!');
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo mới quy tắc ca</button>
                </div>

                {activeSidebarItem === 'Quản lý quy tắc ca' && (
                    <>
                        <ShiftRuleTable
                            data={currentRules}
                            onEdit={handleOpenEditModal}
                            onDelete={handleDelete}
                            onRestore={handleRestore}
                        />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {showCreateModal && (
                    <CreateShiftRuleModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {showEditModal && editingRule && (
                    <EditShiftRuleModal
                        rule={editingRule}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default ShiftRuleManagement;
