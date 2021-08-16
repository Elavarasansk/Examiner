import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Modal, Button, Steps, Input, message as AntMessage, Alert, Spin, Popover } from 'antd';
import '../../common/styles/stepper.css';
import { addCategoryBatch } from '../reduxFlow/actions';

const { Step } = Steps;

const { confirm } = Modal;

class AddCategory extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        loading: false,
        currentSubCategory:null,
        newSubCategoryList:[]
    }
  }
  
  checkSubCategoryExists = () => {
    const { currentSubCategory } = this.state;
    const { existingSubCategoryList } = this.props;
    
    const index = existingSubCategoryList.findIndex(data => data===currentSubCategory);
    if(index !== -1){
      return true;
    }
  }
  
  handleNewSubCategory = (e) => {
    const currentSubCategory = e.target.value.trim().toUpperCase();
    this.setState({ currentSubCategory });
  }
  
  handleAddClick = () => {
    const { currentSubCategory, newSubCategoryList } = this.state;
    if(!currentSubCategory) {
      return;
    }
    if(this.checkSubCategoryExists()){
      AntMessage.error(`SubCategory - {currentSubCategory} already Exists`);
      return;
    }
    newSubCategoryList.push(currentSubCategory);
    this.setState({ newSubCategoryList, currentSubCategory:null });    
  }
  
  handleCategoryUpdate = async () => {
    this.editCategoryAndSubCat().catch(this.handleError);
  }
  
  editCategoryAndSubCat = async() => {
    const { dispatch, close, category } = this.props;
    const { newSubCategoryList } = this.state;
    const categoryVo = {
        category,
        subCategoryList: newSubCategoryList
    };
    
    this.setState({ loading: true });
    await addCategoryBatch(dispatch, categoryVo);
    AntMessage.success(`sub-category has been updated successfully for category - ${category}`);
    this.setState({ loading: false });
    close();
  }

  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  closeConfirm = () => {
    const { close } = this.props;
    confirm({
      title: 'Do you want to close without adding the Category?',
      content: 'Unsaved data will be lost',
      onOk() {
        close();
      },
      onCancel() {
      },
    });
  }
  
  render() {
    const { open, close, category, existingSubCategoryList } = this.props;
    const { loading, currentStep, newCategory, currentSubCategory, newSubCategoryList } = this.state;
    
    const steps = [{
      title: 'New Sub-Category',
      content: <span>
        <Alert message= {
          <Popover content={existingSubCategoryList.map((val,i) => <div>{++i}. {val}</div>)} title="Existing SubCategory" trigger="hover">
            <Button size={'small'} icon={"ordered-list"}>Existing</Button>
          </Popover>
        } type="success" style={{ marginBottom: 10 }} />
        {<span style={{ marginTop: 10 }}>
          <Input placeholder="New Sub-Category" value={currentSubCategory} style={{ width:250 }} onChange={this.handleNewSubCategory}/>
          <Button type="primary" icon="plus" style={{ marginLeft: 2 }} onClick= {this.handleAddClick}/>
          {newSubCategoryList.map((data, i) => <span style={{ marginTop: 5 }} >
            <Input placeholder="New Sub-Category" value={data} style={{ width:250 }} disabled />
            <Button type="dashed" icon="minus" style={{ marginLeft: 2 }} onClick= {() => this.handleDeleteSubCat(i)}/></span>
          )}
        </span>}
      </span>,
    }];
    
    return (
      <Modal
        title={`Add Sub-Category for - ${category}`}
        visible={open}
        onOk={this.handleOk}
        onCancel={this.closeConfirm}
        footer={null}
      >
        <Spin spinning={loading}>
          <Steps current={0}>
            {steps.map(item => (
              <Step key={item.title} title={item.title} />
            ))}
          </Steps>
          <div className= "sub_cat-steps-content">
            {steps[0].content}
          </div>
          <div style={{ margin:'auto', width: 100, padding: 10 }} >
            <Button type="primary" onClick={this.handleCategoryUpdate} disabled={newSubCategoryList.length === 0} style={{ backgroundColor: 'green' }}>
              UPDATE
            </Button>
          </div>
        </Spin>
      </Modal>
    );
  }
}

function mapStateToProps(state) {
  return {
    categoryGroup: state.get('admin').get('categoryGrouping'),
  };
}

export default withRouter(connect(mapStateToProps)(AddCategory));