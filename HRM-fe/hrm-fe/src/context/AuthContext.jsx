import React, { createContext, useContext, useEffect, useState } from 'react';
import { refreshToken } from '../services/authService';
import { logout as logoutService } from '../services/authService';
import { fetchUserInfo } from '../services/userService';
import { attachInterceptors } from '../api/axiosConfig';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [accessToken, setAccessToken] = useState(null);

    const login = (userData, token) => {
        setUser(userData);
        setAccessToken(token);
    };

    const logout = async () => {
        if (!accessToken) return;

        try {
            await logoutService(accessToken);
            // eslint-disable-next-line no-unused-vars
        } catch (error) {
            // optional log error
        } finally {
            setUser(null);
            setAccessToken(null);
        }
    };

    useEffect(() => {
        let mounted = true;

        const tryRefreshToken = async () => {
            try {
                const newToken = await refreshToken();
                if (!newToken) throw new Error('Empty token');

                if (!mounted) return;
                setAccessToken(newToken);
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                if (!mounted) {
                    return;
                }
                setAccessToken(null);
                setUser(null);
            }
        };

        tryRefreshToken();

        return () => {
            mounted = false;
        };
    }, []);

    useEffect(() => {
        attachInterceptors(
            () => accessToken,
            setAccessToken,
            logout
        );
    }, [accessToken, logout]);

    useEffect(() => {
        if (!accessToken) {
            setUser(null);
            return;
        }

        let mounted = true;

        const getUserInfo = async () => {
            try {
                const userInfo = await fetchUserInfo();
                if (mounted) setUser(userInfo);
                // eslint-disable-next-line no-unused-vars
            } catch (err) {
                if (mounted) {
                    setUser(null);
                    setAccessToken(null);
                }
            }
        };

        getUserInfo();

        return () => {
            mounted = false;
        };
    }, [accessToken]);

    return (
        <AuthContext.Provider
            value={{
                user,
                accessToken,
                login,
                logout,
                isAuthenticated: !!user && !!accessToken,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext);
