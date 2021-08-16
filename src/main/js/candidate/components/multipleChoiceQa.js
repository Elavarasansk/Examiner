import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Card, Radio, Tag, Divider } from 'antd';
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

class MultipleChoiceQa extends Component {
	
	
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

//	componentDidUpdate(prevProps, prevState) {
//	}
	
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
    const { question, mcqOption1, mcqOption2, mcqOption3, mcqOption4 } = qaDetails;
    
    return(
      <Card style={{ height:245  }}>
        <Tag>Qns {qnsNum} : </Tag><b>{question}</b>
        <Divider />
        <Radio.Group onChange={this.handleOptionSelection} value={selectedAnswer}>
          <Radio style={styles.radioStyle} value={mcqOption1}>{mcqOption1}</Radio>
          <Radio style={styles.radioStyle} value={mcqOption2}>{mcqOption2}</Radio>
          <Radio style={styles.radioStyle} value={mcqOption3}>{mcqOption3}</Radio>
          <Radio style={styles.radioStyle} value={mcqOption4}>{mcqOption4}</Radio>
        </Radio.Group>
      </Card>
    );
  }
}

function mapStateToProps(state) {
  return {

  };
}

export default withRouter(connect(mapStateToProps)(MultipleChoiceQa));
