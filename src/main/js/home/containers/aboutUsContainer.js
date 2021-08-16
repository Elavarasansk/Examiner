import React, { Component } from 'react';
import { Row } from 'antd';
import '../../common/styles/dashboard.css';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import TestFeaturesMenu from '../components/testFeaturesMenu';
import CategoryInfoForHome from '../components/categoryInfoForHome';

const styles={
	    greenButton:{
	      color:'green', border:'green'
	    }
	}

class AboutUsContainer extends Component {
	render() {
		return(<div  style={{ background: 'transparent', padding: 20 }} >
	    
        <Row style={{ margin:30 }}>
            <div className="centered" style={{ marginLeft: '15%' , width: '70%' }}>
                <div className="top-title">Our Goal is to Hire the Best</div>
                <div className="title">We are partnered with highly efficient developers/publishers around the world to bring you the most efficient library of tests available.</div>
                <div className="text">Our tests are guaranteed to provide you more information about your job candidates than you would have known by an interview alone.
                <br/>Assuming that the test given was related to the job, if your applicant performed well on a test but, after hiring, could not perform the same tasks covered by the test, then we will refund the cost of that test(s).
                <br/>We are happy to assist you with 24/7 support round the clock.We help all the way to hire the best which is our ultimate goal.</div>
            </div>
        </Row>
        
        <CategoryInfoForHome />
        
     </div>);
	}
}

function mapStateToProps(state) {
  return {
  };
}

export default withRouter(connect(mapStateToProps)(AboutUsContainer));
