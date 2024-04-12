import React, { useEffect, useState } from 'react';
import { Button, Modal, Select } from 'antd';

const { Option } = Select;
const PFM_CRUD_BASE_URL = 'http://localhost:8081';
const PFM_CRUD_TRANSACTIONS_REQUEST_PATH = 'transactions';
const PFM_CRUD_CATEGORIES_REQUEST_PATH = 'categories';

const PFM_CLASSIFIER_BASE_URL = 'http://localhost:8080';
const PFM_CLASSIFIER_MANUAL_REQUEST_PATH = 'manual-classifier';

const CategoriesDropDown = ({ txId, onUpdateCategory }) => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);

  const [errorModalVisible, setErrorModalVisible] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const handleModalClose = () => {
    setErrorModalVisible(false);
  };  

  useEffect(() => {
    fetchCategories();
    fetchTransactionDetails();
  }, [txId]);

  const fetchCategories = async () => {
    try {
      const response = await fetch(PFM_CRUD_BASE_URL + '/' + PFM_CRUD_CATEGORIES_REQUEST_PATH);
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchTransactionDetails = async () => {
    try {
      const response = await fetch(`${PFM_CRUD_BASE_URL}/${PFM_CRUD_TRANSACTIONS_REQUEST_PATH}/${txId}`);
      const data = await response.json();
      setSelectedCategory(data.category);
    } catch (error) {
      console.error('Error fetching transaction details:', error);
    }
  };

  const handleManualCategoryChange = async (categoryId) => {
    try {
      const response = await fetch(`${PFM_CLASSIFIER_BASE_URL}/${PFM_CLASSIFIER_MANUAL_REQUEST_PATH}?txId=${txId}&categoryId=${categoryId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        onUpdateCategory(categoryId);
        setSelectedCategory(categoryId);
      } else {
        throw new Error('Failed to update category');
      }
    } catch (error) {
      setErrorMessage('Failed to update category');
      setErrorModalVisible(true);
    }
  };  

  return (
    <>
      <Select
        showSearch
        placeholder="Select category"
        optionFilterProp="children"
        style={{ width: '200px' }}
        value={selectedCategory}
        onChange={handleManualCategoryChange}
      >
        {categories.map((category) => (
          <Option key={category.id} value={category.id}>
            {category.value}
          </Option>
        ))}
      </Select>
      <Modal
        title="Error"
        visible={errorModalVisible}
        onCancel={handleModalClose}
        footer={[
          <Button key="ok" onClick={handleModalClose}>
            OK
          </Button>,
        ]}
      >
        <p>{errorMessage}</p>
      </Modal>
    </>
  );
};

export default CategoriesDropDown;
