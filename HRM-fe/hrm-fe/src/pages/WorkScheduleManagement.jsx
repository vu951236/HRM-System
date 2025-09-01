import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllWorkSchedules,
    createWorkSchedule,
    updateWorkSchedule,
    deleteWorkSchedule,
    restoreWorkSchedule
} from '../services/workScheduleService';

import WorkScheduleTable from '../components/WorkScheduleTable';
import CreateWorkScheduleModal from '../components/CreateWorkScheduleModal';
import EditWorkScheduleModal from '../components/EditWorkScheduleModal';

const WorkScheduleManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý lịch làm việc');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [schedules, setSchedules] = useState([]);
    const [editingSchedule, setEditingSchedule] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadSchedules = async () => {
        try {
            const data = await fetchAllWorkSchedules();
            setSchedules(data);
        } catch (error) {
            console.error('Không thể tải danh sách lịch làm việc:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý lịch làm việc') {
            loadSchedules();
        }
    }, [activeSidebarItem]);

    const filteredSchedules = schedules.filter(s =>
        s.employeeCode?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredSchedules.length / itemsPerPage);
    const currentSchedules = filteredSchedules.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreate = async (data) => {
        try {
            await createWorkSchedule(data);
            await loadSchedules();
            setShowCreateModal(false);
            alert('Tạo lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo lịch làm việc thất bại!');
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateWorkSchedule(id, data);
            await loadSchedules();
            setShowEditModal(false);
            setEditingSchedule(null);
            alert('Cập nhật lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật lịch làm việc thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteWorkSchedule(id);
            await loadSchedules();
            alert('Xóa lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa lịch làm việc thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreWorkSchedule(id);
            await loadSchedules();
            alert('Khôi phục lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục lịch làm việc thất bại!');
        }
    };

    const handleOpenEditModal = (schedule) => {
        setEditingSchedule(schedule);
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo mới lịch làm việc</button>
                </div>

                {activeSidebarItem === 'Quản lý lịch làm việc' && (
                    <>
                        <WorkScheduleTable
                            data={currentSchedules}
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
                    <CreateWorkScheduleModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {showEditModal && editingSchedule && (
                    <EditWorkScheduleModal
                        schedule={editingSchedule}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default WorkScheduleManagement;
