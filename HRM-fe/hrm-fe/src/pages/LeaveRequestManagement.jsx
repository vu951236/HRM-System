import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';

import {
    fetchAllLeaveRequests,
    approveLeaveRequest,
    rejectLeaveRequest,
    deleteLeaveRequest,
    createLeaveRequest,
    restoreLeaveRequest
} from '../services/leaveService';

import LeaveRequestTable from '../components/LeaveRequestTable';
import CreateLeaveModal from '../components/CreateLeaveModal';

const LeaveRequestManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý nghỉ phép');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [requests, setRequests] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadRequests = async () => {
        try {
            const data = await fetchAllLeaveRequests();
            setRequests(data);
        } catch (error) {
            console.error('Không thể tải danh sách nghỉ phép:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý nghỉ phép') {
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
            await approveLeaveRequest(id);
            await loadRequests();
            alert('Duyệt nghỉ phép thành công!');
        } catch (error) {
            console.error(error);
            alert('Duyệt nghỉ phép thất bại!');
        }
    };

    const handleReject = async (id) => {
        try {
            await rejectLeaveRequest(id);
            await loadRequests();
            alert('Từ chối nghỉ phép thành công!');
        } catch (error) {
            console.error(error);
            alert('Từ chối nghỉ phép thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteLeaveRequest(id);
            await loadRequests();
            alert('Xóa yêu cầu nghỉ phép thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa yêu cầu nghỉ phép thất bại!');
        }
    };

    const handleCreate = async (data) => {
        try {
            await createLeaveRequest(data);
            await loadRequests();
            setShowCreateModal(false);
            alert('Tạo yêu cầu nghỉ phép thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo yêu cầu nghỉ phép thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreLeaveRequest(id);
            await loadRequests();
            alert('Khôi phục yêu cầu nghỉ phép thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục yêu cầu nghỉ phép thất bại!');
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo yêu cầu nghỉ phép</button>
                </div>

                {activeSidebarItem === 'Quản lý nghỉ phép' && (
                    <>
                        <LeaveRequestTable
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
                    <CreateLeaveModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}
            </div>
        </div>
    );
};

export default LeaveRequestManagement;
