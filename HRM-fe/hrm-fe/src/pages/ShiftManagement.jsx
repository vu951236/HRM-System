import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllShifts,
    createShift,
    updateShift,
    deleteShift,
    restoreShift
} from '../services/shiftService';

import ShiftTable from '../components/ShiftTable';
import CreateShiftModal from '../components/CreateShiftModal';
import EditShiftModal from '../components/EditShiftModal';

const ShiftManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý ca làm việc');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [shiftsData, setShiftsData] = useState([]);
    const [editingShift, setEditingShift] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadShifts = async () => {
        try {
            const data = await fetchAllShifts();
            setShiftsData(data);
        } catch (error) {
            console.error('Không thể tải danh sách ca làm việc:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý ca làm việc') {
            loadShifts();
        }
    }, [activeSidebarItem]);

    const filteredShifts = shiftsData.filter(s =>
        s.name?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredShifts.length / itemsPerPage);
    const currentShifts = filteredShifts.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreate = async (data) => {
        try {
            await createShift(data);
            await loadShifts();
            setShowCreateModal(false);
            alert('Tạo ca làm việc thành công!'); 
        } catch (error) {
            console.error(error);
            alert('Tạo ca làm việc thất bại!');
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateShift(id, data);
            await loadShifts();
            setShowEditModal(false);
            setEditingShift(null);
            alert('Cập nhật ca làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật ca làm việc thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteShift(id);
            await loadShifts();
            alert('Xóa ca làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa ca làm việc thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreShift(id);
            await loadShifts();
            alert('Khôi phục ca làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục ca làm việc thất bại!');
        }
    };

    const handleOpenEditModal = (shift) => {
        setEditingShift(shift);
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo mới ca</button>
                </div>

                {activeSidebarItem === 'Quản lý ca làm việc' && (
                    <>
                        <ShiftTable
                            data={currentShifts}
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
                    <CreateShiftModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {showEditModal && editingShift && (
                    <EditShiftModal
                        shift={editingShift}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default ShiftManagement;
