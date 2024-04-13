import React, { useState, useEffect } from 'react';
import { DatePicker, Button } from 'antd';

const { RangePicker } = DatePicker;

const PFM_CRUD_BASE_URL = 'http://localhost:8081';
const PFM_CRUD_TRANSACTIONS_REQUEST_PATH = 'transactions';

const PFM_REPORT_BASE_URL = 'http://localhost:8082';
const PFM_REPORT_SCORING_REQUEST_PATH = 'scoring';

const DateRangeScoringPage = () => {
  const [dateRange, setDateRange] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [scoringResult, setScoringResult] = useState(null);

  useEffect(() => {
    if (transactions.length > 0) {
      computeScoring();
    }
  }, [transactions]);

  const handleDateRangeChange = (dates) => {
    console.log('Selected date range:', dates);
    setDateRange(dates);
    setTransactions([]);
    setScoringResult(null);
  };

  const fetchTransactions = async () => {
    try {
      if (!dateRange || dateRange.length === 0) {
        console.error('Date range is not set.');
        return;
      }
      const startDate = dateRange[0].toISOString();
      const endDate = dateRange[1].toISOString();
      const url = `${PFM_CRUD_BASE_URL}/${PFM_CRUD_TRANSACTIONS_REQUEST_PATH}/date?start=${encodeURIComponent(startDate)}&end=${encodeURIComponent(endDate)}`;
      const response = await fetch(url);
      const data = await response.json();
      setTransactions(data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const computeScoring = async () => {
    const transactionAmounts = transactions.map((transaction) => transaction.amount);
    try {
      const response = await fetch(`${PFM_REPORT_BASE_URL}/${PFM_REPORT_SCORING_REQUEST_PATH}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(transactionAmounts),
      });
      const data = await response.json();
      setScoringResult(data);
    } catch (error) {
      console.error('Error computing scoring:', error);
    }
  };

  const handleCalculateScoring = async () => {
    await fetchTransactions();
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h1>Scoring</h1>
      <RangePicker onChange={handleDateRangeChange} />
      <Button type="primary" onClick={handleCalculateScoring} style={{ marginTop: '16px' }}>Calculate Scoring</Button>
      {transactions.length === 0 ? (
        <p>No transactions found for the selected date range.</p>
      ) : (
        <div style={{ marginTop: '24px', maxWidth: '600px' }}>
          <h2>Transactions:</h2>
          <table style={{ width: '100%', borderCollapse: 'collapse' }}>
            <thead>
              <tr>
                <th>Date</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction, index) => (
                <tr key={index}>
                  <td>{transaction.date}</td>
                  <td>${transaction.amount.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {scoringResult && (
        <div style={{ marginTop: '24px' }}>
          <h2>Result:</h2>
          <p>Total Spent: {scoringResult.totalSpent}</p>
          <p>Total Received: {scoringResult.totalReceived}</p>
        </div>
      )}
    </div>
  );
  
  
}

export default DateRangeScoringPage;
