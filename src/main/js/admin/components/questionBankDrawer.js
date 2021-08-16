import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Divider, Drawer, Form, Button, Select, Icon, Card, Table, Input, List } from 'antd';
import { getFilteredQuestionBankList } from '../reduxFlow/actions';
import AddQuestionBank from './addQuestionBank';
import QuestionAnswerEdit from './questionAnswerEdit';

const { Option } = Select;

const styles={
    select:{
      width: 200,
      border: '2px solid',
      borderColor: '#080808',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5
    }
}

const { Search } = Input;

class QuestionBankDrawer extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        loading:false,
        filter: {
          category: undefined,
          subCategory: undefined,
          questionBankName: undefined
        },
        addSingleQA: false
    }
  }
  
  componentDidMount(){
    const { filter } = this.state;
    const { dispatch } = this.props;
    getFilteredQuestionBankList(dispatch, filter);
  }
  
  handleFilterChange = (name, value) => {
    const { dispatch } = this.props;
    const { filter } = this.state;
    filter[name] = value;
    this.setState({ filter });
    getFilteredQuestionBankList(dispatch, filter);
  }
  
  handleAddQuestionBank = () => {
    this.setState({ openQbModal: true })
  }
  
  handleQuesTypeChange = (text, row) => {
	    const questionType = event.target.value;
	    const { filter } = this.state;
	    filter.questionType = questionType;
	    this.setState({ filter });
	    this.loadQuestionanswerList().catch(this.handleError);
  }
  
  handleSingleQAAdd = (text, row) => {
	  let singleQBData = {
		questionBank: {
		  questionBankName: row.questionBankName,
		  testCategory: row.testCategory
		},
		questionType: text
	  }
	  this.setState({ addSingleQA: true, singleQBData });
  }
  
  loadQuestionanswerList = async (page) => {
	    this.setState({ loading: true });
	    const { dispatch } = this.props;
	    const { filter, pageNum, pageSize } = this.state;
	    const currPage = (page !== undefined)? page: pageNum;
	    
	    await getQuestionAnswers(dispatch, filter, currPage , pageSize);
	    this.setState({ loading: false });
	  }
  
  render() {
    const { open, close, filteredQbList } = this.props;
    const { filter, openQbModal, addSingleQA, singleQBData } = this.state;
    const { category, subCategory, questionBankName } = filter;
    
    const categoryClassifyColumns = [{
      title:'Question Bank Name',
      dataIndex:'questionBankName',
      key:'questionBankName',
    }, {
      title:'Category',
      dataIndex:'testCategory.category',
      key:'testCategory.category',
    }, {
      title:'Sub Category',
      dataIndex:'testCategory.subCategory',
      key:'testCategory.subCategory',
    }, {
        title:'Add Question Answer',
        dataIndex:'questionType',
        key:'questionType',
        render: (text, row) => {
        	return(<div>
        	<Select allowClear showSearch placeholder="Add Question Answer" optionFilterProp="children" onChange={(text) => this.handleSingleQAAdd(text, row)} 
        		style={{ width: 200 }}>
            	{/*	<Option value={0} >Descriptive</Option> */}
            		<Option value={1} >Either-Or</Option>
            		<Option value={2} >Multi-Choice</Option>
          	</Select>
          	</div>);
        }
    }];
    
    return (
      <Drawer
        title="Manage Question-Bank"
        width={720}
        onClose={() => close()}
        visible={open}
        maskClosable={false}
      >
        <Search
          placeholder="Question Bank"
          value={questionBankName}
          style={{...styles.select }}
          onChange={e => this.handleFilterChange("questionBankName", e.target.value)}
        />
      
        <Search
          placeholder="Category"
          value={category}
          style={{...styles.select }}
          onChange={e => this.handleFilterChange("category", e.target.value)}
        />
        
        <Search
          placeholder="Sub-Category"
          value={subCategory}
          style={{...styles.select }}
          onChange={e => this.handleFilterChange("subCategory", e.target.value)}
        />
        
        <Button type="primary" shape="circle" icon="plus" onClick={this.handleAddQuestionBank}/>
        
        <Divider/>
      
        <Table 
          rowKey="questionBankName"
          columns={categoryClassifyColumns}
          dataSource={filteredQbList.size>0 ? filteredQbList.toJS():[]}
          size={"small"}
        />
        
        {openQbModal && <AddQuestionBank open={openQbModal} close={() => {
          this.setState({ openQbModal: false });
          this.componentDidMount();}
        }/>}
        		
        <div
          style={{
            position: 'absolute',
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e8e8e8',
            padding: '10px 16px',
            textAlign: 'right',
            left: 0,
            background: '#fff',
            borderRadius: '0 0 4px 4px',
          }}
        >
          <Button type="danger" style={{ marginRight: 8 }} onClick={() => close()} >CLOSE</Button>
          
          {addSingleQA && <QuestionAnswerEdit rowdata={singleQBData} open= {addSingleQA} close={() => this.setState({ addSingleQA: false })} tableload={this.loadQuestionanswerList} />}
        
        </div>		
      </Drawer>
    );
  }
}


function mapStateToProps(state) {
  return {
    filteredQbList: state.get('admin').get('filteredQuestionBankList'),
  };
}

const WrappedQuestionBankDrawer = Form.create()(QuestionBankDrawer);
export default withRouter(connect(mapStateToProps)(WrappedQuestionBankDrawer));