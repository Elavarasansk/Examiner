import React, { Component } from 'react';
import  PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Checkbox, Button, Modal , message as AntMessage, Spin } from 'antd';
import { startTest } from '../../reduxFlow/actions';
import '../../../common/styles/carousel.css';
import '../../../common/styles/dashboard.css';

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
    const { dispatch , testId, close, proceedTest } = this.props;
    await startTest(testId, dispatch);
    close();
    proceedTest();
    this.setState({ loading: false });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  closeConfirm = () => {
    const { close } = this.props;
    confirm({
      title: 'Do you want to close Agreement?',
      content: 'You may not be able to proceed test.',
      onOk() {
        close();
      },
      onCancel() {
      },
    });
  }
	
  render() {
    const { current, agreeLicense, loading } = this.state;
    const { open } = this.props;
    
    return(
        <Modal
          title="Vexamine License Agreement"
          visible={open}
          onOk={this.handleOk}
          onCancel={this.closeConfirm}
          footer={null}
        >
        <Spin spinning={loading} >
        <div  style={{ background: '#ECECEC', padding: 50, textAlign:'justify', margin: 'auto' }} >
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
      </Modal>
    );
  }
}

function mapStateToProps(state) {
  return {
  };
}

export default withRouter(connect(mapStateToProps)(TestAgreement));
