import React from "react";
import { Link } from "react-router-dom";
import './navbar.css';

const Navbar = () => {
  return (
    <div className="navbar">
      <h1>Personal Expense Tracker</h1>
      <div>
        <Link to="/incomepage">Income</Link>
        <Link to="/addpage">Add Expense</Link>
        <Link to="/homepage">View All Expenses</Link>
        <Link to="/categorypage">View by Category</Link>
        <Link to="/datepage">View by Date</Link>
        <Link to="/reportpage">User Category Report</Link>
        <Link to="/login">Log Out</Link>
      </div>
    </div>
  );
};

export default Navbar;

