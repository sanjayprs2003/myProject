import ReportService from "../../service/reportservice"
import Navbar from "../navbar/navbar"
import './reportpage.css';

const ReportPage = () => {

    const {
        report,
        error
    } = ReportService()

    return (
        <div className="report-1">
            <Navbar />
            <div className="report-2">
                <h1>User Report</h1>
                <div>
                    <table className="report-3">
                        <thead className="report-4">
                            <tr>
                                <th className="report-5">Type</th>
                                <th className="report-5">Percentage</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                report.length > 0 ? (
                                    report.map((report, index) => (
                                        <tr key={index}>
                                            <td>{report.type}</td>
                                            <td>{report.percentage}</td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={6}>Please Give Income and Expense</td>
                                    </tr>
                                )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    )

};

export default ReportPage;