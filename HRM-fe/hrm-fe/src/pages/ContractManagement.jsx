import React, { useEffect, useState } from 'react';
import Sidebar from '../components/Sidebar';
import ContractTable from '../components/ContractTable';
import Pagination from '../components/Pagination';
import CreateContractModal from '../components/CreateContractModal';
import EditContractModal from '../components/EditContractModal';
import ExtendContractModal from '../components/ExtendContractModal';
import ContractHistoryModal from '../components/ContractHistoryModal';
import SearchBox from "../components/SearchBox";
import { useAuth } from '../context/AuthContext';
import { getSidebarGroups } from '../components/sidebarData';
import {
    fetchAllContracts,
    createContract,
    extendContract,
    terminateContract,
    softDeleteContract,
    restoreContract,
    modifyContract
} from '../services/contractService';

const ContractManagement = () => {
    const [sidebarGroups, setSidebarGroups] = useState([]);
    const [activeSidebarItem, setActiveSidebarItem] = useState('Quản lý hợp đồng');
    const [activePage, setActivePage] = useState(1);
    const [searchTerm, setSearchTerm] = useState('');
    const [contractsData, setContractsData] = useState([]);
    const [editingContract, setEditingContract] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [extendingContract, setExtendingContract] = useState(null);
    const [showExtendModal, setShowExtendModal] = useState(false);
    const [viewingHistoryContract, setViewingHistoryContract] = useState(null);

    const { user } = useAuth();

    const loadContracts = async () => {
        try {
            const data = await fetchAllContracts();
            setContractsData(data);
        } catch (error) {
            console.error('Không thể tải danh sách hợp đồng:', error);
        }
    };

    useEffect(() => {
        const groups = getSidebarGroups(user || null);
        setSidebarGroups(groups);
    }, [user]);

    useEffect(() => {
        if (activeSidebarItem === 'Quản lý hợp đồng') {
            loadContracts();
        }
    }, [activeSidebarItem]);

    const filteredContracts = contractsData.filter(c =>
        c.userName?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const itemsPerPage = 10;
    const totalPages = Math.ceil(filteredContracts.length / itemsPerPage);
    const currentContracts = filteredContracts.slice((activePage - 1) * itemsPerPage, activePage * itemsPerPage);

    const handleCreate = async (data) => {
        await createContract(data);
        await loadContracts();
    };

    const handleOpenEditModal = (contract) => {
        setEditingContract(contract);
        setShowEditModal(true);
    };

    const handleUpdate = async (id, data) => {
        await modifyContract(id, data);
        await loadContracts();
        setShowEditModal(false);
        setEditingContract(null);
    };

    const handleOpenExtendModal = (contract) => {
        setExtendingContract(contract);
        setShowExtendModal(true);
    };

    const handleConfirmExtend = async (id, newEndDate) => {
        await extendContract(id, newEndDate);
        setShowExtendModal(false);
        setExtendingContract(null);
        await loadContracts();
    };

    const handleTerminate = async (id) => {
        await terminateContract(id);
        await loadContracts();
    };

    const handleSoftDelete = async (id) => {
        await softDeleteContract(id);
        await loadContracts();
    };

    const handleRestore = async (id) => {
        await restoreContract(id);
        await loadContracts();
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
                    <button onClick={() => setShowCreateModal(true)}>Create</button>
                </div>

                {activeSidebarItem === 'Quản lý hợp đồng' && (
                    <>
                        <ContractTable
                            data={currentContracts}
                            onEdit={handleOpenEditModal}
                            onExtend={handleOpenExtendModal}
                            onTerminate={handleTerminate}
                            onSoftDelete={handleSoftDelete}
                            onRestore={handleRestore}
                            onViewHistory={setViewingHistoryContract}
                        />

                        <Pagination
                            currentPage={activePage}
                            totalPages={totalPages}
                            onChangePage={setActivePage}
                        />
                    </>
                )}

                {showCreateModal && (
                    <CreateContractModal
                        onClose={() => setShowCreateModal(false)}
                        onCreate={handleCreate}
                    />
                )}

                {viewingHistoryContract && (
                    <ContractHistoryModal
                        contract={viewingHistoryContract}
                        onClose={() => setViewingHistoryContract(null)}
                    />
                )}

                {showEditModal && editingContract && (
                    <EditContractModal
                        contract={editingContract}
                        onClose={() => setShowEditModal(false)}
                        onUpdate={handleUpdate}
                    />
                )}

                {showExtendModal && extendingContract && (
                    <ExtendContractModal
                        contract={extendingContract}
                        onClose={() => setShowExtendModal(false)}
                        onExtend={handleConfirmExtend}
                    />
                )}

            </div>
        </div>
    );
};

export default ContractManagement;
