import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Table from '../components/UserTable.jsx';
import Pagination from '../components/Pagination';
import '../styles/Dashboard.css';
import {fetchAllUsers} from '../services/adminService.js';
import CreateUserModal from '../components/CreateUserModal.jsx';
import { createUser } from '../services/adminService.js';
import EditUserModal from '../components/EditUserModal';
import { updateUser } from '../services/adminService.js';
import { getSidebarGroups } from '../components/sidebarData';
import { useAuth } from '../context/AuthContext';
import SearchBox from "../components/SearchBox.jsx";
import { lockOrUnlockUser } from '../services/adminService.js';

const UserManagement = () => {

    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý nhân viên');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [editingUser, setEditingUser] = useState(null);
    const [usersData, setUsersData] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const loadUsers = async () => {
        try {
            const data = await fetchAllUsers();
            setUsersData(data);
        } catch (error) {
            console.error('Không thể tải danh sách nhân viên:', error);
        }
    };

    const { user } = useAuth();

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý nhân viên') {
            loadUsers();
        }
    }, [activeSidebarItem]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý nhân viên') {
            fetchAllUsers()
                .then(data => setUsersData(data))
                .catch(error => console.error('Không thể tải danh sách nhân viên:', error));
        }
    }, [activeSidebarItem]);

    const filteredUsers = usersData.filter(doc =>
        doc.fullName?.toLowerCase().includes(searchTerm.toLowerCase())
    );
    
    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredUsers.length / itemsPerPage);
    const currentUsers = filteredUsers.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);
    const handleCreateUser = async (userData) => {
        try {
            await createUser(userData);
            await loadUsers();
            setShowCreateModal(false);
        } catch (error) {
            console.error('Tạo nhân viên thất bại:', error);
            alert('Không thể tạo nhân viên.');
        }
    };


    const handleUpdateUser = async (userId, updateData) => {
        try {
            await updateUser(userId, updateData);
            await loadUsers();
            setEditingUser(null);
        } catch (error) {
            console.error('Cập nhật thất bại:', error);
            alert('Không thể cập nhật nhân viên.');
        }
    };

    const handleLockUser = async (user) => {
        const confirmText = user.isActive
            ? 'Bạn có chắc muốn khóa tài khoản này?'
            : 'Bạn có chắc muốn mở khóa tài khoản này?';
        if (!window.confirm(confirmText)) return;

        try {
            await lockOrUnlockUser(user.userId, { isActive: !user.isActive });
            await loadUsers(); // reload data
        } catch (error) {
            console.error('Lỗi khi khóa/mở khóa:', error);
            alert('Không thể thực hiện thao tác.');
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
            {showCreateModal && (
                <CreateUserModal
                    onClose={() => setShowCreateModal(false)}
                    onCreate={handleCreateUser}
                />
            )}

            <div className="main">
                <div className="top-bar">
                    <SearchBox
                        searchTerm={searchTerm}
                        setSearchTerm={(value) => {
                            setSearchTerm(value);
                            setActivePage(1);
                        }}
                    />
                    <div className="icons">
                        <i className="fa fa-bell" />
                        <i className="fa fa-question-circle" />
                    </div>
                </div>

                <div className="page-header">
                    <h1>{activeSidebarItem}</h1>
                    <button onClick={() => setShowCreateModal(true)}>Create</button>
                </div>

                {activeSidebarItem === 'Quản lý nhân viên' && (
                    <>
                        <Table
                            data={currentUsers}
                            onEdit={doc => setEditingUser({ ...doc })}
                            onDelete={id =>
                                window.confirm('Are you sure?') &&
                                setUsersData(prev => prev.filter(doc => doc.id !== id))
                            }
                            onLock={handleLockUser}
                        />
                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {editingUser && (
                    <EditUserModal
                        user={editingUser}
                        onClose={() => setEditingUser(null)}
                        onUpdate={handleUpdateUser}
                    />
                )}

            </div>
        </div>
    );
};

export default UserManagement;
