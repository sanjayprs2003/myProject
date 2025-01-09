import { useEffect, useState } from "react";
import { DateInterface } from "../module/datemodule";
import { ApiService } from "./api-service/apiservice";
import { useNavigate } from "react-router-dom";

const DateService = () => {

    const [startDate, setStartDate] = useState<string>();
    const [lastDate, setLastDate] = useState<string>();
    const [expense, setExpense] = useState<DateInterface.retrivel[]>([]);
    const [error, setError] = useState<string>("");
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    const navigate = useNavigate();
    const apiService = new ApiService();

    useEffect(() => {

        if (!token) {
            navigate("/login");
        }
    }, [navigate]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {

        const { name, value } = e.target;
        if (name === "startDate") {
            setStartDate(value);
        }
        else if (name === "lastDate") {
            setLastDate(value);
        }
    }

    const handleSubmit = async () => {

        if (userId && token) {

            if (!userId || !startDate || !lastDate) {
                setError("Please Give Date");
                return;
            }

            const params: DateInterface.param = { userId, startDate, lastDate };
            const response = await apiService.sendRequest(DateInterface.name, params, navigate) as DateInterface.retrivel[];

            setError("");
            setExpense(response);
        }
        else {
            setError("Error While fetching...");
        }

    }

    return {
        startDate,
        lastDate,
        handleSubmit,
        handleInputChange,
        expense,
        error
    };

}

export default DateService;
