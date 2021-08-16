import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Checkbox, Button, Row, Col,Card, Alert, Icon, message as AntMessage  } from 'antd';
import '../../common/styles/carousel.css';
import '../../common/styles/dashboard.css';
import TestAgreement from '../components/testAgreement';
import Countdown from 'react-countdown-now';
import { getAssignedTest, expireMyTest } from '../reduxFlow/actions';
import TestAttendContainer from './testAttendContainer';

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

class CandidateContainer extends Component {
	
	constructor(props){
	  super(props);
		this.state = {
				current:'behaviour',
		}
	}
	
	componentDidMount() {
	  this.fetchUserAssignedData();
	}
	
	fetchUserAssignedData = async (e) => {
	  this.setState({ loading: true });
	  const { dispatch } = this.props;
	  await getAssignedTest(dispatch);
	}
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
	
	handleCategoryMenuClick = (e) => {
		this.setState({ current: e.key });
	}
	
	getQusComp = () => {
	  const { questionCount } = this.state;
	  const qusArray=[];
	  for(let i=1;i <= questionCount;i++){
	    qusArray.push(<Button shape="circle" type={"dashed"} style={{ margin:10 }} >{i}</Button>);
	  }
	  return qusArray;
	}
	
	validateExpiryAndStartTest = () => {
	  const { dispatch } = this.props;
	  const { id, expirationTime, questionsCount } = this.props.questionBankAssigned.toJS();

	  const currentDate = new Date();
	  const expiryDate = new Date(expirationTime);

	  const currTimeStamp = currentDate.getTime();
	  const expiryTimeStamp = expiryDate.getTime();

	  const isThereEnoughTime = (currTimeStamp < expiryTimeStamp);
	  
	  if(!isThereEnoughTime && expirationTime){
	    expireMyTest(id, dispatch);
	  }
	  return isThereEnoughTime;
	}
	
	
	
	getComponentByAssignment = () => {
	  const { questionBankAssigned } = this.props;
	  const { id, testStartTime, testEndTime, expired, } = questionBankAssigned.toJS();
	  const isTestStarted = !!testStartTime;

	  if(!questionBankAssigned.size > 0){
	    return <span/>;
	  }

	  if(id === null){
	    return <Alert message="No Tests Assigned For You" type="error" />;
	  }

	  if(expired){
	    return <Alert message="Test Expired" type="error" />;
	  }

	  const isThereEnoughTime = this.validateExpiryAndStartTest();

	  if(!testEndTime){
	    if(isThereEnoughTime){
	      if(isTestStarted) {
	        return <TestAttendContainer />
	      } else {
	        return <TestAgreement />
	      }
	    }
	  } else {
	    return  <Alert message={`You Have Successfully Completed your tests.`} type="success" />;
	  }
	}
  
  render() {
    const { current, mcQaData, eitherOrqaData } = this.state;
    const { questionBankAssigned } = this.props;
    
    return(<span>
        {this.getComponentByAssignment()}
      </span>
    );
  }
}
          /*{false && <Countdown date={Date.now() + 10000} />}*/
          
function mapStateToProps(state) {
  return {
    questionBankAssigned: state.get('candidate').get('myTests')
  };
}
export default withRouter(connect(mapStateToProps)(CandidateContainer));
