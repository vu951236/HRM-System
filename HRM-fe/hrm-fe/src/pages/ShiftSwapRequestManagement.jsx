import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Dashboard/Sidebar.jsx';
import Pagination from '../components/Dashboard/Pagination.jsx';
import SearchBox from "../components/Dashboard/SearchBox.jsx";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/Dashboard/sidebarData.js';

import {
    fetchAllShiftSwapRequests,
    approveShiftSwapRequest,
    rejectShiftSwapRequest,
    deleteShiftSwapRequest,
    restoreShiftSwapRequest,
    createShiftSwapRequest
} from '../services/shiftSwapService';

import ShiftSwapRequestTable from '../components/ShiftSwap/ShiftSwapRequestTable.jsx';
import CreateShiftSwapRequestModal from '../components/ShiftSwap/CreateShiftSwapRequestModal.jsx';

const ShiftSwapRequestManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý yêu cầu đổi ca');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [requests, setRequests] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadRequests = async () => {
        try {
            const data = await fetchAllShiftSwapRequests();
            setRequests(data);
        } catch (error) {
            console.error('Không thể tải danh sách yêu cầu đổi ca:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý yêu cầu đổi ca') {
            loadRequests();
        }
    }, [activeSidebarItem]);

    const filteredRequests = requests.filter(r =>
        r.id?.toString().toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredRequests.length / itemsPerPage);
    const currentRequests = filteredRequests.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleApprove = async (id) => {
        try {
            await approveShiftSwapRequest(id, user.id);
            await loadRequests();
            alert('Duyệt yêu cầu thành công!');
        } catch (error) {
            console.error(error);
            alert('Duyệt yêu cầu thất bại!');
        }
    };

    const handleReject = async (id) => {
        try {
            await rejectShiftSwapRequest(id, user.id);
            await loadRequests();
            alert('Từ chối yêu cầu thành công!');
        } catch (error) {
            console.error(error);
            alert('Từ chối yêu cầu thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteShiftSwapRequest(id);
            await loadRequests();
            alert('Xóa yêu cầu thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa yêu cầu thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreShiftSwapRequest(id);
            await loadRequests();
            alert('Khôi phục yêu cầu thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục yêu cầu thất bại!');
        }
    };

    const handleCreate = async (data) => {
        try {
            await createShiftSwapRequest(data);
            await loadRequests();
            setShowCreateModal(false);
            alert('Tạo yêu cầu đổi ca thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo yêu cầu thất bại!');
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo yêu cầu đổi ca</button>
                </div>

                {activeSidebarItem === 'Quản lý yêu cầu đổi ca' && (
                    <>
                        <ShiftSwapRequestTable
                            data={currentRequests}
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
                    <CreateShiftSwapRequestModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}
            </div>
        </div>
    );
};

export default ShiftSwapRequestManagement;
