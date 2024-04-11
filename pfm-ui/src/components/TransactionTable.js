import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal } from 'antd';
import CategoriesDropDown from './CategoriesDropDown';
import { Tag } from "antd";

const baseUrl = 'http://localhost:8081';
const transactionsRequestPath = 'transactions';

const TransactionTable = () => {
  const [transactions, setTransactions] = useState([]);
  const [selectedTxId, setSelectedTxId] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await fetch(baseUrl + '/' + transactionsRequestPath);
      const data = await response.json();
      setTransactions(data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const handleView = (record) => {
    Modal.info({
      title: 'Transaction Details',
      content: (
        <div>
          <h3>Selected Transaction</h3>
          <p>ID: {record.id}</p>
          <p>Description: {record.description}</p>
          <p>Date: {record.date}</p>
          <p>Amount: {record.amount}</p>
          <p>Category: {record.category ? record.category.value : 'Uncategorized'}</p>
  
          {record.parent ? (
            <>
              <h3>Parent Transaction</h3>
              <p>ID: {record.parent.id}</p>
              <p>Description: {record.parent.description}</p>
              <p>Date: {record.parent.date}</p>
              <p>Amount: {record.parent.amount}</p>
              <p>Category: {record.parent.category ? record.parent.category.value : 'Uncategorized'}</p>
            </>
          ) : (
            <p>No parent transaction found.</p>
          )}
        </div>
      ),
      onOk() {},
    });
  };

  const handleCategoryChange = (txId) => {
    setSelectedTxId(txId);
    setIsModalVisible(true);
  };

  const handleModalOk = () => {
    setIsModalVisible(false);
    fetchTransactions();
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  const getRandomColor = () => {
    const colors = ['blue', 'green', 'red', 'orange', 'purple', 'cyan', 'magenta'];
    const randomIndex = Math.floor(Math.random() * colors.length);
    return colors[randomIndex];
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
      render: (category) => (
        <Tag color={getRandomColor()}>{category ? category.value : 'Uncategorized'}</Tag>
      ),
    },
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'Amount',
      dataIndex: 'amount',
      key: 'amount',
    },
    {
      title: 'Actions',
      key: 'actions',
      colSpan: 2,
      render: (_, record) => (
        <Space>
          <Button onClick={() => handleView(record)}>View</Button>
          <Button onClick={() => handleCategoryChange(record.id)}>Change Category</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Table dataSource={transactions} columns={columns} />
      <Modal
        title="Change Category"
        visible={isModalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
      >
        <CategoriesDropDown txId={selectedTxId} onUpdateCategory={handleModalOk} />
      </Modal>
    </>
  );
};

export default TransactionTable;
