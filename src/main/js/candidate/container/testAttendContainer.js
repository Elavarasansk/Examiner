import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

//import Countdown from 'react-countdown-now';
import ReactCountdownClock from 'react-countdown-clock';

import { Checkbox, Button, Row, Col,Card, Alert, Icon, Divider} from 'antd';
import { Modal, message as AntMessage, Breadcrumb, Carousel,Spin } from 'antd';

import EitherOrQa from '../components/eitherOrQa';
import DescriptiveQa from '../components/descriptiveQa';
import MultipleChoiceQa from '../components/multipleChoiceQa';

import { getAssignedTest, getMyQuestions, submitTestAnswers } from '../reduxFlow/actions';
import '../../common/styles/carousel.css';
import '../../common/styles/dashboard.css';

const styles={
  candidate: {
    color: 'whitesmoke',
    top: 20,
    position:'relative'
  },
  testCategory: {
    marginTop:30,
    marginRight:10
  }
}

const { confirm } = Modal;
let confirmUpdate = {};

class TestAttendContainer extends Component {
  constructor(props){
    super(props);
    this.state = {
        current:'behaviour',
        currentQuestion: 1,
        questionCount: 25,
        loading : false
    }
  }
  
  componentDidMount() {
    this.fetchUserAssignedData();
  }
  
  fetchUserAssignedData =async (e) => {
    this.setState({ loading: true });
    const { dispatch } = this.props;
    await getAssignedTest(dispatch);
    this.setState({ loading: false });
    
    this.fetchQuestionsAssignedToMe();
  }
  
