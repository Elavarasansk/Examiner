import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Drawer, Form, Button, Input, Select, Icon  , Checkbox , Spin , Modal , message as AntMessage , Tooltip , Switch  , Row , Col , InputNumber } from 'antd';
import { getQuestionBankDetails , registerManager , updateQbModelView , handleQbSelectedList } from '../reduxFlow/actions';
import DrawerQuestionBankModal from '../components/drawerQuestionBankModal';
import QuestionBankSelectedList from '../components/questionBankSelectedList';

let confirmUpdate = {};
const { Option } = Select;
const { confirm } = Modal;
const ButtonGroup = Button.Group;
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
};
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
const tootTipSave = (
    <div>
      <p>Please fill above information to register !!!</p>
    </div>
  );


class ManagerDrawerContainer extends Component {
  
  constructor(props){
    super(props);
    this.state = {
        selectedList : [],
        questionBankCount:'',
        disableCheckbox:true,
        disableRegister :true,
        loading:false,
        toolTipView:false,
        switchType : false,
        purchasedCredits: undefined
    }
  }
  
  componentDidMount(){ 
    const { dispatch } = this.props;  
    getQuestionBankDetails(dispatch).catch(this.handleError);
    this.props.onRef(this);
  }

  componentWillUnmount(){
    this.props.onRef(undefined);
  }
  
  onClose = () => {
    const { close , dispatch } = this.props;
    confirm({
      title: 'Do you want to close without registering the Manager?',
      content: 'Unsaved data will be lost',
      centered: true,
      onOk: async () => {
        await handleQbSelectedList( dispatch , [] );
        close();
      }
    });
  };
  
