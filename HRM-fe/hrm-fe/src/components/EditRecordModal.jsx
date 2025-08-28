import React, { useState, useEffect } from 'react';
import '../styles/Dashboard.css';
import { getDepartments, getPositions, getEmployeeTypes } from '../services/DataApi';

const EditRecordModal = ({ record, onClose, onUpdate }) => {
    const [recordData, setRecordData] = useState({
        employeeCode: '',
        userName: '',
        profileName: '',
        departmentId: '',
        positionId: '',
        employmentTypeId: '',
        supervisorId: '',
        hireDate: '',
        terminationDate: '',
        workLocation: '',
        note: '',
    });

    const [departments, setDepartments] = useState([]);
    const [positions, setPositions] = useState([]);
    const [employeeTypes, setEmployeeTypes] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                const deps = await getDepartments();
                setDepartments(deps);
            } catch {
                setDepartments([]);
            }

            try {
                const pos = await getPositions();
                setPositions(pos);
            } catch {
                setPositions([]);
            }

            try {
                const types = await getEmployeeTypes();
                setEmployeeTypes(types);
            } catch {
                setEmployeeTypes([]);
            }
        }
        fetchData();
    }, []);

    useEffect(() => {
        if (record) {
            const dep = departments.find(d => d.name === record.departmentName);
            const pos = positions.find(p => p.name === record.positionName);
            const empType = employeeTypes.find(t => t.name === record.employmentTypeName);

            setRecordData({
                employeeCode: record.employeeCode || '',
                userName: record.userName || '',
                profileName: record.profileName || '',
                departmentId: dep ? dep.id : '',
                positionId: pos ? pos.id : '',
                employmentTypeId: empType ? empType.id : '',
                supervisorId: record.supervisorId !== null && record.supervisorId !== undefined ? record.supervisorId : '',
                hireDate: record.hireDate || '',
                terminationDate: record.terminationDate || '',
                workLocation: record.workLocation || '',
                note: record.note || '',
            });
        }
    }, [record, departments, positions, employeeTypes]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setRecordData(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = () => {
        const department = departments.find(d => d.id === Number(recordData.departmentId));
        const position = positions.find(p => p.id === Number(recordData.positionId));
        const employmentType = employeeTypes.find(t => t.id === Number(recordData.employmentTypeId));

        const updateData = {
            departmentName: department ? department.name : null,
            positionName: position ? position.name : null,
            employmentTypeName: employmentType ? employmentType.name : null,
            supervisorId: recordData.supervisorId ? Number(recordData.supervisorId) : null,
            hireDate: recordData.hireDate || null,
            terminationDate: recordData.terminationDate || null,
            workLocation: recordData.workLocation || null,
            note: recordData.note || null,
            userId: record.userId,
        };

        onUpdate(record.id, updateData);
        onClose();
    };
    
    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2>✏️ Chỉnh sửa hồ sơ nhân viên</h2>

                <div className="form-group">
                    <label>Employee Code</label>
                    <input name="employeeCode" value={recordData.employeeCode} disabled />
                </div>

                <div className="form-group">
                    <label>User Name</label>
                    <input name="userName" value={recordData.userName} disabled />
                </div>

                <div className="form-group">
                    <label>Profile Name</label>
                    <input name="profileName" value={recordData.profileName} disabled />
                </div>

                <div className="form-group">
                    <label>Department</label>
                    <select name="departmentId" value={recordData.departmentId} onChange={handleChange}>
                        <option value="">-- Chọn phòng ban --</option>
                        {departments.map(dep => (
                            <option key={dep.id} value={dep.id}>{dep.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Position</label>
                    <select name="positionId" value={recordData.positionId} onChange={handleChange}>
                        <option value="">-- Chọn vị trí --</option>
                        {positions.map(pos => (
                            <option key={pos.id} value={pos.id}>{pos.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Employment Type</label>
                    <select name="employmentTypeId" value={recordData.employmentTypeId} onChange={handleChange}>
                        <option value="">-- Chọn loại nhân viên --</option>
                        {employeeTypes.map(type => (
                            <option key={type.id} value={type.id}>{type.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Supervisor ID</label>
                    <input
                        type="number"
                        name="supervisorId"
                        value={recordData.supervisorId}
                        onChange={handleChange}
                        placeholder="Nhập Supervisor ID"
                    />
                </div>

                <div className="form-group">
                    <label>Hire Date</label>
                    <input type="date" name="hireDate" value={recordData.hireDate} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Termination Date</label>
                    <input type="date" name="terminationDate" value={recordData.terminationDate} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Work Location</label>
                    <input name="workLocation" value={recordData.workLocation} onChange={handleChange} />
                </div>

                <div className="form-group">
                    <label>Note</label>
                    <textarea name="note" value={recordData.note} onChange={handleChange} />
                </div>

                <div style={{ marginTop: '20px', textAlign: 'right' }}>
                    <button onClick={onClose}>Cancel</button>
                    <button onClick={handleSubmit}>Update Record</button>
                </div>
            </div>
        </div>
    );
};

export default EditRecordModal;
