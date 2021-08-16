import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Row, Col, Card, Button, Carousel, Menu, Icon } from 'antd';
import '../../common/styles/carousel.css';
import '../../common/styles/dashboard.css';
import TestFeaturesMenu from '../components/testFeaturesMenu';
import AmazonComponent from '../components/amazonComponent';
import CategoryInfoForHome from '../components/categoryInfoForHome'; 

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

const { Meta } = Card;

class HomeInfoContainer extends Component {
	
	
	constructor(props){
	  super(props);
		this.state = {
				current:'behaviour'
		}
	}
	
	
	handleCategoryMenuClick = (e) => {
		this.setState({ current: e.key });
	}
  
  render() {
    
    const { current } = this.state;
    
    return(<div  style={{ background: 'transparent', padding: 20 }} >
        <Carousel effect="fade" autoplay={true} dots={true}>
          <div>
            <h3 className={"candidates"} >
              <div className="candidate-text" >
                <h1 style={styles.candidate}>Picking up the Right fit for the Job ??</h1>
                <p><br/>Here we got for the Master prophecy,<br/>Predicting too much on how the talents can be best fitted.</p>
              </div>
            </h3>
          </div>
          <div>
	          <h3 className={"exam"} >
	            <div className="candidate-text">
	              <h1 style={styles.candidate}>Looking for a Right Candidate ??</h1>
	              <p><br/>We Help You Hire Right Candidate</p>
	            </div>
	          </h3>
          </div>
        </Carousel>
        <br/>
        <Card title={<h1 className="top-title" >Why Vexamine ??</h1>} bordered={false}>
          <h2 >Fixing the Right People on your Team ??</h2>
          <p className="vexamine-Detail" >Vexamine employee assessment determines the quality, performance and
             cultural fit of each candidate. Different categories of questions are structured in the manner
             to predict candidates future performance and passion towards job success, capturing right talents
             with high hiring efficiency at affordable cost with reflection of low in overall organizational performance.</p>
        </Card>
        <CategoryInfoForHome />
        <AmazonComponent />
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
  };
}

export default withRouter(connect(mapStateToProps)(HomeInfoContainer));
