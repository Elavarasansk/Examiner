import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Spin, Table, Radio, message as AntMessage, Select, Divider, Input, Tag, Button, Breadcrumb, Alert } from 'antd';
import { getCategoryGroups, getQuestionBankGroups, getQuestionAnswers } from '../reduxFlow/actions';
import CategoryDrawer from '../components/categoryDrawer';
import QuestionBankDrawer from '../components/questionBankDrawer';
import QuestionAnswerEdit from '../components/questionAnswerEdit';

const styles={
    select:{
      width: 200,
      border: '2px solid',
      borderColor: 'royalblue',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5
    },
    button:{
      backgroundColor:'green',
      marginTop: 10,
      marginRight: 10  
    }
}

const qaColumnDesc = [{
  title: 'Question',
  dataIndex: 'question',
  key: 'question'
},{
  title: 'Answer',
  dataIndex: 'answer',
  key: 'answer',
  render: text => <Tag color="green" >{text}</Tag>
}];

const qaColumnEitherOr = [{
  title: 'Option A',
  dataIndex: 'choiceOption1',
  key: 'choiceOption1'
},{
  title: 'Option B',
  dataIndex: 'choiceOption2',
  key: 'choiceOption2'
}];

const qaColumnMc = [{
  title: 'Option A',
  dataIndex: 'mcqOption1',
  key: 'mcqOption1'
},{
  title: 'Option B',
  dataIndex: 'mcqOption2',
  key: 'mcqOption2'
},{
  title: 'Option C',
  dataIndex: 'mcqOption3',
  key: 'mcqOption3'
},{
  title: 'Option D',
  dataIndex: 'mcqOption4',
  key: 'mcqOption4'
}];

const { Option } = Select;
const { Search } = Input;

class AdminDetailsContainer extends Component {

  constructor(props){
    super(props);
    this.state = {
        loading: false,
        pageNum: 0,
        pageSize: 10,
        openCategoryDrawer: false,
        openQbDrawer: false,
        filter:{
          questionType:2
        },
        openEdit:false,
        addSingleQA: false
    }
  }

  componentDidMount(){
    const { dispatch } = this.props;
    getCategoryGroups(dispatch);
    getQuestionBankGroups(dispatch);
    this.loadQuestionanswerList().catch(this.handleError);
  }
  
