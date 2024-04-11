import React, { useEffect, useState } from 'react';
import { Select } from 'antd';

const { Option } = Select;
const baseUrl = 'http://localhost:8081';
const categoriesRequestPath = 'categories';
const transactionsRequestPath = 'transactions';

const CategoriesDropDown = ({ txId, onUpdateCategory }) => {
  const [categories, setCategories] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);

  useEffect(() => {
    fetchCategories();
    fetchTransactionDetails();
  }, [txId]);

  const fetchCategories = async () => {
    try {
      const response = await fetch(baseUrl + '/' + categoriesRequestPath);
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  const fetchTransactionDetails = async () => {
    try {
      const response = await fetch(`${baseUrl}/${transactionsRequestPath}/${txId}`);
      const data = await response.json();
      setSelectedCategory(data.category);
    } catch (error) {
      console.error('Error fetching transaction details:', error);
    }
  };

  const handleCategoryChange = async (value) => {
    try {
      const response = await fetch(`${baseUrl}/${transactionsRequestPath}/${txId}/${value}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        onUpdateCategory(value);
        setSelectedCategory(value);
        console.error('Failed to update category');
      }
    } catch (error) {
      console.error('Error updating category:', error);
    }
  };

  return (
    <Select
      showSearch
      placeholder="Select category"
      optionFilterProp="children"
      style={{ width: '200px' }}
      value={selectedCategory}
      onChange={handleCategoryChange}
    >
      {categories.map((category) => (
        <Option key={category.id} value={category.id}>
          {category.value}
        </Option>
      ))}
    </Select>
  );
};

export default CategoriesDropDown;
