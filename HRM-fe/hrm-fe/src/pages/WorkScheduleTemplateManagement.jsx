import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Dashboard/Sidebar.jsx';
import Pagination from '../components/Dashboard/Pagination.jsx';
import SearchBox from "../components/Dashboard/SearchBox.jsx";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/Dashboard/sidebarData.js';

import {
    fetchAllTemplates,
    createTemplate,
    updateTemplate,
    deleteTemplate,
    restoreTemplate,
    applyTemplate
} from '../services/workScheduleTemplateService';

import WorkScheduleTemplateTable from '../components/WorkScheduleTemplate/WorkScheduleTemplateTable.jsx';
import CreateWorkScheduleTemplateModal from '../components/WorkScheduleTemplate/CreateWorkScheduleTemplateModal.jsx';
import EditWorkScheduleTemplateModal from '../components/WorkScheduleTemplate/EditWorkScheduleTemplateModal.jsx';

const WorkScheduleTemplateManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý mẫu lịch làm việc');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [templates, setTemplates] = useState([]);
    const [editingTemplate, setEditingTemplate] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const { user } = useAuth();

    const loadTemplates = async () => {
        try {
            const data = await fetchAllTemplates();
            setTemplates(data);
        } catch (error) {
            console.error('Không thể tải danh sách mẫu lịch làm việc:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý mẫu lịch làm việc') {
            loadTemplates();
        }
    }, [activeSidebarItem]);

    const filteredTemplates = templates.filter(t =>
        t.templateName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredTemplates.length / itemsPerPage);
    const currentTemplates = filteredTemplates.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreate = async (data) => {
        try {
            await createTemplate(data);
            await loadTemplates();
            setShowCreateModal(false);
            alert('Tạo mẫu lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Tạo mẫu lịch làm việc thất bại!');
        }
    };

    const handleUpdate = async (id, data) => {
        try {
            await updateTemplate(id, data);
            await loadTemplates();
            setShowEditModal(false);
            setEditingTemplate(null);
            alert('Cập nhật mẫu lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Cập nhật mẫu lịch làm việc thất bại!');
        }
    };

    const handleDelete = async (id) => {
        try {
            await deleteTemplate(id);
            await loadTemplates();
            alert('Xóa mẫu lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Xóa mẫu lịch làm việc thất bại!');
        }
    };

    const handleRestore = async (id) => {
        try {
            await restoreTemplate(id);
            await loadTemplates();
            alert('Khôi phục mẫu lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Khôi phục mẫu lịch làm việc thất bại!');
        }
    };

    const handleApply = async (id, startDate, endDate, overwrite = false) => {
        try {
            await applyTemplate(id, startDate, endDate, overwrite);
            alert('Áp dụng mẫu lịch làm việc thành công!');
        } catch (error) {
            console.error(error);
            alert('Áp dụng mẫu lịch làm việc thất bại!');
        }
    };

    const handleOpenEditModal = (template) => {
        setEditingTemplate(template);
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
                    <button onClick={() => setShowCreateModal(true)}>Tạo mới mẫu lịch</button>
                </div>

                {activeSidebarItem === 'Quản lý mẫu lịch làm việc' && (
                    <>
                        <WorkScheduleTemplateTable
                            data={currentTemplates}
                            onEdit={handleOpenEditModal}
                            onDelete={handleDelete}
                            onRestore={handleRestore}
                            onApply={handleApply}
                        />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {showCreateModal && (
                    <CreateWorkScheduleTemplateModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {showEditModal && editingTemplate && (
                    <EditWorkScheduleTemplateModal
                        template={editingTemplate}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}
            </div>
        </div>
    );
};

export default WorkScheduleTemplateManagement;