  loadQuestionanswerList = async (page) => {
    this.setState({ loading: true });
    const { dispatch } = this.props;
    const { filter, pageNum, pageSize } = this.state;
    const currPage = (page !== undefined)? page: pageNum;
    
    await getQuestionAnswers(dispatch, filter, currPage , pageSize);
    this.setState({ loading: false });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  getColumnType = () => {
    const { questionType } = this.state.filter;
    
    let qaColumn = []; 
    
    const actions = [{
      title:'Actions',
      width:150,
      render: (row) => <Button type="primary" icon="edit" onClick={() => this.setState({ openEdit: true, row })}/>
    }]
    
    switch(questionType){
      case 0:
        qaColumn = qaColumnDesc;
        break;
      case 1:
        qaColumn = qaColumnDesc.slice(0,1).concat(qaColumnEitherOr).concat(qaColumnDesc.slice(1,2));
        break;
      case 2:
        qaColumn = qaColumnDesc.slice(0,1).concat(qaColumnMc).concat(qaColumnDesc.slice(1,2));
        break;
    }
    qaColumn = qaColumn.concat(actions);
    
    return qaColumn;
  }
  
  handleQuesTypeChange = (event) => {
    const questionType = event.target.value;
    const { filter } = this.state;
    filter.questionType = questionType;
    this.setState({ filter });
    this.loadQuestionanswerList().catch(this.handleError);
  }
  
  handleFilterChange = (type, name, value) => {
    const { filter } = this.state;
    const { category, subCategory, questionBankName } = this.state.filter;

    switch(type){
      case "select":
        filter[name] = value;
        break;
      case "text":
        filter[name] = value;
        break;
    }

    if(!value){
      switch(name){
        case "category":
          filter.subCategory = value;
          filter.questionBankName = value;
          break;
        case "subCategory":
          filter.questionBankName = value;
          break;
      }
    } else {
      
      const { categoryGroup, quesBankGroup, quesAnsList } = this.props;
      const categoryObj = categoryGroup.size>0? categoryGroup.toJS():[];
      const quesBankObj = quesBankGroup.size>0? quesBankGroup.toJS():null;

      switch(name){
        case "category":
          if(subCategory && categoryObj[value] && categoryObj[value].indexOf(subCategory) === -1){
            filter.subCategory = undefined;
            filter.questionBankName = undefined;
          };
          break;
        case "subCategory":
          if(quesBankObj && category && quesBankObj[`${category}-${value}`] &&
              quesBankObj[`${category}-${value}`].indexOf(questionBankName) === -1){
            filter.questionBankName = undefined;
          };
          break;
      }
    }
    this.setState({ filter });
    this.loadQuestionanswerList().catch(this.handleError);
  }
  
  handleCategoryAdd = () => {
    this.setState({ openCategoryDrawer: true });
  }
  
  handlePageChange = (page, pageSize) => {
    const pageNum = page-1;
    this.setState({ pageNum });
    this.loadQuestionanswerList(pageNum).catch(this.handleError);
  }
  
  handleQbAdd = () => {
    this.setState({ openQbDrawer: true });
  }
  
  handleSingleQAAdd = (text) => {
	  const { filter } = this.state;
	  let singleQBData = {
		questionBank: {
		  questionBankName: filter.questionBankName,
		  testCategory: {
			category: filter.category,
			subCategory: filter.subCategory
		  }
		},
		questionType: text
	  }
	  this.setState({ addSingleQA: true, singleQBData });
  }
  
  render() {
    const { loading, openCategoryDrawer, openQbDrawer, filter, pageNum,pageSize, openEdit, row, addSingleQA, singleQBData } = this.state;
    const { categoryGroup, quesBankGroup, quesAnsList } = this.props;
    const { category, subCategory, questionBankName, question, questionType} = filter;
    
    const categoryObj = categoryGroup.size>0? categoryGroup.toJS():[];
    const quesBankObj = quesBankGroup.size>0? quesBankGroup.toJS():null;
    
    const qaColumn = this.getColumnType();
    
    const qbFilter = (quesBankObj && category && subCategory)? quesBankObj[`${category}-${subCategory}`]:[]
    
    const paginationProps={
        pageSize: pageSize,
        onChange:this.handlePageChange,
        total: quesAnsList.size>0 ? quesAnsList.get('totalElements'):0
    }
    
    return(<Spin spinning={loading}>
    
        <Button type="primary" icon="tool" size={'default'} style={styles.button} onClick={this.handleCategoryAdd}>Manage Category</Button>
        <Button type="primary" icon="switcher" size={'default'} style={styles.button} onClick={this.handleQbAdd}>Manage QuestionBank</Button>
    
        {openCategoryDrawer && <CategoryDrawer open= {openCategoryDrawer} close={() => this.setState({ openCategoryDrawer: false })} />}
        {openQbDrawer && <QuestionBankDrawer open= {openQbDrawer} close={() => this.setState({ openQbDrawer: false })} />}
        
        <Divider />
        
        <Select
          allowClear
          showSearch
          placeholder="Category"
          optionFilterProp="children"
          value={category}
          style={styles.select}
          onChange={value => this.handleFilterChange("select", "category", value)}
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
        >
          {Object.keys(categoryObj).map(data => <Option value= {data} key={data} >{data}</Option>)}
        </Select>
        
        <Select
          allowClear
          showSearch
          placeholder="Sub-Category"
          optionFilterProp="children"
          value={category? subCategory: undefined }
          style={styles.select}
          onChange={value => this.handleFilterChange("select", "subCategory", value)}
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
        >
          {categoryObj[category] && categoryObj[category].map(data => <Option value= {data} key={data} >{data}</Option>)}
        </Select>
        
        <Select
          allowClear
          showSearch
          placeholder="Question Bank"
          optionFilterProp="children"
          value={subCategory? questionBankName: undefined }
          style={styles.select}
          onChange={value => this.handleFilterChange("select", "questionBankName", value)}
          filterOption={(input, option) =>
            option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
          }
        >
          {qbFilter && qbFilter.map(data => <Option value={data} key={data} >{data}</Option>)}
        </Select>
        
        <Radio.Group defaultValue= {questionType} onChange={this.handleQuesTypeChange} style={{...styles.select, width:350, border:'3px solid', padding:5 }} >
          <Radio value= {2} >Multi-Choice</Radio>
          <Radio value= {1} >Either-Or</Radio>
          {/*  <Radio value= {0} >Descriptive</Radio>*/}
        </Radio.Group> 
        
        <Search
          placeholder="Question Name"
          style={{...styles.select, width: 400 }}
          onChange={e => this.handleFilterChange("text", "question", e.target.value)}
          enterButton
        />
        
        {(filter['category'] !== undefined && filter['subCategory'] !== undefined && filter['questionBankName'] !== undefined) 
        	&&  <Select allowClear showSearch placeholder="Add Question Answer" optionFilterProp="children"
            	style={styles.select} onChange={this.handleSingleQAAdd} >
            		<Option value={0} >Descriptive</Option>
            		<Option value={1} >Either-Or</Option>
            		<Option value={2} >Multi-Choice</Option>
          		</Select>}
      
        <Table
          rowKey="uid"
          style={{ backgroundColor:'floralwhite' }}
          columns={qaColumn}
          dataSource={quesAnsList.size>0 ? quesAnsList.get('content').toJS():[]}
          bordered
          size={"middle"}
          pagination={paginationProps}
        />
        {openEdit && <QuestionAnswerEdit rowdata={row} open= {openEdit} close={() => this.setState({ openEdit: false })} tableload={this.loadQuestionanswerList} />}
        {addSingleQA && <QuestionAnswerEdit rowdata={singleQBData} open= {addSingleQA} close={() => this.setState({ addSingleQA: false })} tableload={this.loadQuestionanswerList} />}
    </Spin>);
  }
}


function mapStateToProps(state) {
  return {
    categoryGroup: state.get('admin').get('categoryGrouping'),
    quesBankGroup: state.get('admin').get('quesBankGrouping'),
    quesAnsList: state.get('admin').get('questionAnswer')
  };
}

export default withRouter(connect(mapStateToProps)(AdminDetailsContainer));
