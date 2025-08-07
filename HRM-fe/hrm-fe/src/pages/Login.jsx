import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import '../styles/Login.css';
import { FaEnvelope, FaLock } from 'react-icons/fa';
import { IoEyeSharp, IoEyeOffSharp } from 'react-icons/io5';
import { FiGlobe, FiHelpCircle } from 'react-icons/fi';
import logo from '../assets/logo.png';
import imgMain from '../assets/img-main.png';
import { login as loginService } from '../services/authService';
import { useNavigate } from 'react-router-dom';

function Login() {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });
    const [showPassword, setShowPassword] = useState(false);
    const navigate = useNavigate();

    const { login } = useAuth();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const toggleShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const { token, user } = await loginService(formData.username, formData.password);
            login(user, token);
            alert('Login successful!');
            navigate('/');
        } catch (error) {
            alert(error.message || 'Login failed');
        }
    };

    return (
        <div className="login-wrapper">
            {/* Header */}
            <header className="header">
                <div className="left-section">
                    <img src={logo} alt="Logo" className="logo-image" />
                    <p className="heading">HRM</p>
                </div>
                <div className="icons">
                    <FiGlobe className="icon" />
                    <FiHelpCircle className="icon" />
                </div>
            </header>

            <main className="main-content">
                <div className="visual-area">
                    <img src={imgMain} alt="Main" className="hero" />
                </div>

                <div className="form-area">
                    <h2 className="title">Welcome back 👋</h2>
                    <p className="subtitle">Log in your account</p>
                    <form className="login-form" onSubmit={handleSubmit}>
                        <div className="textbox">
                            <FaEnvelope className="textbox-icon left" />
                            <input
                                type="text"
                                name="username"
                                placeholder="What is your username?"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className="textbox">
                            <FaLock className="textbox-icon left" />
                            {showPassword ? (
                                <IoEyeSharp className="textbox-icon right" onClick={toggleShowPassword} />
                            ) : (
                                <IoEyeOffSharp className="textbox-icon right" onClick={toggleShowPassword} />
                            )}
                            <input
                                type={showPassword ? 'text' : 'password'}
                                name="password"
                                placeholder="Enter your password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <p className="footer link" onClick={() => navigate('/forgot-password')} style={{ textAlign: 'right', cursor: 'pointer' }}>
                            Forgot password?
                        </p>

                        <button type="submit" className="button">Continue</button>
                    </form>
                </div>
            </main>
        </div>
    );
}

export default Login;
