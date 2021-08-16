import React, { Component } from 'react';
import { Button, Modal, Input, Form, Select } from 'antd';
import { message, Row, Col, Radio, Alert, Spin } from 'antd';
import { doQuestionAnswerEdit, doAddQuestionAnswerSingle } from '../reduxFlow/actions';

const { Option } = Select;
const { TextArea } = Input;

class QuestionAnswerEdit extends Component {

	state = {
			showModal: false,
			choice : '',
			changedAnswerChoice: undefined,
			loading: false
	}

	componentDidMount = () => {
	  const { form, rowdata } = this.props;
	  const { getFieldDecorator, setFieldsValue } = form;
    const { choiceOption1, choiceOption2, mcqOption1, mcqOption2, mcqOption3, mcqOption4, questionType, answer } = rowdata;
    const choice = Object.keys(rowdata)[Object.values(rowdata).indexOf(answer)];
    
    this.state.choice = choice;
    
    setFieldsValue({
      question: rowdata.question,
      optionA: questionType === 0 ? '' : questionType === 1 ? choiceOption1 : mcqOption1,
      optionB: questionType === 0 ? '' : questionType === 1 ? choiceOption2 : mcqOption2,
      optionC: questionType === 2 ? mcqOption3: '',
      optionD: questionType === 2 ? mcqOption4: '',
      answerDecl: questionType === 0 ? answer : '',
	  answerChoice: questionType === 1 || questionType === 2 ? choice: ''
    });
	}

	handleSubmit = async (e) => {
		e.preventDefault();
		let isValid = false;
		const { form } = this.props;
		form.validateFields((err) =>  isValid = !err);
		if(!isValid){
			return;
		}
		this.setState({ loading: true });
		await this.handleQuestionInsert().catch(this.handleError);
		this.setState({ loading: false });
	}

	handleQuestionInsert = async() => {
	  const { form, rowdata, tableload, close } = this.props;
		const { getFieldValue } = this.props.form;
		const { id, questionType, answer } = this.props.rowdata;
		const { changedAnswerChoice } = this.state; 
		let param = {
		    id,
		    questionType
		};
		param['question'] = getFieldValue('question').trim();
		
		switch(questionType){
		  case 0:
		    param['answer'] = getFieldValue('answerDecl').trim();
		    break;
		    
		  case 1:
		    param['choiceOption1'] = getFieldValue('optionA').trim();
		    param['choiceOption2'] = getFieldValue('optionB').trim();
		    param['answer'] = changedAnswerChoice !== undefined ? param[changedAnswerChoice].trim() : answer;
		    break;
		    
		  case 2:
		    param['mcqOption1'] = getFieldValue('optionA').trim();
		    param['mcqOption2'] = getFieldValue('optionB').trim();
		    param['mcqOption3'] = getFieldValue('optionC').trim();
		    param['mcqOption4'] = getFieldValue('optionD').trim();
		    param['answer'] = changedAnswerChoice !== undefined ? param[changedAnswerChoice].trim() : answer;
		    break;
		}
		
		console.log(param);
		
		if(param['id'] !== undefined) {
			await doQuestionAnswerEdit(param);
			message.success('Record changed successfully.');
		} else {
			param['questionBankName'] = rowdata.questionBank.questionBankName;
			param['category'] = rowdata.questionBank.testCategory.category;
			param['subCategory'] = rowdata.questionBank.testCategory.subCategory;
			await doAddQuestionAnswerSingle(param);
			message.success('Record added successfully.');
		}
		
		this.setState({ showModal: false });
		tableload();
		close();
	}

	handleError = (err) => {
		message.error(`${err.message}`);
		this.setState({ loading: false });
	}

	handleAnswerChoiceChange = (e) => {
	  const choice = e.target.value;
	  this.setState({ choice, changedAnswerChoice: choice });
	}
	
