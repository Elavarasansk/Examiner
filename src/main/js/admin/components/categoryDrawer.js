import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Divider, Drawer, Form, Button, Select, Icon, Card, Table, Input, List } from 'antd';
import { getFilteredCategory } from '../reduxFlow/actions';
import AddCategory from './addCategory';
import EditCategory from './editCategory';

const { Option } = Select;

const styles={
    select:{
      width: 250,
      border: '2px solid',
      borderColor: '#080808',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5
    }
}

const { Search } = Input;

class CategoryDrawer extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        filter:{
          category: undefined,
          subCategory: undefined
        },
        openCategoryModal: false,
        editCategoryModal: false,
    }
  }
  
  componentDidMount(){
    const { filter } = this.state;
    const { dispatch } = this.props;
    getFilteredCategory(dispatch, filter);
  }
  
  handleFilterChange = (name, value) => {
    const { dispatch } = this.props;
    const { filter } = this.state;
    filter[name] = value;
    this.setState({ filter });
    getFilteredCategory(dispatch, filter);
  }
  
  getListCardData = () => {
    const { categoryTableRecord } = this.props;
    const categoryObj = categoryTableRecord.size>0? categoryTableRecord.toJS():{};
    
    let cardData=[];
    let i=1;
    Object.keys(categoryObj).map(data => cardData.push({ index:i++, category: data, subCategoryList: categoryObj[data] }));
    return cardData;
  }
  
  handleAddCategory = () => {
    this.setState({ openCategoryModal: true });
  }
  
  handleEditCategory = (editCategory) => {
    this.setState({ editCategoryModal: true, editCategory });
  }
  
  render() {
    const { open, categoryGroup } = this.props;
    const { filter, openCategoryModal, editCategoryModal, editCategory } = this.state;
    const { category, subCategory } = filter;
    const categoryObj = categoryGroup.size>0? categoryGroup.toJS():[];
    
    const listCardData = this.getListCardData();
    
    
    const testCategoryColumns = [{
      title:'Category',
      dataIndex:'category',
      key:'category',
      width:200,
      render: category => <span>
      <Button type="dashed" shape="circle" icon="edit" onClick={() => this.handleEditCategory(category)} size={'small'} style={{ color: 'black'}} />
      {category}</span>
    }, {
      title:'Sub-Category',
      dataIndex:'subCategoryList',
      key:'subCategoryList',
      render: data => <List
          size="small"
          header={null}
          footer={null}
          bordered
          dataSource={data}
          renderItem={item => <List.Item>{item}</List.Item>}
        />
    }];
    
    return (
      <Drawer
        title="Manage Category"
        width={720}
        onClose={() => this.props.close()}
        visible={open}
        maskClosable={false}
      >
        <Search
          placeholder="Category"
          value={category}
          style={{...styles.select }}
          onChange={e => this.handleFilterChange("category", e.target.value)}
          enterButton
        />
        
        <Search
          placeholder="Sub-Category"
          value={subCategory}
          style={{...styles.select }}
          onChange={e => this.handleFilterChange("subCategory", e.target.value)}
          enterButton
        />
        
        <Button type="primary" shape="circle" icon="plus" onClick={this.handleAddCategory}/>
        
        <Divider/>
      
        <Table 
          rowKey="category"
          columns={testCategoryColumns}
          dataSource={listCardData}
          size={"small"}
        />
        
        {openCategoryModal && <AddCategory open={openCategoryModal} close={() => {
          this.setState({ openCategoryModal: false });
          this.componentDidMount()}
        }/>}
        
        {editCategoryModal && <EditCategory
          category={editCategory}
          existingSubCategoryList={categoryObj[editCategory]}
          open={editCategoryModal} 
          close={() => {this.setState({ editCategoryModal: false }); this.componentDidMount()}}/>}
        
      </Drawer>
    );
  }
}


function mapStateToProps(state) {
  return {
    categoryGroup: state.get('admin').get('categoryGrouping'),
    categoryTableRecord: state.get('admin').get('categoryList'),
  };
}

const WrappedCategoryDrawer = Form.create()(CategoryDrawer);
export default withRouter(connect(mapStateToProps)(WrappedCategoryDrawer));