import React, { Component } from 'react';
import '../../common/styles/carousel.css';
import '../../common/styles/dashboard.css';
import { Row, Col, Card, Button, Carousel, Menu, Icon } from 'antd';

class TestFeaturesMenu extends Component {
	
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
		
		return(
				<div>
	    	       <h3 className={"test-category"} style={{marginTop: 30 }} >
	    		        <Menu onClick={this.handleCategoryMenuClick} selectedKeys={[current]} mode="horizontal" theme="dark">
	    			        <Menu.Item key="behaviour"><Icon type="mail" />Behavioural Test</Menu.Item>
	    			        <Menu.Item key="computer"><Icon type="mail" />Basic Computer Skill Test</Menu.Item>
	    			        <Menu.Item key="programming"><Icon type="mail" />Programming Skill Test</Menu.Item>
	    			        <Menu.Item key="mechanical"><Icon type="mail" />Mechanical / Manufacturing</Menu.Item>
	    			     </Menu>
	    			    
	    			     {current==='behaviour' && <div className="text-details">
	    	            <p><br/>Candidates past behaviour is used to predict the future behaviour and <br/> can help determine
	    	            the level of compatibility between candidate and company. <br/> We have the following tests  to assist the employees behavior</p>
	    	         </div>}
	    			     
	    			     {current==='computer' && <div className="text-details">
	    			     <p><br/>Candidates basic computer literacy is measured <br/> which allows predicting candidates job readlines as well as overall job performance.
	    			     <br/> We have the following test under Basic Computer Test</p>
	      			     <ul>
	        			     <li>Microsoft Excel-2016 Test (Intermediate)</li>
	        			     <li>Microsoft Word-2016 Test (Intermediate)</li>
	        			     <li>Fundamentals of Computer Test</li>
	        			   </ul>
	    			     </div>}
	    			     
	    			     {current==='programming' && <div className="text-details">
	    			     <p><br/>Our interactive programming skill tests show you exactly what they know or don't know. <br/> Each computer skills assessment is a simulation of a computer program.
	    			     <br/>Each computer skills assessment has approximately 20 question. <br/>The job applicant will be asked to answer questions of high level industry standards from <br/>  </p>
	      			     <ul>
	      			       <li>Computer languages</li>
	                   <li>Libraries</li>
	                   <li>Frameworks of latest technologies</li>
	                 </ul>
	    			     </div>}
	    			     
	    			     {current==='mechanical' && <div className="text-details">
	    			     <p><br/>Measures candidates mechanical skills such as managing,designing,supervising 
	    			     <br/>along with all necessary knowledge to operate effectively in complex industrial environment.
	    			     <br/> We have the following test under Mechanical/Manufacturing.</p>
	      			     <ul>
	                   <li>ForkLift Operator Test</li>
	                   <li>Welding Operator Test</li>
	                   <li>Sanitary Specialists Test</li>
	                 </ul>
	    			     </div>}
	    		     </h3>
	  	      </div>
		);
	}
}

export default TestFeaturesMenu;