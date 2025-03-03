import { api } from "../../global-api";

export class ApiService {

    sendRequest = async (endPoint: string, param: any, navigate: any): Promise<any> => {

        const token = localStorage.getItem("token");

        const headers: HeadersInit = {
            "Content-Type": "application/json",
        }

        if (!api.includes("login")) {
            headers["Authorization"] = `Bearer ${token}`
        }

        try {
               let response = await fetch(`${api}${endPoint}`, {
                method: "POST",
                headers: headers,
                body: JSON.stringify(param) || null,
            });

            if (response.ok) {
                return await response.json() || null;
            }

            if (response.status === 401) {
                const newAccessToken = await this.refreshToken();
                if(newAccessToken) {
                        console.log("refresh Token...");
                        headers["Authorization"] = `Bearer ${newAccessToken}`;
                        response = await fetch(`${api}${endPoint}`, {
                            method : "POST",
                            headers : headers,
                            body : JSON.stringify(param) || null,
                            });

                        if(response.ok){
                            return await response.json() || null;
                           }
                       else {
                           console.log("Error Occurs During Refresh Token Fetching...");
                           navigate("/login");
                          }
                    }
                  console.log("ABCDEF");
            }

            const errorData = await response.json();
            const errorMessage = errorData.message || "Failed to Login";
            throw new Error(errorMessage);
        } catch (error) {
            throw new Error(error instanceof Error ? error.message : "An error occurred");
        }
    }

    refreshToken = async () : Promise<any> => {

        const refToken = localStorage.getItem("refToken");

        if (!refToken) {
            console.log("No refresh token found, redirecting to login...");
            return null;
          }


        try {
               const response = await fetch(`${api}refresh-token`,{
                method : "POST",
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify({token : refToken}),
            });

            if(response) {
                const newAccessToken = await response.json();
                localStorage.setItem("token", newAccessToken.token);
                return newAccessToken.token;
            }

            console.log("Failed to fetch accesstoken");
            localStorage.removeItem("token");
            localStorage.removeItem("refToken");
            return null;

        }

        catch (err) {
            console.log("1");
            throw new Error("Error? :"+err);
        }

    }

}