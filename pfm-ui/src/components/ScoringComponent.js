import React, { useState } from 'react';
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

  const handleDateRangeChange = (dates) => {
    setDateRange(dates);
  };

  const fetchTransactions = async () => {
    try {
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
    await computeScoring();
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h1>Scoring</h1>
      <RangePicker onChange={handleDateRangeChange} />
      <Button type="primary" onClick={handleCalculateScoring} style={{ marginTop: '16px' }}>Calculate Scoring</Button>
      {scoringResult && (
        <div style={{ marginTop: '24px' }}>
          <h2>Result:</h2>
          <p>Total Spent: {scoringResult.totalSpent}</p>
          <p>Total Received: {scoringResult.totalReceived}</p>
        </div>
      )}
    </div>
  );
};

export default DateRangeScoringPage;
