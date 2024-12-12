import { useEffect, useState } from "react"
import { ReportInterface } from "../module/reportmodule";
import { ApiService } from "./api-service/apiservice";
import { useNavigate } from "react-router-dom";

const ReportService = () => {

    const [report, setReport] = useState<ReportInterface.retrivel[]>([]);
    const [error, setError] = useState<string>("");
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    const navigate = useNavigate();
    const apiService = new ApiService();

    useEffect(() => {

        if (!token || !userId) {
            navigate("/login");
        }

        if (userId && token) {

            const fetchReport = async () => {

                try {
                    const params: ReportInterface.param = { userId };
                    const response = await apiService.sendRequest(ReportInterface.name, params, navigate) as ReportInterface.retrivel[];

                    setReport(response);
                }

                catch (error) {
                    setError(error instanceof Error ? error.message : "Error While Fetching...");
                }

            }; fetchReport();
        }
    }, [userId, token]);

    return {
        report,
        error
    }

};

export default ReportService;