  handleSubmit = (e) => {
    this.setState({ loading: true });
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        this.submitRegister();
      }
      this.setState({loading: false });
    });
  };
  
  submitRegister = () => {
    const { form } = this.props;
    let param = form.getFieldsValue();
    confirmUpdate = confirm({
      title: `Are you Sure do you want to register ${param.mailId} as manager?`,
      onOk: async () => { await this.onOkConfirm().catch(this.handleError) },
      centered: true,
      onCancel() {
      },
    });
  }
  
  onOkConfirm = async () => {
    const { form , dispatch , selectedList , close } = this.props;
    const { switchType } = this.state;
    let param = form.getFieldsValue();
//    param.questionBankList = selectedList;
    param.company =  param.company.substring(0, 1).toUpperCase() + param.company.substring(1).toLowerCase();
    param.questionBankList = this.questionBankList();
    param.switchType = switchType;
    confirmUpdate.update({ cancelButtonProps : {disabled : true } });
    await registerManager(param , dispatch);
    confirmUpdate.update({ cancelButtonProps : {disabled : false } });
    AntMessage.success(`${param.mailId} has been successfully registered as Manager`);
    close();
    this.props.changechild();
    form.resetFields();
    this.props.screenReload();
  }
  
  questionBankList = () => {
    const { allQuestionBankDetails } = this.props;
    return allQuestionBankDetails.map(qbValue => 
      qbValue.questionBankName
    );   
  }
  
  compareToFirstPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && value !== form.getFieldValue('password')) {
      callback('Two passwords that you enter is inconsistent!');
    } else {
      callback();
    }
  };

  validateToNextPassword = (rule, value, callback) => {
    const form = this.props.form;
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], { force: true });
    }
    callback();
  };
  
  disableCheck = () => {
    const form = this.props.form;
    const { selectedList  } = this.props;
    const { switchType , purchasedCredits } = this.state;
    let formValues = form.getFieldsValue();
    
    if( !switchType ){
      if ( formValues.mailId != undefined && formValues.password != undefined && formValues.confirm != undefined && formValues.company != undefined ){
        this.setState({ disableCheckbox : false });
        formValues.agreement == true ?  this.setState({ disableRegister : false }) :  this.setState({ disableRegister : true });
      }else{
        this.setState({ disableCheckbox : true });
      }
    }else{
      if ( formValues.mailId != undefined && formValues.company != undefined ){
        this.setState({ disableCheckbox : false });
        formValues.agreement == true ?  this.setState({ disableRegister : false }) :  this.setState({ disableRegister : true });
      }else{
        this.setState({ disableCheckbox : true });
      }
    }
  }
  
  disableRegister = () => {
    const form = this.props.form;
    const { disableCheckbox } = this.state;
    let formValues = form.getFieldsValue();
    if(disableCheckbox == false){
      let checkValues =   Object.values(form.getFieldsValue()) ;
      let checkArray = checkValues.map(data => data !=undefined ? data.toString().trim() : data);
      checkArray.indexOf(undefined) == '-1' && checkArray.indexOf('')  == '-1' ? 
          this.setState({ disableRegister : false }) : this.setState({ disableRegister : true });
    }else {
      this.setState({ disableRegister : true });
    }
  }
  
  formChange = ()=> {
    this.disableCheck(); 
  }
  
  modalView = () => {
     const { dispatch } = this.props;  
     updateQbModelView( dispatch , true );
  }
  
  questionBankCount = () => {
    const { selectedList } = this.props;  
    this.disableCheck();
    this.disableRegister();
    selectedList.length > 0 ? this.setState({ question : `${selectedList.length} Question Bank's Selected` }) : this.setState({ question : undefined });
  }
  
  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  viewQbSelectedList = () => {
    this.setState({ qbSelectedList: true });
  }
  
  onChange = (event) => {
      this.setState({ switchType: event });
      const { form } = this.props;
      let formValues = form.getFieldsValue();
  }
  
  creditChange = async (value) => {
    await this.setState({ purchasedCredits : value });
    this.disableCheck();
  }
  
  render(){
    
    const { getFieldDecorator } = this.props.form;
    const { disableRegister , disableCheckbox , question , loading , toolTipView , qbSelectedList , switchType } = this.state;
    const { modalQbView , selectedList , open , allQuestionBankDetails} = this.props;
    
    return(
      <Spin spinning={loading} >
      <div>
        <Drawer
        title="Create a new Manager"
        width={620}
        onClose={this.onClose}
        visible={open}
        maskClosable={false}
        >
        <Row type="flex"  justify='end'>
        <b>Auto-Generate Password:</b> <Switch checkedChildren="Yes" unCheckedChildren="No" defaultunChecked  onChange={this.onChange}/>
        </Row>
        
        <Row style={{ marginTop : 20 }} >
        <Form layout="vertical"  onSubmit={this.handleSubmit} onChange = {this.formChange}>
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
          })(<Input placeholder="Please input your E-mail!" />)}
          </Form.Item>
          
          {!switchType && <Form.Item label="Password" hasFeedback>
          {getFieldDecorator('password', {
            rules: [
              {
                required: true,
                message: 'Please input your password!',
              },
              {
                validator: this.validateToNextPassword,
              },
            ],
          })(<Input type="password"  placeholder="Please input your password!"/>)}
          </Form.Item> }
     
          {!switchType && <Form.Item label="Confirm Password" hasFeedback>
          {getFieldDecorator('confirm', {
            rules: [
             {
              required: true,
              message: 'Please confirm your password!',
             },
             {
              validator: this.compareToFirstPassword,
             },
            ],
          })(<Input type="password"  placeholder="Please confirm your password!"/>
          )}
          </Form.Item> }
         
          <Form.Item label="Company name">
          {getFieldDecorator('company', {
            rules: [{ required: true, message: 'Please input your company name!' }],
          })(<Input style={{ width: '100%' }}  placeholder="Please input your company name!" />
          )}
          </Form.Item>
          
          <Form.Item  {...formItemLayout} label="Credit Value :">
          {getFieldDecorator('purchasedCredits', {
          })(
              <InputNumber onChange = {this.creditChange} min={1} max={100000} defaultValue={0} />
          )}
          </Form.Item>
      
          { false &&  <div style = {{ marginBottom : 30 , marginTop:10 }}>
          <Input value = {question} disabled ='true' style={{ color : 'forestgreen' , width: '75%' , marginRight:10}}
          placeholder="Select the question bank"/>
          <ButtonGroup>
          {selectedList.length >0 && <Tooltip placement="bottom" title = "Click to view the selected QB" >
          <Button onClick= { this.viewQbSelectedList } type="primary" icon="info-circle" />
          </Tooltip>}
          <Button onClick= {this.modalView} type="primary">
          Choose QB
          </Button>
          </ButtonGroup>
          </div> }
            
          <Form.Item  style={{ textAlign : 'center' }}>
          {getFieldDecorator('agreement', {
          valuePropName: 'checked',
          })(
          <Checkbox disabled = {disableCheckbox} onClick = { this.disableRegister}>
           I Confirm the above Information
          </Checkbox>,
          )}
          </Form.Item>
       
          <Form.Item  style={{ textAlign : 'center' }}>
          <Tooltip placement="bottom" title={disableRegister == true ? tootTipSave : undefined} >
          <Button style={styles.button} disabled = {disableRegister} type="primary" htmlType="submit">
           Register
          </Button>
          </Tooltip>
          </Form.Item>
          
        </Form>
        </Row>
        </Drawer>
        { false &&  <DrawerQuestionBankModal questionBankCount = {this.questionBankCount.bind(this)}
          onRef = {ref => (this.child = ref)} /> }
        { false &&  <QuestionBankSelectedList open = { qbSelectedList } close={() => {
           this.setState({ qbSelectedList : false })}}/> } 
        </div>
      </Spin>
    )
  }
}

function mapStateToProps(state) {
  return {
    modalQbView: state.get('admin').get('updateQbModelView'),
    selectedList : state.get('admin').toJS().handleQbSelectedList,
    allQuestionBankDetails : state.get('admin').toJS().getQuestionBankDetails,
  };
}

const WrappedManagerDrawerContainer = Form.create()(ManagerDrawerContainer);
export default withRouter(connect(mapStateToProps)(WrappedManagerDrawerContainer)); 