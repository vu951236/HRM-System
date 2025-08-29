import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import Pagination from '../components/Pagination';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import SystemLogTable from '../components/SystemLogTable';
import { fetchAllSystemLogs } from '../services/systemLogService';

const SystemLogManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Nhật ký hệ thống');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [logs, setLogs] = useState([]);

    const { user } = useAuth();

    const loadLogs = async () => {
        const data = await fetchAllSystemLogs();
        setLogs(data);
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Nhật ký hệ thống') {
            loadLogs();
        }
    }, [activeSidebarItem]);

    const filteredLogs = logs.filter(l =>
        l.username?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredLogs.length / itemsPerPage);
    const currentLogs = filteredLogs.slice(
        (activePage - 1) * itemsPerPage,
        activePage * itemsPerPage
    );

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
                        setSearchTerm={value => {
                            setSearchTerm(value);
                            setActivePage(1);
                        }}
                    />
                </div>

                <div className="page-header">
                    <h1>{activeSidebarItem}</h1>
                </div>

                {activeSidebarItem === 'Nhật ký hệ thống' && (
                    <>
                        <SystemLogTable data={currentLogs} />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default SystemLogManagement;
