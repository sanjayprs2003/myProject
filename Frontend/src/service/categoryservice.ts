import React, { useEffect, useState } from "react"
import { CategoryInterface } from "../module/categorymodule"
import { ApiService } from "./api-service/apiservice";
import { useNavigate } from "react-router-dom";

const CategoryService = () => {

    const [expense, setExpense] = useState<CategoryInterface.retrivel[]>([]);
    const [selectedCategory, setSelectedCategory] = useState<string[]>([]);
    const [categoryType, setCategoryType] = useState<string>("");
    const [error, setError] = useState<string>('');
    const apiService = new ApiService();
    const navigate = useNavigate();
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");

    useEffect(() => {

        if (!token) {
            navigate("/login");
        }

        if (userId && token) {
            const fetchExpenses = async () => {
                try {
                    const params: CategoryInterface.param = { userId };
                    const response = await apiService.sendRequest(CategoryInterface.view, params, navigate) as string[];
                    setSelectedCategory(response);
                } catch (error) {
                    setError("Error While Fetching...");

                }
            };

            fetchExpenses();
        }
    }, [userId, token]);

    const handleInputChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = e.target;
        if (name === "categoryType") {
            setCategoryType(value);
        }
    }


    const handleSubmit = async (categoryType: string) => {
        if (userId && token) {
            try {

                const params: CategoryInterface.param = { userId, categoryType };
                const response = await apiService.sendRequest(CategoryInterface.viewBy, params, navigate) as CategoryInterface.retrivel[];

                setExpense(response);
            }
            catch (error) {
                setError("Error fetching data: " + error);
            }
        }
    }

    return {
        expense,
        categoryType,
        selectedCategory,
        handleInputChange,
        handleSubmit,
        error
    }

};

export default CategoryService;