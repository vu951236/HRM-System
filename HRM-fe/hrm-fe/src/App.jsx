import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Home from './pages/Home';

import './App.css';
import { AuthProvider } from './context/AuthContext.jsx';

function App() {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />

                </Routes>
            </Router>
        </AuthProvider>
    );
}

export default App;
