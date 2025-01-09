import HomeService from "../../service/homepageservice";
import Navbar from "../navbar/navbar";
import './homepage.css'

const HomePage: React.FC = () => {

  const {
    expenses,
    updatingExpenses,
    viewUpdateExpense,
    handleCloseUpdate,
    handleDelete,
    handleInputChange,
    handleUpdate,
    error,
    updateError
  } = HomeService()

  return (
    <div className="viewbox-1">
      <Navbar />
      <h1>View All Expenses</h1>
      <div className="viewbox-2">
        {error && <div className="error-message">{error}</div>}
        {expenses.length === 0 ? (
          <div>Please Add Expenses...</div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Category ID</th>
                <th>Amount</th>
                <th>Category Name</th>
                <th>Description</th>
                <th>Category Type</th>
                <th>Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {expenses.map((expense) => (
                <tr key={expense.categoryid}>
                  <td>{expense.categoryid}</td>
                  <td>{expense.amount}</td>
                  <td>{expense.name}</td>
                  <td>{expense.description}</td>
                  <td>{expense.type}</td>
                  <td>{expense.date}</td>
                  <td>
                    <button onClick={() => viewUpdateExpense(expense)}>Edit</button>
                    <button onClick={() => handleDelete(expense.categoryid)}>Delete</button>
                  </td>
                </tr>
              ))
              }
            </tbody>
          </table>
        )}
      </div>

      {updatingExpenses && (
        <>
          <div className="overlay"></div>
          <div className="viewbox-3">
            <h1>Update Expense</h1>
            <div className="form-group">
              <label htmlFor="categoryid">Category ID:</label>
              <input type="text" id="categoryid" value={updatingExpenses.categoryid} disabled />
            </div>
            <div className="form-group">
              <label htmlFor="amount">Expense Amount:</label>
              <input
                type="number" id="amount" name="amount" value={updatingExpenses.amount} onChange={handleInputChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="name">Category Name:</label>
              <input type="text" id="name" name="name" value={updatingExpenses.name} onChange={handleInputChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="description">Category Description:</label>
              <input type="text" id="description" name="description" value={updatingExpenses.description} onChange={handleInputChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="type">Category Type:</label>
              <input type="text" id="type" name="type" value={updatingExpenses.type} onChange={handleInputChange} required />
            </div>
            <div className="form-group">
              <label htmlFor="date">Date:</label>
              <input type="date" id="date" name="date" value={updatingExpenses.date} onChange={handleInputChange} required />
            </div>
            {updateError && <div className="error-box">{updateError}</div>}
            <button onClick={handleUpdate}>Update Expense</button>
            <button onClick={handleCloseUpdate}>Close</button>
          </div>
        </>
      )
      }

    </div>
  );
};

export default HomePage;
