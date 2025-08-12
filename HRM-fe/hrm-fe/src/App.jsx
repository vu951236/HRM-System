import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Home from './pages/Home';
import ForgotPassword from './pages/ForgotPassword';
import ResetPassword from './pages/ResetPassword';

import UserManagement from './pages/UserManagement';
import EditProfilePage from './pages/EditProfilePage';
import ChangePasswordPage from "./pages/ChangePasswordPage.jsx";
import RecordManagement from "./pages/RecordManagement.jsx";

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

                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
