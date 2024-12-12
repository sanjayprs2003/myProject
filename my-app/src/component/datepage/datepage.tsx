import DateService from "../../service/dateservice"
import Navbar from "../navbar/navbar"
import './datepage.css'

const DatePage = () => {

    const {
        startDate,
        lastDate,
        handleInputChange,
        handleSubmit,
        expense,
        error
    } = DateService()

    return (
        <div className="div1">
            <Navbar />
            <div className="div2">
                <h1>View Expense By Date</h1>
                <div className="div3">
                    <div className="div4">
                        <label>Start Date :</label>
                        <input type="date" name="startDate" value={startDate} onChange={handleInputChange} required/>
                    </div>
                    <div className="div4">
                        <label>Last Date :</label>
                        <input type="date" name="lastDate" value={lastDate} onChange={handleInputChange} required/>
                    </div>
                    <button id="butn" onClick={handleSubmit}>View Expense</button>
                </div>
               
                {error && <div className="error-message">{error}</div>}
            </div>
            <table className="table-1">
                <thead>
                    <tr>
                        <th>Category Id</th>
                        <th>Amount</th>
                        <th>Category Name</th>
                        <th>Description</th>
                        <th>Category Type</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
                {expense.length > 0 ? (
             expense.map((expenseItem, index) => (
                <tr key={index}>
                    <td>{expenseItem.categoryid}</td> 
                    <td>{expenseItem.amount}</td>
                    <td>{expenseItem.name}</td>
                    <td>{expenseItem.description}</td>
                    <td>{expenseItem.type}</td>
                    <td>{expenseItem.date}</td>
                </tr>
            ))
        ) : (
            <tr>
                <td colSpan={6}>No data available</td>
            </tr>
        )}
                </tbody>
            </table>
        </div>
    )

}

export default DatePage;