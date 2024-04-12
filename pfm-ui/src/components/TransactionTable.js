import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal } from 'antd';
import CategoriesDropDown from './CategoriesDropDown';
import { Tag } from "antd";

const PFM_CRUD_BASE_URL = 'http://localhost:8081';
const PFM_CRUD_TRANSACTIONS_REQUEST_PATH = 'transactions';

const PFM_CLASSIFIER_BASE_URL = 'http://localhost:8080';
const PFM_CLASSIFIER_AUTOMATIC_REQUEST_PATH = 'auto-classifier';

const TransactionTable = () => {
  const [transactions, setTransactions] = useState([]);
  const [selectedTxId, setSelectedTxId] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [errorModalVisible, setErrorModalVisible] = useState(false);
  const [successModalVisible, setSuccessModalVisible] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await fetch(PFM_CRUD_BASE_URL + '/' + PFM_CRUD_TRANSACTIONS_REQUEST_PATH);
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

  const handleManualCategoryChange = (txId) => {
    setSelectedTxId(txId);
    setIsModalVisible(true);
  };

  const handleAutomaticCategoryChange = async (txId, txDescription) => {
    try {
      const response = await fetch(`${PFM_CLASSIFIER_BASE_URL}/${PFM_CLASSIFIER_AUTOMATIC_REQUEST_PATH}?txId=${txId}&description=${encodeURIComponent(txDescription)}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        fetchTransactions();
        setSuccessMessage('Category successfully classified');
        setSuccessModalVisible(true);
      } else {
        throw new Error('Failed to update category');
      }
    } catch (error) {
      setErrorMessage('Failed to update category');
      setErrorModalVisible(true);
    }
  };

  const handleModalOk = () => {
    setIsModalVisible(false);
    fetchTransactions();
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
  };

  const getRandomColor = () => {
    const colors = ['magenta', 'red', 'volcano', 'orange', 'gold', 'lime', 'green', 'cyan', 'blue', 'geekblue', 'purple'];
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
      colSpan: 3,
      render: (_, record) => (
        <Space>
          <Button onClick={() => handleView(record)}>View</Button>
          <Button onClick={() => handleManualCategoryChange(record.id)}>Manual Classification</Button>
          <Button onClick={() => handleAutomaticCategoryChange(record.id, record.description)}>Automatic Classification</Button>
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
      <Modal
        title="Error"
        visible={errorModalVisible}
        onCancel={() => setErrorModalVisible(false)}
        footer={null}
      >
        <p>{errorMessage}</p>
      </Modal>
      <Modal
        title="Success"
        visible={successModalVisible}
        onCancel={() => setSuccessModalVisible(false)}
        footer={null}
      >
        <p>{successMessage}</p>
      </Modal>
    </>
  );
};

export default TransactionTable;
