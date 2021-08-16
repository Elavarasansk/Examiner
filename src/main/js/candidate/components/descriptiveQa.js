import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Card, Radio, Divider, Tag, Input } from 'antd';
import { answerMyQuestion } from '../reduxFlow/actions';
import '../../common/styles/carousel.css';
import '../../common/styles/dashboard.css';

const styles={
    radioStyle: {
      display: 'block',
      height: '30px',
      lineHeight: '30px',
    }
}

const { TextArea } = Input;

class DescriptiveQa extends Component {
	
	
	constructor(props){
	  super(props);
		this.state = {
		    selectedAnswer: null
		}
	}
	
	static getDerivedStateFromProps(nextProps, prevState){
    const { selectedAnswer } = prevState;
    if(!selectedAnswer){
      return { selectedAnswer: nextProps.qaDetails.selectedAnswer };
    } else{
      return null;
    }
  }
	
	handleOptionSelection = (e) => {
	  const selectedAnswer = e.target.value;
    const { qaDetails, dispatch } = this.props;
    const { resultId } = qaDetails; 
    this.setState({ selectedAnswer });
  };
  
  render() {
    const { selectedAnswer } = this.state;
    const { qaDetails, qnsNum, dispatch } = this.props;
    const { resultId, question, choiceOption1, choiceOption2 } = qaDetails;
    
    return(
      <Card style={{ height:245 }} >
         <Tag>Qns {qnsNum} : </Tag><b>{question}</b>
        <Divider />
        <ul>
          <TextArea placeholder="Enter Your answer" 
            value={selectedAnswer} 
            onBlur= {() => answerMyQuestion(resultId, selectedAnswer, dispatch)} 
            autosize={{ minRows: 5, maxRows: 5 }} 
            onChange={this.handleOptionSelection} />
        </ul>
      </Card>
    );
  }
}

function mapStateToProps(state) {
  return {
    
  };
}

export default withRouter(connect(mapStateToProps)(DescriptiveQa));
