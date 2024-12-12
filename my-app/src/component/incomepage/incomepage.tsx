import IncomeService from "../../service/incomeservice";
import Navbar from "../navbar/navbar";
import "./incomepage.css";

const IncomePage = () => {

    const {
        income,
        handleSubmit,
        handleFormToggle,
        handleInputChange,
        showForm,
        incomeValue,
        error,
    } = IncomeService();

    return (
        <div className="incomepage-1">
          <Navbar />
          <div className="incomepage-2">
             <div className="incomepage-3">
               <h1>Income</h1>
               <div className="incomepage-4">
                 {income && income.income !== undefined ? (
                   <div>Your Income: {income.income}</div>
                 ) : (
                   <div>Your Income: Not added yet</div>
                 )}
               </div>
               {error && <div id="error-message">{error}</div>}
               <button onClick={handleFormToggle}>
                 {income?.income !== undefined ? "Update" : "Add"} Income
               </button>
             </div>
           </div>

          {showForm && (
             <div className={`income-form ${showForm ? "show" : ""}`}>
                <input
                  type="number"
                  value={incomeValue}
                  onChange={handleInputChange}
                  placeholder="Enter income"
                 />
              <button onClick={() => handleSubmit()}>
                {income?.income !== undefined ? "Update" : "Add"} Income
              </button>
            </div>
          )}
        </div>
    );
};

export default IncomePage;
