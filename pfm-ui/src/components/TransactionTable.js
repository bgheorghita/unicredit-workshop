import React, { useEffect, useState } from 'react';
import { Table, Button, Space, Modal } from 'antd';
import CategoriesDropDown from './CategoriesDropDown';
import { Tag } from "antd";

const TransactionTable = () => {
  const [transactions, setTransactions] = useState([]);
  const [selectedTxId, setSelectedTxId] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);

  useEffect(() => {
    fetchTransactions();
  }, []);

  const fetchTransactions = async () => {
    try {
      const response = await fetch('http://localhost:8081/transactions');
      const data = await response.json();
      setTransactions(data);
    } catch (error) {
      console.error('Error fetching transactions:', error);
    }
  };

  const handleView = (record) => {
    console.log('View transaction:', record);
  };

  const handleDelete = (record) => {
    console.log('Delete transaction:', record);
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
      colSpan: 3,
      render: (_, record) => (
        <Space>
          <Button onClick={() => handleView(record)}>View</Button>
          <Button onClick={() => handleDelete(record)} danger>Delete</Button>
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