  fetchQuestionsAssignedToMe = async (e) => {
    const { id } = this.props.questionBankAssigned.toJS();
    
    this.setState({ loading: true });
    const { dispatch } = this.props;
    await getMyQuestions(id, dispatch);
    this.setState({ loading: false });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  getQusNumComp = () => {
    const { questionsCount } = this.props.questionBankAssigned.toJS();
    const qusArray=[];
    for(let i=1;i <= questionsCount;i++){
      qusArray.push(<Button shape="circle" key= {i} style={{ margin:10 }}
      type={this.getButtonType(i)} onClick={() => this.handleQuestionNumChange(i)}>{i}</Button>);
    }
    return qusArray;
  }
  
  getButtonType = (buttonNum) => {
    const { currentQuestion } = this.state;
    return currentQuestion===buttonNum ? "primary": "dashed";
  }
  
  handleQuestionNumChange = (currentQuestion)=> {
    this.setState({ currentQuestion });
  }
  
  handlePrevNextChange = (type)=> {
    let { currentQuestion } = this.state;
    const myQuestionList = this.props.questionsAssigned.toJS();
    
    switch (type){
      case "previous":
        if(currentQuestion > 1){
          currentQuestion--;
        }
        break;
        
      case "next":
        if(currentQuestion < myQuestionList.length){
          currentQuestion++;
        }
        break;
    }
    this.setState({ currentQuestion });
  }
  
  getQbDetails = () => {
    const { questionBank } = this.props.questionBankAssigned.toJS();
    const { questionBankName, testCategory } = questionBank;
    const { category, subCategory} = testCategory;
    return {category, subCategory, questionBankName }
  }
  
  loadComponentByQusType = () => {
    const qaComp = [];
    const { currentQuestion } = this.state;
    const myQuestionList = this.props.questionsAssigned.toJS();
    
    let i=1;
    myQuestionList.forEach(qa => {
      const { id, questionType } = qa;

      switch (questionType) {
        case 0:
          qaComp.push(<div key={id} style={{ display: (currentQuestion===i)? 'block': 'none' }}><DescriptiveQa qnsNum={i} qaDetails={qa}/></div>);
          break;
        case 1:
          qaComp.push(<div key={id} style={{ display: (currentQuestion===i)? 'block': 'none' }}><EitherOrQa qnsNum={i} qaDetails={qa}/></div>);
          break;
        case 2:
          qaComp.push(<div key={id} style={{ display: (currentQuestion===i)? 'block': 'none' }}><MultipleChoiceQa qnsNum={i} qaDetails={qa}/></div>);
          break;
      }
      i++;
    });
    
    return qaComp;
  }
  
  getCountdownTimer = () => {
    const { expirationTime } = this.props.questionBankAssigned.toJS();

    let expTimeStamp = new Date(expirationTime);
    expTimeStamp = expTimeStamp.getTime();
    let currTime = new Date();
    currTime = currTime.getTime();
    return this.diffMinutes(expTimeStamp, currTime);
  }
  
  diffMinutes = (dt2, dt1) => {
    const diff =(dt2 - dt1) / 1000;
    return Math.abs(Math.round(diff));
  }
  
  handleTestSubmit = () => {
    const { dispatch , questionBankAssigned } = this.props;
    
    confirmUpdate =  confirm({
      title: 'Are you sure want to submit the test ?',
      content: 'Once submitted cannot re-attend',
      okText: 'Submit',
      okType: 'danger',
      cancelText: 'No',
      onOk(){
    	confirmUpdate.update({ cancelButtonProps : {disabled : true } });
        submitTestAnswers(questionBankAssigned.get('id'), dispatch);
        confirmUpdate.update({ cancelButtonProps : {disabled : false } });
      },
    });
  }
  
  autoSubmitTest = () => {
    const { dispatch , questionBankAssigned } = this.props;
    this.setState({  loading : true  });
    submitTestAnswers(questionBankAssigned.get('id'), dispatch);
    this.setState({  loading : false  });
  }
  
  render() {
    const { current, currentQuestion, mcQaData, eitherOrqaData,loading } = this.state;
    const quesNumComp = this.getQusNumComp();
    const { category, subCategory, questionBankName } = this.getQbDetails();
    
    const balanceTime = this.getCountdownTimer();
    
    const qaComp = this.loadComponentByQusType();
    
    return(
        	<Spin spinning = { loading } >
      <Row>
        <Col span={16} >
          <Alert message= {
              <Breadcrumb>
                <Breadcrumb.Item>{category}</Breadcrumb.Item>
                <Breadcrumb.Item>{subCategory}</Breadcrumb.Item>
                <Breadcrumb.Item>{questionBankName}</Breadcrumb.Item>
              </Breadcrumb>
          } type="info" style={{ margin: 5 }}/>
          
            {false && <Carousel>{qaComp}</Carousel>}
            {qaComp}
            <div style={{ margin:'auto', width:300, padding:10 }} >
              <Button.Group size={'default'} >
                <Button type="primary" disabled={currentQuestion === 1} onClick={() => this.handlePrevNextChange('previous')}>
                  <Icon type="left" />
                  Previous
                </Button>
                <Button type="primary" disabled={currentQuestion === qaComp.length} onClick={() => this.handlePrevNextChange('next')}>
                  Next
                  <Icon type="right" />
                </Button>
              </Button.Group>
            </div>
            <Divider/>
            <div style={{ margin:'auto', width:350 }} >
              <Button type="primary" onClick={this.handleTestSubmit} style={{ backgroundColor:'green', margin:'auto', width:300, }}>
                <Icon type="check-circle" />
                Submit & Complete Test
              </Button>
            </div>
        </Col>
        <Col span={8}>
          <Card title={<Alert message="Will Expire in" type="error"/>}>
            <div style={{ marginLeft:65 }}>
             <ReactCountdownClock seconds={balanceTime}
                color="brown"
                alpha={0.8}
                size={200}
                timeFormat="hms"
                onComplete={() => this.autoSubmitTest()} />
            </div>
          </Card>
          <Card >
             {quesNumComp}
          </Card>
        </Col>
      </Row>
      </Spin>
    );
  }
}

          /*{false && <Countdown date={Date.now() + 10000} />}*/
          
function mapStateToProps(state) {
  return {
    questionBankAssigned: state.get('candidate').get('myTests'),
    questionsAssigned: state.get('candidate').get('myQuestions'),
  };
}

export default withRouter(connect(mapStateToProps)(TestAttendContainer));