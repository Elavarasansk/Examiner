import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { handleQbSelectedList } from '../reduxFlow/actions';
import { List , Modal , Button } from 'antd';

class QuestionBankSelectedList extends Component {
  
  constructor(props){
    super(props);
    this.state = {
    }
  }

  handleOk = e => {
    const { close } = this.props;
    close();
  };

  handleCancel = e => {
    const { close } = this.props;
    close();
  };

  render(){
    const { selectedList , open, visible } = this.props;
    return(
        <Modal
        title= "Question Bank Selected List"
        visible={open}
        width = {500}
        onOk={this.handleOk}
        onCancel={this.handleCancel}
        footer={[
          <Button key="submit" type="primary" onClick={this.handleOk}>
            Ok
          </Button>
        ]}
        >
        <List
          size="small"
          bordered
          style={{ border:'1px solid'}}
          dataSource={selectedList}
          renderItem={item => <List.Item><h3><b>{item}</b></h3></List.Item>}
        />
        </Modal>
        )
      }
    }

function mapStateToProps(state) {
  return {
    selectedList : state.get('admin').toJS().handleQbSelectedList,
  };
}

export default withRouter(connect(mapStateToProps)(QuestionBankSelectedList));