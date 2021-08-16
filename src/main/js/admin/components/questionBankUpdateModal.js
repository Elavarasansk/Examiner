import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { getQuestionBankDetails , getSingleManagerDetail , updateManagerDetails , updateCreditDetails } from '../reduxFlow/actions';
import { List , Modal , Button , Form , Input , Select , Tooltip , InputNumber , Spin , message as AntMessage } from 'antd';

const { Option , OptGroup } = Select;
const styles={
    select:{
      width: 400,
      border: '2px solid',
      borderColor: 'royalblue',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5,
      marginTop:5
    },
    button:{
      backgroundColor:'green',
      marginTop: 10,
      marginRight: 10  
    }
}
const formItemLayout = {
    labelCol: {
      xs: { span: 24 },
      sm: { span: 5 },
    },
    wrapperCol: {
      xs: { span: 24 },
      sm: { span: 12 },
    },
  };
const tootTipUpdate = (
    <div>
      <p>Please Update the information to register !!!</p>
    </div>
  );

class QuestionBankUpdateModal extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        allManagerArray : [],
        updateSelectedList : [],
        registerdisable : true,
        creditValue : undefined,
        loading : false
    }
  }
  
  componentDidMount = async() => {
    const { dispatch , form , row } = this.props;  
    const { singleManagerInfo } = this.props;
    let responseArray = [];
    form.setFieldsValue({ mailId : row.mailId , company : row.company , purchasedCredits : row.purchasedCredits });
    this.setState({allManagerArray :responseArray , updateVisible: true});
  }

  handleUpdateCancel = e => {
    const { cancel } = this.props;  
    cancel();
  };

  handleUpdateOk = e => {
    const { close } = this.props;
    close();
  };

  handleUpdateChange = (value) => {
   this.setState({ updateSelectedList: value });
  }
  
  handleUpdateManagerSubmit = async () => {
    const { updateSelectedList } = this.state;
    const { dispatch , row , close } = this.props;
    let param = {};
    param.mailId = row.mailId;
    param.company = row.company;
    param.questionBankList = updateSelectedList;
    await updateManagerDetails( dispatch , param );
    close();
  }
  
  handleUpdateCreditSubmit =  async () => {
    const { row } = this.props;
    await this.updateManagerCreditInfo().catch(this.handleError);
  }
  
  updateManagerCreditInfo = async () => {
    const { creditValue } = this.state;
    const { dispatch ,  close , row } = this.props;
    let param = {};
    param.mailId = row.mailId;
    param.purchasedCredits = creditValue;
    this.setState( { loading  : true  } ) ; 
    await updateCreditDetails( dispatch , param );
    this.setState( { loading  : false  } ) ; 
    close();
    AntMessage.success(`Purchased Credits has been successfully updated for ${row.mailId}`);
  }
  
  
  creditChange = async (value) => {
    const { row } = this.props;
    row.purchasedCredits != value ? this.setState({ registerdisable: false , creditValue: value }) : this.setState({ registerdisable: true , creditValue: value });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  render(){
    const { open , singleManagerInfo} = this.props;
    const { getFieldDecorator } = this.props.form;
    const { allManagerArray , updateSelectedList , registerdisable,loading } = this.state;
  
    return(
        <Modal
        title="Update Information"
        style={{ top: 20 }}
        visible={open}
        onOk={this.handleUpdateOk}
        onCancel={this.handleUpdateCancel}
        centered
        footer={null}>
        <Spin spinning={loading}>
        <Form onSubmit={this.handleUpdateCreditSubmit} >
          <Form.Item label="E-mail">
          {getFieldDecorator('mailId', {
            rules: [
              {
                type: 'email',
                message: 'The input is not valid E-mail!',
              },
              {
                required: true,
                message: 'Please input your E-mail!',
              },
              ],
          })(<Input disabled placeholder="Please input your Email"/>)}
          </Form.Item>
  
          <Form.Item label="Company name">
            {getFieldDecorator('company', {
              rules: [{ required: true, message: 'Please input your company name!' }],
            })(
                <Input disabled placeholder="Please input your company name!" />
            )}
          </Form.Item>
          
          <Form.Item  {...formItemLayout} label="Credit Value :">
          {getFieldDecorator('purchasedCredits', {
            rules: [{ required: true, message: 'Please input your Credit Value!' }],
          })(
              <InputNumber onChange = {this.creditChange} min={1} max={100000} defaultValue={0} />
          )}
          </Form.Item>
          
          { false && <Form.Item  label="Question bank Name">
          {getFieldDecorator('question', {
            rules: [{
              required: true, message: 'Please select your Question Banks!',
            }],
          })(
              <Select
              mode="multiple"
              style={{ width: '100%' }}
              allowClear
              placeholder="Please select Question Bank"
              onChange={this.handleUpdateChange}
              >
              <OptGroup label="Chosen">
              {allManagerArray.map(allData => 
              singleManagerInfo.indexOf(allData) != -1 ? <Option disabled value={allData}>{allData}</Option> : ''
              )}
              </OptGroup>
              <OptGroup label="Not-Chosen">
              {allManagerArray.map(allData => 
              singleManagerInfo.indexOf(allData) == -1 ? <Option value={allData}>{allData}</Option> : ''
              )}
              </OptGroup>
              </Select>
          )}
          </Form.Item> }

          <Form.Item style={{ textAlign : 'center' }}>
          <Tooltip placement="right" title={ registerdisable ? tootTipUpdate : undefined } >
          <Button disabled = { registerdisable } style={styles.button}  type="primary" htmlType="submit">Submit</Button>
          </Tooltip>
          </Form.Item>

        </Form>
        </Spin>
      </Modal>
    )
  }
}

function mapStateToProps(state) {
  return {
    singleManagerInfo: state.get('admin').toJS().getSingleManagerDetail,
    updateManagerInfo: state.get('admin').toJS().updateManagerDetails,
  };
}

const WrappedQuestionBankUpdateModal= Form.create()(QuestionBankUpdateModal);
export default withRouter(connect(mapStateToProps)(WrappedQuestionBankUpdateModal)); 
