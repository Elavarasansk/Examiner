import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Input, Modal, Button, Select, Icon, Spin } from 'antd';
import { Tooltip, Popover, Divider, Upload, message as AntMessage  } from 'antd';
import { getCategoryGroups, getQuestionBankTemplate, uploadQuestionBank } from '../reduxFlow/actions';

const { Option } = Select;
const { confirm } = Modal;
const FileSaver = require('file-saver');

const styles={
    select:{
      width: 200,
      border: '2px solid',
      borderColor: '#080808',
      borderRadius: 6,
      marginRight: 5,
      marginBottom: 5
    }
}

const { Search } = Input;
const { Dragger } = Upload;

class AddQuestionBank extends Component {

  constructor(props){
    super(props);
    this.state = {
        loading:false,
        questionBankName: undefined,
        questionBankFile:[],
        filter: {
        }
    }
  }

  componentDidMount(){
    const { dispatch } = this.props;
    getCategoryGroups(dispatch);
  }

  handleFilterChange = (type, name, value) => {
    const { filter } = this.state;
    const { category, subCategory } = this.state.filter;
    filter[name] = value;

    if(name==="category"){
      switch (value){
        case undefined:
          filter.subCategory = value;
          break;
        default:
          const { categoryGroup } = this.props;
          const categoryObj = categoryGroup.size>0? categoryGroup.toJS():[];
          if(subCategory && categoryObj[value] &&
              categoryObj[value].indexOf(subCategory) === -1){
            filter.subCategory = undefined;
          };
          break;
      }
    }
    this.setState({ filter });
  }

  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }

  closeConfirm = () => {
    const { close } = this.props;
    confirm({
      title: 'Do you want to close without adding the QuestionBank?',
      content: 'Unsaved data will be lost',
      onOk() {
        close();
      },
      onCancel() {
      },
    });
  }
  
  handleQbUpload = () => {
    this.uploadQb().catch(this.handleError);
  }
  
  uploadQb = async() => {
    const { dispatch, close } = this.props;
    const { filter, questionBankName, questionBankFile } = this.state;
    const { category, subCategory } = filter;
    const param = {
        category, subCategory, questionBankName, file: questionBankFile[0].originFileObj
    }
    this.setState({ loading: true });
    await uploadQuestionBank(dispatch, param);
    this.setState({ loading: false });
    AntMessage.success(`Question Bank has been Uploaded succesfully:: ${category}-${subCategory}-${questionBankName}`);
    close();
  }

  handleError = (err) => {
    AntMessage.error(`${err.message}`);
    this.setState({ loading: false });
  }
  
  downloadQbTemplate = async() => {
    const { dispatch } = this.props;
    const qbTemplateFile = await getQuestionBankTemplate(dispatch);
    FileSaver.saveAs(qbTemplateFile, 'questionbank_template.xls');
  }
  
  handleQuestionBankNameChange = (e) => {
    const questionBankName = e.target.value.trim().toUpperCase();
    this.setState({ questionBankName });
  }
  
  beforeQBUpload = (file) => {
     const fileType = file.name.split('.')[1];
	  if (fileType !== 'xls') {
	    AntMessage.error('File with wrong extension. Please upload .xls file');
	    return true;
	  } else {
	    return false;
	  }
 };
 
 handleFileRemove = () => {
   this.setState({ questionBankFile: [] });
 }
  
	handleFileChange = (event) => {
		  const { file, status } = event;
		  const fileType = file.name.split('.')[1];
		  if (fileType !== 'xls') {
		    return;
		  }
		  let fileList = event.fileList;
		  fileList = fileList.slice(-1);
		  this.setState({ questionBankFile : fileList});
		  if (status === 'done') {
		    AntMessage.success(`${info.file.name} file uploaded successfully.`);
		  } else if (status === 'error') {
		    AntMessage.error(`${info.file.name} file upload failed.`);
		  }
		}
	
  render() {
    const { open } = this.props;
    const { loading, filter, questionBankName, questionBankFile } = this.state;
    const { categoryGroup } = this.props;
    const { category, subCategory } = filter;

    const categoryObj = categoryGroup.size>0? categoryGroup.toJS():[];
    
    const uploadProps = {
        name: 'file',
        multiple: true,
        onRemove: this.handleFileRemove,
	    onChange: this.handleFileChange
    };

    return (
        <Modal
          title="QuestionBank Configuration"
          visible={open}
          onCancel={this.closeConfirm}
          footer={null}
        >
          <Spin spinning={loading}>
            <Select
            allowClear
            showSearch
            placeholder="Category"
            optionFilterProp="children"
            value={category}
            style={styles.select}
            onChange={value => this.handleFilterChange("select", "category", value)}
            filterOption={(input, option) =>
              option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
          >
            {Object.keys(categoryObj).map(data => <Option value= {data}>{data}</Option>)}
          </Select>
          
          <Select
            allowClear
            showSearch
            placeholder="Sub-Category"
            optionFilterProp="children"
            value={category? subCategory: undefined }
            style={styles.select}
            onChange={value => this.handleFilterChange("select", "subCategory", value)}
            filterOption={(input, option) =>
              option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
          >
            {categoryObj[category] && categoryObj[category].map(data => <Option value= {data}>{data}</Option>)}
          </Select>
          
          <Divider/>
          
          <Input
            style={{ width: 200, marginBottom: 5, marginRight: 10 }}
            placeholder="QuestionBank name"
            value={questionBankName}
            onChange={this.handleQuestionBankNameChange}
            prefix={<Icon type="book" style={{ color: 'rgba(0,0,0,.25)' }} />}
            suffix={
              <Tooltip title="Enter your QuestionBank name">
                <Icon type="info-circle" style={{ color: 'rgba(0,0,0,.45)' }} />
              </Tooltip>
            }
          />
         <Button type="primary" icon="download" onClick={this.downloadQbTemplate} size="small" >Sample-Template</Button> 
            
         <Dragger {...uploadProps} fileList={questionBankFile} beforeUpload={this.beforeQBUpload} >
            <p className="ant-upload-drag-icon">
              <Icon type="cloud-upload" />
            </p>
            <p className="ant-upload-text">Click or drag questionBank file to this area to upload</p>
            <p className="ant-upload-hint">
              Please upload in a desired format for proper validation.
            </p>
          </Dragger>
          <div style={{ margin:'auto', width:100, marginTop: 10 }} >
          
          <Popover placement="bottom" content={["Category", "Sub-category", "QuestionBankName"].map((val,i) => <div>{++i}. <i>{val}</i></div>)} title="Please Select" trigger="hover">
            <Button type="primary" onClick={this.handleQbUpload} style={{ backgroundColor: 'green', color: 'whitesmoke' }}
              disabled={!category || !subCategory || !questionBankName || questionBankFile.length === 0 } >SAVE</Button>
          </Popover>
          </div>
        </Spin>
      </Modal>
    );
  }
}

function mapStateToProps(state) {
  return {
    categoryGroup: state.get('admin').get('categoryGrouping'),
  };
}

export default withRouter(connect(mapStateToProps)(AddQuestionBank));