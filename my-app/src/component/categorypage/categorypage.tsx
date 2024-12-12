import CategoryService from "../../service/categoryservice";
import Navbar from "../navbar/navbar";
import './categorypage.css';

const CategoryPage = () => {

    const {
        expense,
        categoryType,
        selectedCategory,
        handleInputChange,
        handleSubmit,
        error
    } = CategoryService()


    return (
        <div className="container-box1">
            <Navbar />
            <div className="container-box2">
                <h1>View Expense By Category</h1>
                <div className="container-box3">
                    <label>
                        Choose the category type :
                    </label>
                    <select
                        id="categoryType"
                        name="categoryType"
                        value={categoryType}
                        onChange={handleInputChange}
                    >
                        <option value="">Select Category</option>
                        {
                            selectedCategory.length > 0 ? (
                                selectedCategory.map( (category, index) => (
                                    <option key={index} value={category}>
                                        {category}
                                    </option>
                                ))
                            ) :
                            (
                                <option value="" >No Categories Available</option>
                            )
                        }
                    </select>
                    <button id="btn" onClick={() => handleSubmit(categoryType)}>View Expense</button>
                </div>
                {error && <div className="error-msg">{error}</div>}
            </div>
            <table id="table-1">
            <thead>
                <tr>
                    <th>Category ID</th>
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

};

export default CategoryPage;