import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { getCategoryForHome } from '../reduxFlow/actions';
import { Row, Col, Card, Menu, Spin } from 'antd';
import '../../common/styles/carousel.css';

class CategoryInfoForHome extends Component {
	
	state = {
		data: undefined,
		current:'',
		isloading: false
	}
	
	componentDidMount = async() => {
		const { dispatch } = this.props;
		this.setState({ isloading: true});
		const data = await getCategoryForHome(dispatch);
		this.setState({ isloading: false, data, current: Object.keys(JSON.parse(data)).length > 0 ? Object.keys(JSON.parse(data))[0] : ''  });
	}
	
	handleCategoryMenuClick = (e) => {
		this.setState({ current: e.key });
	}
	
	render() {
		const { data, current, isloading } = this.state;
		let cardData = data !== undefined ? JSON.parse(data) : {};
		
		return(
		<Spin spinning={isloading} >
		  <div>
		  <Row gutter={16} style={{ marginTop: 15 }} >
	      <Col>
	      
	      <h3 className={"test-category"} style={{marginTop: 30 }} >
	      	{Object.keys(cardData).length > 0 && <Menu onClick={this.handleCategoryMenuClick} selectedKeys={[current]} mode="horizontal" theme="dark">
	      	{Object.keys(cardData).map(rec => 
	      		<Menu.Item key={rec} bordered={false}>{rec}</Menu.Item>)}
	      	</Menu>}
	      
	      	<div className="text-details" >
	      		{cardData[current] !== undefined && cardData[current].map(val => <p style={{marginLeft: 50 }} >{val}</p> )}
	      		{cardData[current] !== undefined && <p style={{marginLeft: 50 }} >and much more..</p>}
	      	</div>
	      </h3>
	      
	     
	      </Col>
	      </Row>
		  </div>
		</Spin>
		);
	}
}

function mapStateToProps(state) {
	return{
	};
}

export default withRouter(connect(mapStateToProps)(CategoryInfoForHome));
