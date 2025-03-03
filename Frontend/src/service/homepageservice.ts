import { useState, useEffect } from "react";
import { ApiService } from "./api-service/apiservice";
import { HomeInterface } from "../module/homemodule";
import { useNavigate } from "react-router-dom";

const HomeService = () => {

  const [expenses, setExpenses] = useState<HomeInterface.retrivel[]>([]);
  const [updatingExpenses, setUpdatingExpenses] = useState<HomeInterface.retrivel | null>(null);
  const [error, setError] = useState<string>('');
  const [updateError, setUpdateError] = useState<string>('');
  const navigate = useNavigate();
  const apiService = new ApiService();

  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem("token");

  useEffect(() => {

    if (!token) {
      navigate("/login");
    }

    if (userId && token) {
      const fetchExpense = async () => {
        try {
          const params: HomeInterface.param = { userId };
          const response = await apiService.sendRequest(HomeInterface.view, params, navigate) as HomeInterface.retrivel;
  
          if (Array.isArray(response)) {
            setExpenses(response);
          } else {
            setExpenses([]);
            setError("Response is not an array.");
          }
        } catch (err) {
            console.log("Failed to load expenses: " + (err instanceof Error ? err.message : err));
            navigate("/login");
        }
      };

      fetchExpense();
    }
  }, [userId, token]);

  const handleDelete = async (categoryid: string) => {
    if (!userId || !token) {
      setError("User is not authenticated");
      return;
    }

    const isConfirm = window.confirm("Are you sure you want to delete this expense?");
    if (isConfirm) {
      try {
        const params: HomeInterface.param = { userId, categoryid: String(categoryid) };
        await apiService.sendRequest(HomeInterface.deleteBy, params, navigate);
        setExpenses((prevExpenses) => prevExpenses.filter((expense) => expense.categoryid !== categoryid));
      } catch (error: any) {
        setError(error.message || "Failed to delete expense");
      }
    }
  };

  const viewUpdateExpense = (expense: HomeInterface.retrivel) => {

    setUpdateError("");
    setUpdatingExpenses(expense);
  }

  const handleCloseUpdate = () => {

    setUpdatingExpenses(null);
  };

  const handleUpdate = async () => {
    if (!updatingExpenses) {
      setUpdateError("No expenses selected for update");
      return;
    }

    if (!updatingExpenses.categoryid || !updatingExpenses.amount || !updatingExpenses.name || !updatingExpenses.description || !updatingExpenses.type || !updatingExpenses.date) {
      setUpdateError("Please Fill the all blanks correctly");
      return;
    }

    if (updatingExpenses.amount <= 0) {
      setUpdateError("Amount Should Be Positive Values");
      return;
    }

    if (!userId || !token) {
      setUpdateError("User is not authenticated");
      return;
    }

    try {
      const params: HomeInterface.param = {
        userId: userId,
        categoryid: String(updatingExpenses.categoryid),
        amount: Number(updatingExpenses.amount),
        name: updatingExpenses.name.trim(),
        description: updatingExpenses.description.trim(),
        type: updatingExpenses.type.trim(),
        date: updatingExpenses.date
      };

      await apiService.sendRequest(HomeInterface.updateBy, params, navigate);

      setUpdatingExpenses(null);
      setUpdateError("");
      setExpenses((prevExpenses) =>
        prevExpenses.map((expense) =>
          expense.categoryid === updatingExpenses.categoryid ? updatingExpenses : expense
        )
      );
    } catch (error) {
      setUpdateError(error instanceof Error ? error.message : "Failed to update expense");
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (updatingExpenses) {
      const { name, value } = e.target;
      setUpdatingExpenses({
        ...updatingExpenses,
        [name]: value,
      });
    }
  };

  return {
    expenses,
    updatingExpenses,
    viewUpdateExpense,
    handleCloseUpdate,
    handleDelete,
    handleInputChange,
    handleUpdate,
    error,
    updateError
  };
};

export default HomeService;
