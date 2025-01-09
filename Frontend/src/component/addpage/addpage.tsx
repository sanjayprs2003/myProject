import AddService from "../../service/addservice";
import Navbar from "../navbar/navbar";
import './addpage.css';

const AddPage = () => {

    const {
        categoryid,
        amount,
        name,
        description,
        type,
        date,
        error,
        loading,
        handleInputChange,
        handleSubmit
    } = AddService()

    return (
        <div className="addpage-1">
            <Navbar />
            <div className="addpage-2">
                <div className="addpage-3">
                    <h1>Add Expenses</h1>
                    <div>
                        <div className="form">
                            <label>Enter The Category ID :</label>
                            <input type="text" name="categoryid" value={categoryid} onChange={handleInputChange} required />
                        </div>
                        <div className="form">
                            <label>Enter The Amount :</label>
                            <input type="number" name="amount" value={amount} onChange={handleInputChange} required />
                        </div>
                        <div className="form">
                            <label>Enter The Name :</label>
                            <input type="text" name="name" value={name} onChange={handleInputChange} required />
                        </div>
                        <div className="form">
                            <label>Enter The Description :</label>
                            <input type="text" name="description" value={description} onChange={handleInputChange} required />
                        </div>
                        <div className="form">
                            <label>Enter The Type :</label>
                            <input type="text" name="type" value={type} onChange={handleInputChange} required />
                        </div>
                        <div className="form">
                            <label>Enter The Date :</label>
                            <input type="date" name="date" value={date} onChange={handleInputChange} required />
                        </div>
                        {error && <p className="error-message">{error}</p>}
                        <button onClick={handleSubmit} disabled={loading}>{loading ? "Submiting..." : "Submit"} </button>
                    </div>
                </div>
            </div>
        </div>
    )


}

export default AddPage;