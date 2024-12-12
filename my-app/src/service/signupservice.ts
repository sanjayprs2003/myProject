import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ApiService } from "./api-service/apiservice";
import { SignupInterface } from "../module/signupmodule";

const SignupService = () => {

    const [username, setUsername] = useState<string>('');
    const [enPassword, setEnPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [error, setError] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const navigate = useNavigate();
    const apiService = new ApiService();

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const { name, value } = e.target;

        if (name === "username") {
            setUsername(value);
        } else if (name === "password") {
            setEnPassword(value);
        } else if (name === "confirmPassword") {
            setConfirmPassword(value);
        }
    }

    const checkDetails = async () => {

        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{6,}$/;

        if (!username || !enPassword || !confirmPassword) {
            setError("Please Fill the All Blanks");
            return;
        }
        if (username.length < 6 || enPassword.length < 6) {
            setError("Input Shoule be more than 6 characters");
            return;
        }
        if (!passwordRegex.test(enPassword)) {
            setError("Password Not Strong");
            return;
        }
        if (enPassword !== confirmPassword) {
            setError("Passwords Do Not Match");
            return;
        }

        setLoading(true);

        try {

            const password = btoa(enPassword);
            const params: SignupInterface.param = { username, password };
            const response = await apiService.sendRequest(SignupInterface.name, params, "") as SignupInterface.retrivel;

            if (response.success) {
                navigate("/login");
            }
        }

        catch (error) {
            setError(error instanceof Error ? error.message : 'An unexpected error occurred.');
        }

        setLoading(false);

    }

    return {
        username,
        enPassword,
        confirmPassword,
        handleInputChange,
        error,
        loading,
        checkDetails
    }
}

export default SignupService;