	render() {
		const { rowdata, open, close } = this.props;
		const { showModal , choice, loading } = this.state;
		const { getFieldDecorator, setFieldsValue } = this.props.form;
		
		const { questionType, questionBank, id } = rowdata;
		const { category, subCategory } = questionBank.testCategory;

		let title = '';
		let buttonValue = '';
		
		if(id !== undefined) {
			title = "Edit Question";
			buttonValue = "Update";
		} else {
			title = "Add Question Answer";
			buttonValue = "Insert";
		}
		
		return(
		    <div>
  		    <Modal title={title}
  		     width='60%'
  		     visible={open}
  		     onCancel={() => close()}
  		     footer={null}
  		     header={null} destroyOnClose={true} >
  		    <Spin spinning={loading} >
  		    
  		      <Alert message= {`${category} -> ${subCategory} -> ${questionBank.questionBankName}`} type="success" style={{ marginBottom: 10 }} />
    		    <Form>
      		    <Form.Item label={<b>Question</b>}>
        		    {getFieldDecorator('question', {
        		      rules:[{
        		        required: true,
        		        message: "Please enter a question"
        		      }]
        		    })(<TextArea placeholder="Question" autosize={{ minRows: 3, maxRows: 6 }} />)}
      		    </Form.Item>
      
      		    <Row>
        		    <Col span={12} >
          		    {(questionType === 1 || questionType === 2) && <Form.Item label="Option A">
          		    {getFieldDecorator('optionA', {
          		      rules:[{
          		        required: true,
          		        message: "Please enter a Option A"
          		      }]
          		    })(
          		        <Input placeholder="Option A" style={{ width: '99%' }} />
          		    )}
          		    </Form.Item>}
        		    </Col>
        		    <Col span={12} >
          		    {(questionType === 1 || questionType === 2) && <Form.Item label="Option B">
          		    {getFieldDecorator('optionB', {
          		      rules:[{
          		        required: true,
          		        message: "Please enter a Option B"
          		      }]
          		    })(
          		        <Input placeholder="Option B" />
          		    )}
          		    </Form.Item>}
        		    </Col>
      		    </Row>
      
      		    <Row>
        		    <Col span={12} >
          		    {questionType === 2 && <Form.Item label="Option C">
          		    {getFieldDecorator('optionC', {
          		      rules:[{
          		        required: true,
          		        message: "Please enter a Option C"
          		      }]
          		    })(
          		        <Input placeholder="Option C" style={{ width: '99%' }} />
          		    )}
          		    </Form.Item>}
        		    </Col>
        		    <Col span={12} >
          		    {questionType === 2 && <Form.Item label="Option D">
          		    {getFieldDecorator('optionD', {
          		      rules:[{
          		        required: true,
          		        message: "Please enter a Option D"
          		      }]
          		    })(
          		        <Input placeholder="Option D" />
          		    )}
          		    </Form.Item>}
        		    </Col>
      		    </Row>
      
      		    {(questionType === 1 || questionType === 2) && <Form.Item label={<b>Correct Answer</b>}>
        		    {getFieldDecorator('answerChoice', {
        		      rules:[{
        		        required: true,
        		        message: "Please enter an answer"
        		      }]
        		    })(
        		      <div>
        		        {(questionType === 2) && <Radio.Group value= {choice} buttonStyle="solid" onChange={this.handleAnswerChoiceChange}>
        		          <Radio.Button value="mcqOption1">Option A</Radio.Button>
        		          <Radio.Button value="mcqOption2">Option B</Radio.Button>
        		          <Radio.Button value="mcqOption3">Option C</Radio.Button>
        		          <Radio.Button value="mcqOption4">Option D</Radio.Button>
        		        </Radio.Group>}
        		        
        		        {(questionType === 1) && <Radio.Group value= {choice} buttonStyle="solid" onChange={this.handleAnswerChoiceChange}>
                      <Radio.Button value="choiceOption1">Option A</Radio.Button>
                      <Radio.Button value="choiceOption2">Option B</Radio.Button>
                    </Radio.Group>}
        		        
        		        {questionType === 0 && <Input placeholder="Answer" />}
        		     </div>
        		        )}
      		    </Form.Item>}
      
      		    {questionType === 0 && <Form.Item label= {<b>Correct Answer</b>}>
        		    {getFieldDecorator('answerDecl', {
        		      rules:[{
        		        required: true,
        		        message: "Please enter an answer"
        		      }]
        		    })(<Input placeholder="Answer" />)}
      		    </Form.Item>}
      		    <div style={{ width:100, margin: 'auto' }} >
      		      <Button type="primary" icon="save" style={{ backgroundColor: 'green' }} onClick={this.handleSubmit}>{buttonValue}</Button>
      		    </div>
    		    </Form>
    		    </Spin>
  		    </Modal>
		    </div>
		);
				}
	}

	const WrappedQuestionAnswerEdit = Form.create()(QuestionAnswerEdit);
	export default WrappedQuestionAnswerEdit;
