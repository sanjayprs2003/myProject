import { BrowserRouter, Route, Routes } from "react-router-dom";
import LoginPage from "../component/loginpage/loginpage";
import SignupPage from "../component/signuppage/signuppage";
import HomePage from "../component/homepage/homepage";
import AddPage from "../component/addpage/addpage";
import IncomePage from "../component/incomepage/incomepage";
import CategoryPage from "../component/categorypage/categorypage";
import DatePage from "../component/datepage/datepage";
import ReportPage from "../component/reportpage/reportpage";

 const AppRouter = () => {

  return (

    <BrowserRouter>
    <Routes>
        <Route path='/login' element={<LoginPage />} ></Route>
        <Route path="/register" element={<SignupPage />}></Route>
        <Route path='*' element={<LoginPage />} ></Route>
        <Route path='/homepage' element={<HomePage />}></Route>
        <Route path="/addpage" element={<AddPage />}></Route>
        <Route path="/incomepage" element={<IncomePage />}></Route>
        <Route path="/categorypage" element={<CategoryPage />}></Route>
        <Route path="/datepage" element={<DatePage />}></Route>
        <Route path="/reportpage" element={<ReportPage />}></Route>
    </Routes> 
    </BrowserRouter>

  )

}

export default AppRouter;
