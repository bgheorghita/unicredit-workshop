import React, {useEffect, useState} from 'react';
import { DownOutlined, SmileOutlined } from '@ant-design/icons';
import {Dropdown, Select, Space} from 'antd';
const CategoriesDropDown = () => {
    const [categories, setCategories] = useState([]);
    useEffect(() => {
        fetchCategories();
    }, []);

    const fetchCategories = async () => {
        try {
            const response = await fetch('http://localhost:8080/categories');
            const data = await response.json();
            setCategories(data);
        } catch (error) {
            console.error('Error fetching transactions:', error);
        }
    };

    return(
        <Select
            showSearch
            placeholder="Select a person"
            optionFilterProp="children"
            // onChange={onChange}
            // onSearch={onSearch}
            // filterOption={filterOption}
            options={
                categories.map((category) => ({
                    key: category.id,
                    label: category.value,
                }))
            }
        />
    );
};
export default CategoriesDropDown;

