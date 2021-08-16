import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Card, Radio, Divider, Tag } from 'antd';
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

class EitherOrQa extends Component {
	
	
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
    answerMyQuestion(resultId, selectedAnswer, dispatch);
    this.setState({ selectedAnswer });
  };
  
  render() {
    const { selectedAnswer } = this.state;
    const { qaDetails, qnsNum } = this.props;
    const { question, choiceOption1, choiceOption2 } = qaDetails;
    
    return(
      <Card style={{ height:245 }}>
         <Tag>Qns {qnsNum} : </Tag><b>{question}</b>
        <Divider />
        <Radio.Group onChange={this.handleOptionSelection} value={selectedAnswer} style={{ margin: 20 }}>
          <Radio style={styles.radioStyle} value={choiceOption1}>{choiceOption1}</Radio>
          <Radio style={styles.radioStyle} value={choiceOption2}>{choiceOption2}</Radio>
        </Radio.Group>
      </Card>
    );
  }
}

function mapStateToProps(state) {
  return {
    
  };
}

export default withRouter(connect(mapStateToProps)(EitherOrQa));
