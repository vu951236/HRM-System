import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {useAuth} from '../context/AuthContext.jsx';

const Home = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [showGuestMessage, setShowGuestMessage] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => {
            if (!user) {
                setShowGuestMessage(true);
            }
        }, 300);
        return () => clearTimeout(timer);
    }, [user]);

    const handleGoToLogin = () => {
        navigate('/login');
    };

    const handleLogout = async () => {
        await logout();
        navigate('/login');
    };

    return (
        <div style={{ padding: '2rem' }}>
            {user ? (
                <>
                    <h2>Thông tin người dùng</h2>
                    <p><strong>Username:</strong> {user.username}</p>
                    <p><strong>Role:</strong> {user.role}</p>
                    <button onClick={handleLogout}>Đăng xuất</button>
                </>
            ) : (
                showGuestMessage && (
                    <div>
                        <p>Bạn chưa đăng nhập.</p>
                        <button onClick={handleGoToLogin}>
                            Quay về trang đăng nhập
                        </button>
                    </div>
                )
            )}
        </div>
    );
};

export default Home;
