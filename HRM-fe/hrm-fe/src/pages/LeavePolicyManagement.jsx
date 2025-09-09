import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Dashboard/Sidebar.jsx';
import Pagination from '../components/Dashboard/Pagination.jsx';
import SearchBox from "../components/Dashboard/SearchBox.jsx";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/Dashboard/sidebarData.js';
import {
    fetchAllLeavePolicies,
    createLeavePolicy,
    updateLeavePolicy,
    deleteLeavePolicy,
    restoreLeavePolicy
} from '../services/leavePolicyService';
import LeavePolicyTable from '../components/LeavePolicy/LeavePolicyTable.jsx';
import CreateLeavePolicyModal from '../components/LeavePolicy/CreateLeavePolicyModal.jsx';
import EditLeavePolicyModal from '../components/LeavePolicy/EditLeavePolicyModal.jsx';

const LeavePolicyManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý chính sách nghỉ');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [leavePolicies, setLeavePolicies] = useState([]);
    const [editingPolicy, setEditingPolicy] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadLeavePolicies = async () => {
        try {
            const data = await fetchAllLeavePolicies();
            setLeavePolicies(data);
        } catch (error) {
            console.error('Không thể tải danh sách chính sách nghỉ:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý chính sách nghỉ') {
            loadLeavePolicies();
        }
    }, [activeSidebarItem]);

    const filteredPolicies = leavePolicies.filter(p =>
        p.policyName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredPolicies.length / itemsPerPage);
    const currentPolicies = filteredPolicies.slice(
        (activePage - 1) * itemsPerPage,
        activePage * itemsPerPage
    );

    const handleCreate = async (data) => {
        try {
            await createLeavePolicy(data);
            await loadLeavePolicies();
            setShowCreateModal(false);
            alert('Tạo chính sách nghỉ thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo chính sách nghỉ thất bại!');
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateLeavePolicy(id, data);
            await loadLeavePolicies();
            setShowEditModal(false);
            setEditingPolicy(null);
            alert('Cập nhật chính sách nghỉ thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật chính sách nghỉ thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteLeavePolicy(id);
            await loadLeavePolicies();
            alert('Xóa chính sách nghỉ thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa chính sách nghỉ thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreLeavePolicy(id);
            await loadLeavePolicies();
            alert('Khôi phục chính sách nghỉ thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục chính sách nghỉ thất bại!');
        }
    };

    const handleOpenEditModal = (policy) => {
        setEditingPolicy(policy);
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo mới chính sách nghỉ</button>
                </div>

                {activeSidebarItem === 'Quản lý chính sách nghỉ' && (
                    <>
                        <LeavePolicyTable
                            data={currentPolicies}
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
                    <CreateLeavePolicyModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {showEditModal && editingPolicy && (
                    <EditLeavePolicyModal
                        policy={editingPolicy}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default LeavePolicyManagement;
