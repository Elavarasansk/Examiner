import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Button , Modal , Table , Tag , Row , Form , Input , Select , Statistic , Icon , Spin ,  message as AntMessage , Divider , Badge} from 'antd';
import { getManagersDetails , getQuestionBankDetails , getSingleManagerDetail , getFilteredEmailId , updateManagerPassword } from '../reduxFlow/actions';
import ManagerDrawerContainer from './managerDrawerContainer';
import '../../common/styles/questionbank.css';
import QuestionBankUpdateModal from '../components/questionBankUpdateModal';

const { confirm } = Modal;
const Search = Input.Search;
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

let confirmReject = {};
class ManagerInformationContainer extends Component {

  constructor(props){
    super(props);
    this.state = {
        visible: false,
        singleManagerData : [],
        selectedList : [],
        componentMailMap : {},
        componentCompanyMap : {},
        company : undefined,
        mailId: undefined,
        tableCount:'',
        loading:false,
        drawerVisible:false,
        updateVisible : false,
        selectedRow: {}
    }
  }

  componentDidMount(){
    this.screenLoad();
  }
  
  screenLoad = async() => {
    this.setState({ loading: true });
    await this.allManagersInfo();
    this.filterComponentLoad();
    this.setState({ loading: false });
  }
  
  allManagersInfo = async() => {
    const { dispatch } = this.props
    const { company , mailId } = this.state;
    let param = {};
    param.mailId =  mailId;
    param.company = company;
    await getManagersDetails(dispatch , param).catch(this.handleError);
  }
  
  filterComponentLoad = () => {
    const { allManagerDetails } = this.props;
    const { company , mailId } = this.state;
    let mailValueMap = {};
    let companyValueMap = {};
    { allManagerDetails.length>0 && allManagerDetails.forEach(data=> {
      mailValueMap[data.mailId] = data.company ;
      companyValueMap[data.company] = data.mailId;
    })};
    this.setState({ componentMailMap: mailValueMap , componentCompanyMap: companyValueMap , tableCount : allManagerDetails.length });
  }
  
  changeCamelCase = (company) => {
    return company.substring(0, 1).toUpperCase() + company.substring(1).toLowerCase()
  }


  handleOk = e => {
    this.setState({ visible: false});
  };

  handleCancel = e => {
    this.setState({
      visible: false,
    });
  };
  
  showModal = async( row ) => {
    const { dispatch } = this.props;  
    await getSingleManagerDetail( row.mailId,dispatch).catch(this.handleError);
    const { singleManagerInfo } = this.props;
    const { selectedList } = this.state;

    let singleManagerArray = [];
    let singleManagerObject = {};
    singleManagerInfo.forEach( (data) => {
      singleManagerArray.push({ 'questionBankName' : data });
    })
    let resultList = Array.from(new Set(selectedList.concat(singleManagerInfo)));
    this.setState({ visible: true ,  singleManagerData:singleManagerArray ,selectedList : resultList});
  };

  changechild(){
    this.allManagersInfo();
  }

  onChange = async (value) =>{
    const { componentMailMap } = this.state;
    const { company } = this.state;
    this.setState({ loading: true });
    let param = {};
    param['mailId'] = value;
    param['company'] = company ;
    this.handleFilterChange(param);
    this.setState({ mailId: value });
  }

  onCompanyChange = async (value) => {
    const { mailId } = this.state;
    this.setState({ loading: true });
    let param = {};
    param['mailId'] = mailId;
    param['company'] = value ;
    this.handleFilterChange(param);
    this.setState({ company: value });
  }
  
  handleFilterChange = async(param) => {
    const { dispatch } = this.props;
    await getManagersDetails( dispatch , param).catch(this.handleError);
    const { allManagerDetails } = this.props;
    let mailValueMap = {};
    allManagerDetails.forEach(data=> {
      mailValueMap[data.mailId] = data.company;
    });
    this.setState({ loading: false });
    this.setState({ componentMailMap: mailValueMap , tableCount : allManagerDetails.length });
  }

  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  showDrawer = () => {
    this.setState({ drawerVisible: true });
  };
  
  showUpdateModal = (row) => {
    this.setState({ updateVisible: true , selectedRow: row });
  }
  
  resetManagerPassword =async (row) =>{
	    const { dispatch } = this.props ; 
	    let param = {};
	    param.mailId = row.mailId;
	    confirmReject.update({ cancelButtonProps : {disabled : true } });
	    await updateManagerPassword( param , dispatch );
	    confirmReject.update({ cancelButtonProps : {disabled : false } });
	    AntMessage.success(`${param.mailId} password has been successfully updated`);
	  }

 showConfirm = async (row) => {
	 confirmReject = confirm({
	      width: 480,
	      title: `Are you sure do you want to reset ${row.mailId} password?`,
	      centered: true,
	      onOk: async () => { await this.resetManagerPassword(row); }
	    });
	} 

