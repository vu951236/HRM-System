import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Table from '../components/RecordTable.jsx';
import Pagination from '../components/Pagination';
import '../styles/Dashboard.css';
import { fetchAllRecord, createRecord, updateRecord, softDeleteRecord, restoreRecord } from '../services/recordService.js';
import CreateRecordModal from '../components/CreateRecordModal.jsx';
import EditRecordModal from '../components/EditRecordModal.jsx';
import { getSidebarGroups } from '../components/sidebarData';
import { useAuth } from '../context/AuthContext';
import SearchBox from "../components/SearchBox.jsx";

const RecordManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý hồ sơ');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [editingRecord, setEditingRecord] = useState(null);
    const [recordsData, setRecordsData] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadRecords = async () => {
        try {
            const data = await fetchAllRecord();
            setRecordsData(data);
        } catch (error) {
            console.error('Không thể tải danh sách hồ sơ:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý hồ sơ') {
            loadRecords();
        }
    }, [activeSidebarItem]);

    const filteredRecords = recordsData.filter(doc =>
        doc.userName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredRecords.length / itemsPerPage);
    const currentRecords = filteredRecords.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreateRecord = async (recordData) => {
        try {
            await createRecord(recordData);
            await loadRecords();
            setShowCreateModal(false);
        } catch (error) {
            console.error('Tạo hồ sơ thất bại:', error);
            alert('Không thể tạo hồ sơ.');
        }
    };

    const handleUpdateRecord = async (recordId, updateData) => {
        try {
            await updateRecord(recordId, updateData);
            await loadRecords();
            setEditingRecord(null);
        } catch (error) {
            console.error('Cập nhật hồ sơ thất bại:', error);
            alert('Không thể cập nhật hồ sơ.');
        }
    };

    const handleSoftDelete = async (id) => {
        try {
            await softDeleteRecord(id);
            await loadRecords();
        } catch (error) {
            console.error('Xóa mềm thất bại:', error);
            alert('Không thể xóa hồ sơ.');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreRecord(id);
            await loadRecords();
        } catch (error) {
            console.error('Khôi phục thất bại:', error);
            alert('Không thể khôi phục hồ sơ.');
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
                <CreateRecordModal
                    onClose={() => setShowCreateModal(false)}
                    onCreate={handleCreateRecord}
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

                {activeSidebarItem === 'Quản lý hồ sơ' && (
                    <>
                        <Table
                            data={currentRecords}
                            onEdit={doc => setEditingRecord({ ...doc, userId: doc.userId || null })}
                            onSoftDelete={handleSoftDelete}
                            onRestore={handleRestore}
                        />
                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {editingRecord && (
                    <EditRecordModal
                        record={editingRecord}
                        onClose={() => setEditingRecord(null)}
                        onUpdate={handleUpdateRecord}
                    />
                )}
            </div>
        </div>
    );
};

export default RecordManagement;
