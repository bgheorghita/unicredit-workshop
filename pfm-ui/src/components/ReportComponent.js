import React, { useState, useEffect } from 'react';
import { Button } from 'antd';
import { Tree as AntdTree } from 'antd';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const { TreeNode } = AntdTree;

const PFM_CRUD_BASE_URL = 'http://localhost:8081';
const PFM_CRUD_TRANSACTIONS_REQUEST_PATH = 'transactions';

const PFM_REPORT_BASE_URL = 'http://localhost:8082';
const PFM_REPORT_REQUEST_PATH = 'report';

const ReportComponent = () => {
  const [transactions, setTransactions] = useState([]);
  const [reportData, setReportData] = useState(null);

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await fetch(`${PFM_CRUD_BASE_URL}/${PFM_CRUD_TRANSACTIONS_REQUEST_PATH}`);
      const data = await response.json();
      setTransactions(data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const computeReport = async () => {
    try {
      const response = await fetch(`${PFM_REPORT_BASE_URL}/${PFM_REPORT_REQUEST_PATH}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(transactions),
      });
      const data = await response.json();
      setReportData(data);
    } catch (error) {
      console.error('Error computing report:', error);
    }
  };

  const renderTreeNodes = (data) =>
    data.map((item, index) => (
      <TreeNode
        title={`${item.categoryName} (${item.percentage}%)`}
        key={`${item.categoryName}-${index}`}
      >
        {item.subcategories && item.subcategories.length > 0 && renderTreeNodes(item.subcategories)}
      </TreeNode>
    ));

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h1 style={{ marginBottom: '20px' }}>Report</h1>
      <Button type="primary" onClick={computeReport}>Compute Report</Button>
      {reportData && (
        <div style={{ marginTop: '20px', width: '80%', height: '400px' }}>
          <ResponsiveContainer>
            <BarChart
              data={reportData.entries}
              margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="categoryName" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="percentage" fill="#8884d8" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}
      <div style={{ marginTop: '20px', width: '80%' }}>
        {reportData && (
          <AntdTree defaultExpandAll>
            {renderTreeNodes(reportData.entries)}
          </AntdTree>
        )}
      </div>
    </div>
  );
};

export default ReportComponent;
