import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Checkbox, Button, message as AntMessage, Spin } from 'antd';
import { startTest } from '../reduxFlow/actions';
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

class TestAgreement extends Component {
	
	
	constructor(props){
	  super(props);
		this.state = {
				agreeLicense: false,
				loading: false
		}
	}
	
	handleAgreementCheck = (e) => {
	  this.setState({ agreeLicense: e.target.checked });
	}

	handleToProceedTest =async (e) => {
    this.setState({ loading: true });
    const { dispatch , questionBankAssigned } = this.props;
    await startTest(questionBankAssigned.get('id'), dispatch);
    this.setState({ loading: false });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
	
  render() {
    const { current, agreeLicense, loading } = this.state;
    
    return(<span>
    	<Spin spinning={loading} >
        <div  style={{ background: '#ECECEC', padding: 50, width:500, textAlign:'justify', margin: 'auto' }} >
          <ul>
             <li><p>Simplify your data collection efforts and internal workflows with these 1900+ web
             form templates that blend in smoothly on your website. Customize any of the form examples that
             you find by using the 123FormBuilder platform. No coding required.</p></li>
        
             <li><p>Simplify your data collection efforts and internal workflows with these 1900+ web
             form templates that blend in smoothly on your website. Customize any of the form examples that
             you find by using the 123FormBuilder platform. No coding required.</p></li>
             
             <li><p>Simplify your data collection efforts and internal workflows with these 1900+ web
             form templates that blend in smoothly on your website. Customize any of the form examples that
             you find by using the 123FormBuilder platform. No coding required.</p></li>
          </ul>
          <Checkbox checked={agreeLicense} onChange={this.handleAgreementCheck} >
            I have read the <a href="">agreement</a> Accept & Continue.
          </Checkbox>
        </div>
        <div style={{ margin: 'auto', width: 85, marginTop: 5 }}>
          <Button type="primary" htmlType="submit" disabled={!agreeLicense} onClick={()=>this.handleToProceedTest().catch(this.handleError)}>Attend Test</Button>
        </div>
        </Spin>
      </span>
    );
  }
}

function mapStateToProps(state) {
  return {
    questionBankAssigned: state.get('candidate').get('myTests')
  };
}

export default withRouter(connect(mapStateToProps)(TestAgreement));
