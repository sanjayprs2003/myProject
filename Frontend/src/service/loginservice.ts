import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ApiService } from "./api-service/apiservice";
import { LoginInterface } from "../module/loginmodule";

const LoginService = () => {

  const [username, setUsername] = useState<string>('');
  const [enPassword, setEnPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);
  const navigate = useNavigate();
  const apiService = new ApiService();

  useEffect(() => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
  }, [username]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    if (name === 'username') {
      setUsername(value);
    } else if (name === 'password') {
      setEnPassword(value);
    }
  };

  const handleLogin = async () => {
    if (!username || !enPassword) {
      setError("Username and password are required.");
      return;
    }

    setLoading(true);

    try {

      const password = btoa(enPassword);
      const params: LoginInterface.param = { username, password };
      const response = await apiService.sendRequest(LoginInterface.name, params, navigate) as LoginInterface.retrivel;

      if (response.success) {
        localStorage.setItem("token", response.token);
        localStorage.setItem("userId", response.userId);
        navigate("/homepage");
      } else {
        setError("Error while logging");
      }

    }
    catch (error) {
      setError(error instanceof Error ? error.message : "Failed To Fetch");
    }

    setLoading(false);
  };

  return {
    username,
    enPassword,
    handleInputChange,
    error,
    loading,
    handleLogin
  };
};

export default LoginService;