  render(){

    const {allManagerDetails  , singleManagerInfo} = this.props;
    const { singleManagerData , selectedList , visible , drawerVisible , updateVisible , selectedRow } = this.state; 
    const { componentMailMap , company , mailId , componentCompanyMap , tableCount , loading } = this.state;
    const { getFieldDecorator } = this.props.form;
    const columns = [
      {
        title: <strong><h4>Email-Id</h4></strong>,
        dataIndex: 'mailId',
        key: 'mailId',
        sorter: (a, b) => a.mailId.length - b.mailId.length,
        render:(text , row) => {
          return <span>{text}</span>
        },
      },
      {
        title: <strong><h4>Company Name</h4></strong>,
        dataIndex: 'company',
        key: 'company',
        render:(text , row) => {
          return <span>{text}</span>
        },
      },
      {
        title: 'Credits Information',
        children: [
          {
            title: <strong><h4>Purchased Credits</h4></strong>,
            dataIndex: 'purchasedCredits',
            key: 'purchasedCredits',
            align : 'center',
            width:'15%',
            sorter: (a, b) => a.purchasedCredits - b.purchasedCredits,
            render:(text , row) => {
              return <span><h5><Tag color="green">{ text == undefined ? 0 : text }</Tag></h5></span>
            },
          },
          {
            title: <strong><h4>Used Credits</h4></strong>,
            dataIndex: 'usedCredits',
            key: 'usedCredits',
            align : 'center',
            width:'15%',
            sorter: (a, b) => a.usedCredits - b.usedCredits,
            render:(text , row) => {
              return <span><h5><Tag color="red">{text == null || text == undefined ? text == undefined ? 0 :parseInt(text) - 0 : parseInt(text)}</Tag></h5></span>
            },
          },
          {
            title: <strong><h4>Credits Remaining</h4></strong>,
            dataIndex: 'remainingCredits',
            key: 'remainingCredits',
            align : 'center',
            width:'15%',
            sorter: (a, b) => a.remainingCredits - b.remainingCredits,
            render:(text , row) => {
              return <span><h5><Tag color="green">{text}</Tag></h5></span>
            },
          },
        ],
      },
      {
    	title: 'Manager Edit',
        children: [{  
        title: <strong><h4>Credit Edit</h4></strong>,
        key: 'count',
        dataIndex: 'count',
        align : 'center',
        render:(text , row) => {
          return <span><span style = {{marginRight : 10}}><Button onClick={()=>this.showUpdateModal(row)} icon="edit" size= "small" shape="square" type="primary" ></Button></span>
          { false && <Badge count={text}><Button onClick={()=>this.showModal(row)} icon="info-circle" size= "small" shape="square" type="primary" ></Button> </Badge> }
          </span>
        	  }
            },{  
          title: <strong><h4>Password Reset</h4></strong>,
          key: 'reset',
          dataIndex: 'reset',
          align : 'center',
          render:(text , row) => {
          return <Button onClick={()=>this.showConfirm(row)} icon="lock" size= "small" shape="square" type="primary" >Reset</Button>
              }
            }
          ]
      	}
      ];

    const questionBankColumns = [
      {
        title: 'Question Bank Name',
        dataIndex: 'questionBankName',
        key:'questionBankName'
      }
      ];


    const rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {
          this.setState( { selectedList : selectedRowKeys });
        },
        selectedRowKeys : selectedList,
        getCheckboxProps: record => ({
          disabled: record.questionBankName === record.questionBankName, // Column configuration not to be checked
        }),
    };

    return(
      <div>
        <Row>
        <Select
            showSearch
            allowClear
            style={styles.select}
            placeholder="Select Your Email-Id"
            optionFilterProp="children"
            value = {mailId}
            onChange={this.onChange}
            filterOption={(input, option) =>
              option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
        >
          {Object.keys(componentMailMap).map(mail => <Option value={mail}>{mail}</Option> )}
        </Select>

          <Select
            showSearch
            allowClear
            style={styles.select}
            placeholder="Select Your Company Name"
            value = {company}
            optionFilterProp="children"
            onChange={this.onCompanyChange}
            filterOption={(input, option) =>
              option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
          >
          {Array.from(new Set (Object.values(componentMailMap))).map(company => <Option value={company}>{company}</Option>)}
          </Select>
          <Divider />
          </Row>
          <Row>
          <Button  style={{ marginBottom: 10 , float:'right'}} type="primary" onClick={this.showDrawer}>
          <Icon type="plus" /> New Manager
          </Button>
          {drawerVisible && <ManagerDrawerContainer
            open={drawerVisible} close={() => {
              this.setState({ drawerVisible : false })}}
             changechild = {this.changechild.bind(this)}
             onRef = {ref => (this.child = ref)} screenReload={this.screenLoad}
             /> }
          <Statistic  style={{ marginBottom: 10 }}
          prefix={tableCount == 0 ? <Icon theme="twoTone" twoToneColor="red" type="dislike" /> : <Icon theme="twoTone" twoToneColor="#52c41a" type="like" />}
          value={ tableCount }
          />
          </Row>
          <Row>
        <Table
          loading = {loading}
          columns={columns}
          bordered
          size="middle"
          style={{ border:'0px solid', backgroundColor:'floralwhite'}}
          dataSource={allManagerDetails? allManagerDetails:[]}
          /> </Row>
        <Modal
          title="Question Bank Details"
          width = {500}
          visible={visible}
          onOk={this.handleOk}
          centered
          onCancel={this.handleCancel}
          footer={[
          <Button key="submit" type="primary" onClick={this.handleOk}>
            Ok  
          </Button>
          ]}
        >
          <Table
            id={"questionbank"}
            size="middle"
            bordered
            style={{ border:'0px solid', backgroundColor:'white'}}
            rowSelection={rowSelection}
            rowKey={record => record.questionBankName}
            columns={questionBankColumns}
            dataSource={singleManagerData? singleManagerData:[]} />
        </Modal>
      {updateVisible && <QuestionBankUpdateModal row = { selectedRow } open = { updateVisible } close={() => {
            this.setState({ updateVisible : false });  this.screenLoad()}} cancel = {() => {
            	this.setState({ updateVisible : false });	
            }}/>} 
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    allManagerDetails: state.get('admin').toJS().getManagersDetails,
    singleManagerInfo: state.get('admin').toJS().getSingleManagerDetail,
  };
}

const WrappedManagerInformationContainer = Form.create()(ManagerInformationContainer);
export default withRouter(connect(mapStateToProps)(WrappedManagerInformationContainer));