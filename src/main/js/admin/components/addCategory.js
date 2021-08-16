import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Modal, Button, Steps, Input, message as AntMessage, Alert, Spin } from 'antd';
import '../../common/styles/stepper.css';
import { checkAndThrowCategoryExists, addCategoryBatch } from '../reduxFlow/actions';

const { Step } = Steps;

const { confirm } = Modal;

class AddCategory extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        loading: false,
        currentStep: 0,
        newCategory:null,
        currentSubCategory:null,
        newSubCategoryList:[]
    }
  }
  
  next = () => {
    const { currentStep } = this.state; 
    this.setState({ currentStep: currentStep+1 });
  }
  
  handleStepperChange = (type) => {
    let { currentStep } = this.state;
    
    switch(type){
      case "next":
        if(this.checkCategoryExists()){
          AntMessage.error('Category already Exists');
          return;
        }
        currentStep++;
        break;
        
      case "prev":
        currentStep--;
        break;
    }
    this.setState({ currentStep });
  }
  
  
  checkCategoryExists = () => {
    const { newCategory } = this.state;
    const { categoryGroup } = this.props;
    const categoryObj = categoryGroup.size>0 ? categoryGroup.toJS():{};
    const index = Object.keys(categoryObj).findIndex(data => data===newCategory);
    if(index !== -1){
      return true;
    }
  }
  
  handleNewCategory = (e) => {
    this.setState({ newCategory: e.target.value.trim().toUpperCase() });
  }
  
  handleNewSubCategory = (e) => {
    const currentSubCategory = e.target.value.trim().toUpperCase();
    if(!currentSubCategory){
      return;
    }
    this.setState({ currentSubCategory });
  }
  
  handleAddClick = () => {
    const { currentSubCategory, newSubCategoryList } = this.state;
    
    if(!currentSubCategory){
      return;
    }
    
    newSubCategoryList.push(currentSubCategory);
    this.setState({ newSubCategoryList, currentSubCategory:null });    
  }
  
  handleDeleteSubCat = (index) => {
    const { newSubCategoryList } = this.state;
    newSubCategoryList.splice(index,1);
    this.setState({ newSubCategoryList });
  }
  
  handleCategorySave = async () => {
    this.saveCategoryAndSubCat().catch(this.handleError);
  }
  
  saveCategoryAndSubCat = async() => {
    const { dispatch, close } = this.props;
    const { newCategory, newSubCategoryList } = this.state;
    const categoryVo = { 
        category: newCategory,
        subCategoryList: newSubCategoryList
    };
    
    this.setState({ loading: true });
    await checkAndThrowCategoryExists(dispatch, newCategory);
    
    await addCategoryBatch(dispatch, categoryVo);
    AntMessage.success(`Category and sub-category has been added successfully`);
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
    const { open, close } = this.props;
    const { loading, currentStep, newCategory, currentSubCategory, newSubCategoryList } = this.state;
    
    const steps = [{
      title: 'Add Category',
      content: <Input placeholder="New Category" value={newCategory} style={{ width:250 }} onChange={this.handleNewCategory}/>,
    },
    {
      title: 'Add Sub-Category',
      content: <span>
        <Alert message= {`Category:: ${newCategory}`} type="success" style={{ marginBottom: 10 }} />
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
        title="Category Configuration"
        visible={open}
        onOk={this.handleOk}
        onCancel={this.closeConfirm}
        footer={null}
      >
      <Spin spinning={loading}>
        <Steps current={currentStep}>
          {steps.map(item => (
            <Step key={item.title} title={item.title} />
          ))}
        </Steps>
        <div className= {currentStep===0? "category-step-content": "sub_cat-steps-content"} >
          {steps[currentStep].content}
        </div>
        <div style={{ margin:'auto', width:(currentStep===0)? 100: 200, padding: 10 }} >
          {(currentStep===1) && <Button onClick={() => this.handleStepperChange('prev')}>
            Prev
          </Button>}
          {(currentStep===0) && <Button onClick={() => this.handleStepperChange('next')} disabled={!newCategory || newCategory === ''} >
            Next
          </Button>}
          {(currentStep===1) && <Button type="primary" onClick={this.handleCategorySave} disabled={newSubCategoryList.length === 0} style={{ backgroundColor:'green' }}>
            SAVE
          </Button>}
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