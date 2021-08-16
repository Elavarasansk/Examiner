import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Button, Table, Modal, Checkbox ,  message as AntMessage} from 'antd';
import { getQuestionBankDetails , updateQbModelView , handleQbSelectedList} from '../reduxFlow/actions';
import '../../common/styles/questionbank.css'; 

class ManagerDrawerContainer extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        selectedList:[] 
    }
  }
  
  componentDidMount(){ 
    const { dispatch } = this.props;  
    this.props.onRef(this);
    getQuestionBankDetails(dispatch).catch(this.handleError);
  };
  
  componentWillUnmount(){
    this.props.onRef(undefined);
  }

  handleOk = async() => {
    const { selectedList } = this.state;
    const { dispatch } = this.props;  
    updateQbModelView( dispatch , false );
    await handleQbSelectedList( dispatch , selectedList );
    this.props.questionBankCount();
  };

  handleCancel = async() => {
    const { selectedList } = this.state;
    const { dispatch } = this.props;  
    updateQbModelView( dispatch , false );
    await handleQbSelectedList( dispatch , selectedList );
    this.props.questionBankCount();
  }; 
  
  handleSelectedList = () => {
    const { dispatch } = this.props;  
    handleQbSelectedList( dispatch , false );
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }

  
  render(){
    const { allQuestionBankDetails , modalQbView } = this.props;
    const { selectedList } = this.state ;
    const rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {
          this.setState( { selectedList : selectedRowKeys });
        },
        selectedRowKeys : selectedList,
    };
    
    const questionDrawerColumns = [
      {
        title: 'Question Bank Name',
        dataIndex: 'questionBankName',
        key:'questionBankName'
      }
    ];
    
    return(
        <div>
        <Modal
        title="Question Bank Details"
        width = {500}
        centered
        visible={modalQbView}
        onOk={this.handleOk}
        onCancel={this.handleCancel}
        >
        <Table 
        id={"questionbank"}
        bordered
        style={{ border:'0px solid', backgroundColor:'white'}}
        rowSelection={rowSelection} 
        rowKey={record => record.questionBankName} 
        columns={questionDrawerColumns} 
        dataSource={allQuestionBankDetails}
        />
        </Modal>
        </div>
    )
  }
}

function mapStateToProps(state) {
  return {
    allQuestionBankDetails : state.get('admin').toJS().getQuestionBankDetails,
    modalQbView : state.get('admin').get('updateQbModelView'),
  };
}

export default withRouter(connect(mapStateToProps)(ManagerDrawerContainer));