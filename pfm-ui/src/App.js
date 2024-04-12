import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import TransactionTable from './components/TransactionTable';
import './App.css';
import ScoringComponent from './components/ScoringComponent';
import ReportComponent from './components/ReportComponent';

function App() {
  return (
    <Router>
      <div>
        <nav className="navbar">
          <ul className="navbar-nav">
            <li className="nav-item"><Link to="/transactions" className="nav-link">Transactions</Link></li>
            <li className="nav-item"><Link to="/scoring" className="nav-link">Scoring</Link></li>
            <li className="nav-item"><Link to="/report" className="nav-link">Report</Link></li>
          </ul>
        </nav>
        
        <Routes>
          <Route path="/" element={<TransactionsPage />} /> 
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/scoring" element={<ScoringComponent />} />
          <Route path="/report" element={<ReportComponent />} />
        </Routes>
      </div>
    </Router>
  );
}

const TransactionsPage = () => (
  <div style={{ textAlign: 'center' }}>
    <h1 style={{ margin: '20px 0' }}>Transactions</h1>
    <TransactionTable />
  </div>
);


export default App;
