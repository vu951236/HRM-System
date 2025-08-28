import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Home from './pages/Home';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';

import UserManagement from './pages/UserManagement';
import EditProfilePage from './pages/EditProfilePage';
import ChangePasswordPage from "./pages/ChangePasswordPage.jsx";
import RecordManagement from "./pages/RecordManagement.jsx";
import ContractManagement from "./pages/ContractManagement.jsx";
import ShiftManagement from "./pages/ShiftManagement.jsx";
import ShiftRuleManagement from "./pages/ShiftRuleManagement.jsx";
import WorkScheduleManagement from "./pages/WorkScheduleManagement.jsx";
import WorkScheduleTemplateManagement from "./pages/WorkScheduleTemplateManagement.jsx";
import ShiftSwapRequestManagement from "./pages/ShiftSwapRequestManagement.jsx";
import OvertimeRecordManagement from "./pages/OvertimeRecordManagement.jsx";
import LeaveRequestManagement from "./pages/LeaveRequestManagement.jsx";
import LeavePolicyManagement from "./pages/LeavePolicyManagement.jsx";
import AttendanceManagement from "./pages/AttendanceManagement.jsx";
import SalaryRuleManagement from "./pages/SalaryRuleManagement.jsx";
import PayrollManagement from "./pages/PayrollManagement.jsx";
import AttendanceDashboard from "./pages/AttendanceDashboard.jsx";
import EmployeeDashboard from "./pages/EmployeeDashboard.jsx";

import './App.css';
import { AuthProvider } from './context/AuthContext.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/forgot-password" element={<ForgotPassword />} />
                    <Route path="/reset-password" element={<ResetPassword />} />

=                    <Route
                        path="/employee"
                        element={
                            <ProtectedRoute>
                                <UserManagement />
                            </ProtectedRoute>
                        }
                     />
                    <Route
                        path="/editprofile"
                        element={
                            <ProtectedRoute>
                                <EditProfilePage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/changepass"
                        element={
                            <ProtectedRoute>
                                <ChangePasswordPage />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/record"
                        element={
                            <ProtectedRoute>
                                <RecordManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/contract"
                        element={
                            <ProtectedRoute>
                                <ContractManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/shift"
                        element={
                            <ProtectedRoute>
                                <ShiftManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/shiftrule"
                        element={
                            <ProtectedRoute>
                                <ShiftRuleManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/workschedule"
                        element={
                            <ProtectedRoute>
                                <WorkScheduleManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/workscheduletemplate"
                        element={
                            <ProtectedRoute>
                                <WorkScheduleTemplateManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/shiftswap"
                        element={
                            <ProtectedRoute>
                                <ShiftSwapRequestManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/overtime"
                        element={
                            <ProtectedRoute>
                                <OvertimeRecordManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/leave"
                        element={
                            <ProtectedRoute>
                                <LeaveRequestManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/leavepolicy"
                        element={
                            <ProtectedRoute>
                                <LeavePolicyManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/attendance"
                        element={
                            <ProtectedRoute>
                                <AttendanceManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/salaryrule"
                        element={
                            <ProtectedRoute>
                                <SalaryRuleManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/salary"
                        element={
                            <ProtectedRoute>
                                <PayrollManagement />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/attendancedashboard"
                        element={
                            <ProtectedRoute>
                                <AttendanceDashboard />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/employeedashboard"
                        element={
                            <ProtectedRoute>
                                <EmployeeDashboard />
                            </ProtectedRoute>
                        }
                    />

                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
