
import { useNavigate } from "react-router-dom";
import { api } from "../../global-api";

export class ApiService {
    

    sendRequest = async (endPoint: string, param: object, navigate:any): Promise<object> => {
       
        const token = localStorage.getItem("token");
      
        const headers : HeadersInit = {
            "Content-Type" : "application/json",
        }

        if(!api.includes("login")){
            headers["Authorization"] = `Bearer ${token}`
        }

        try {
            const response = await fetch(`${api}${endPoint}`, {
                method: "POST",
                headers:headers,
                body: JSON.stringify(param) || null ,
            });

            if (response.ok) {
                return await response.json() || null; 
            }

            if(response.status === 401) {
               alert("Your Session has expired...!");
               navigate("/login");
            }

            const errorData = await response.json();
            const errorMessage = errorData.message || "Failed to Login"; 
            throw new Error(errorMessage);
        } catch (error) {
            throw new Error(error instanceof Error ? error.message : "An error occurred");
        }
    }

}