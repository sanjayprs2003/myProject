import './loginpage.css';
import React from 'react';
import { Link } from 'react-router-dom';
import LoginService from '../../service/loginservice';

const LoginPage: React.FC = () => {

    const {
        username,
        enPassword,
        handleInputChange,
        error,
        loading,
        handleLogin
    } = LoginService()

    return (
        <div className='container'>
            <div className='box'>
                <h1>LoginPage</h1>
                <div className='container1'>
                    <div className='inputBox'>
                        <label>Enter the Username :</label>
                        <input
                            type='text'
                            name="username"
                            value={username}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <div className='inputBox'>
                        <label>Enter the Password :</label>
                        <input
                            type='password'
                            name="password"
                            value={enPassword}
                            onChange={handleInputChange}
                            required
                        />
                    </div>
                    <button onClick={handleLogin} disabled={loading}>
                        {loading ? 'Logging in... ' : 'Login'}
                    </button>
                    {error && <p className="error-message">{error}</p>}
                    <div className='box1'>
                        <p>
                            Don't have an account? <Link to="/register" className="link1">SignUp</Link>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};


export default LoginPage;

