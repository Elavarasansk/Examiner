import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import {  Descriptions,Row,Tag,Modal } from 'antd';
const DATE_FORMAT = 'YYYY-MM-DD, hh:mm:ss A';
import moment from 'moment';


class VerifyTestAssignment extends Component {
  constructor(props) {
    super(props);
  }
  
  render() {
   const { param } = this.props;
   const { questionBankName,assigneeList,testDate,questionsCount,allowedTimeTaken,inviteSent,questionBankPath } = param;
    return (<div>
    <Row style={{ marginTop : 16 }} >
    <Descriptions  bordered>
    <Descriptions.Item  span={6} label="Question Bank" ><Tag color="blue">{questionBankPath}</Tag></Descriptions.Item>
    <Descriptions.Item  span={6} label="Candidate" >{assigneeList}</Descriptions.Item>
    <Descriptions.Item span={6}  label="Test Start DateTime" >{moment(testDate[0]).format(DATE_FORMAT) }</Descriptions.Item>
    <Descriptions.Item span={6} label="Test End DateTime" >{moment(testDate[1]).format(DATE_FORMAT) }</Descriptions.Item>
    <Descriptions.Item span={6} label="Questions Count" >{questionsCount}</Descriptions.Item>
    <Descriptions.Item  span={6} label="Total Time" >{moment(allowedTimeTaken).format('HH:mm:ss')}</Descriptions.Item>
    <Descriptions.Item  span={6} label="Mail Sent" >{!inviteSent ? <Tag color="red">NO</Tag> : <Tag color="#87d068">YES</Tag>}</Descriptions.Item>
    </Descriptions>
  </Row> 
  </div>);
  }
}
export default withRouter(connect(null)(VerifyTestAssignment));
