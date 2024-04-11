import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import TransactionTable from './components/TransactionTable';
import './App.css';

function Home() {
  return (
    <div>Home</div>
  );
}

function App() {
  return (
    <Router>
      <div>
        <nav>
          <ul>
            <li><Link to="/">Home</Link></li>
            <li><Link to="/transactions">Transactions</Link></li>
          </ul>
        </nav>
        
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/transactions" element={<TransactionsPage />} />
        </Routes>
      </div>
    </Router>
  );
}

const TransactionsPage = () => (
  <div>
    <h1>Transactions</h1>
    <TransactionTable />
  </div>
);

export default App;
