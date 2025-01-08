import { useEffect, useState } from "react";
import { ApiService } from "./api-service/apiservice";
import { IncomeInterface } from "../module/incomemodule";
import { useNavigate } from "react-router-dom";

const IncomeService = () => {

    const [income, setIncome] = useState<IncomeInterface.retrivel | undefined>();
    const [showForm, setShowForm] = useState(false);
    const [incomeValue, setIncomeValue] = useState<number | undefined>();
    const [error, setError] = useState<string>("");
    const apiService = new ApiService();
    const navigate = useNavigate();

    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    useEffect(() => {
      if (!token) {
      navigate("/login");
        }
        if (userId && token) {
          const fetchIncome = async () => {
            try {
              const params: IncomeInterface.param = { userId };
              const response = await apiService.sendRequest(IncomeInterface.name, params, navigate) as IncomeInterface.retrivel;
              setError("");
              setIncome(response);
            } catch (error) {
              setError("Please Add Income");
            }
          };

          fetchIncome();
        }
      }, [userId, token]);

    const handleFormToggle = () => {
        setShowForm((prev) => !prev);
        setIncomeValue(income?.income);
      };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setIncomeValue(Number(e.target.value));
      };

    const handleSubmit = async () => {
        if (userId && token && incomeValue !== undefined) {
          try {
            const params: IncomeInterface.param = { userId, income: incomeValue };
            await apiService.sendRequest(IncomeInterface.name_1, params, navigate);
            setShowForm((prev) => !prev);
            setError("");
            setIncome((prevState) => ({
              ...prevState!,
              income: incomeValue,
            }));
          } catch (error) {
            setError("Error updating or adding income.");
          }
        }
      };

    return {
        income,
        handleSubmit,
        handleFormToggle,
        handleInputChange,
        incomeValue,
        showForm,
        error,
      };
  };

export default IncomeService;
