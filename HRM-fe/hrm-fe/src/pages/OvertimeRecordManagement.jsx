import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';

import {
    fetchAllOvertimeRecords,
    approveOvertimeRecord,
    rejectOvertimeRecord,
    deleteOvertimeRecord,
    restoreOvertimeRecord,
    createOvertimeRecord
} from '../services/overtimeService';

import OvertimeRecordTable from '../components/OvertimeRecordTable';
import CreateOvertimeModal from '../components/CreateOvertimeModal';

const OvertimeRecordManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý tăng ca');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [records, setRecords] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadRecords = async () => {
        try {
            const data = await fetchAllOvertimeRecords();
            setRecords(data);
        } catch (error) {
            console.error('Không thể tải danh sách tăng ca:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý tăng ca') {
            loadRecords();
        }
    }, [activeSidebarItem]);

    const filteredRecords = records.filter(r =>
        r.id?.toString().toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredRecords.length / itemsPerPage);
    const currentRecords = filteredRecords.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleApprove = async (id) => {
        try {
            await approveOvertimeRecord(id);
            await loadRecords();
            alert('Duyệt tăng ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Duyệt tăng ca thất bại!');
        }
    };

    const handleReject = async (id) => {
        try {
            await rejectOvertimeRecord(id);
            await loadRecords();
            alert('Từ chối tăng ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Từ chối tăng ca thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteOvertimeRecord(id);
            await loadRecords();
            alert('Xóa bản ghi tăng ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa bản ghi tăng ca thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreOvertimeRecord(id);
            await loadRecords();
            alert('Khôi phục bản ghi tăng ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục bản ghi tăng ca thất bại!');
        }
    };

    const handleCreate = async (data) => {
        try {
            await createOvertimeRecord(data);
            await loadRecords();
            setShowCreateModal(false);
            alert('Tạo bản ghi tăng ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo bản ghi tăng ca thất bại!');
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo tăng ca</button>
                </div>

                {activeSidebarItem === 'Quản lý tăng ca' && (
                    <>
                        <OvertimeRecordTable
                            data={currentRecords}
                            onApprove={handleApprove}
                            onReject={handleReject}
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
                    <CreateOvertimeModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}
            </div>
        </div>
    );
};

export default OvertimeRecordManagement;
