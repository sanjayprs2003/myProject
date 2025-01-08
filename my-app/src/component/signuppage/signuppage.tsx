import './signuppage.css';
import React from 'react';
import { Link } from 'react-router-dom';
import SignupService from '../../service/signupservice';

const SignupPage: React.FC = () => {

    const {
        username,
        enPassword,
        confirmPassword,
        handleInputChange,
        error,
        loading,
        checkDetails
    } = SignupService()

    return (
        <div className='container'>
            <div className='box'>
                <h1>SignUp Page</h1>
                <div className='container-input'>
                    <div className='inputBox1'>
                        <label>Enter the Username :</label>
                        <input
                            type='text'
                            name='username'
                            value={username}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className='inputBox1'>
                        <label>Enter the Password :</label>
                        <input
                            type='password'
                            name='password'
                            value={enPassword}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className='inputBox1'>
                        <label>Enter the Confirm Password :</label>
                        <input
                            type='password'
                            name='confirmPassword'
                            value={confirmPassword}
                            onChange={handleInputChange}
                        />
                    </div>
                    <button onClick={checkDetails} disabled={loading}>
                        {loading ? 'Registering..' : 'Register'}
                    </button>
                    {error && <p className='error-message'>{error}</p>}
                    <div>
                        <p>Already have an account? <Link to="/login" className='linkStyle'>Login</Link></p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SignupPage;