import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { Row,Icon, Button, Modal, Upload, Table, message as AntMessage } from 'antd';
import { getParseData,downloadTemplate,addAllCandidate } from '../reduxFlow/actions.js';

const FileSaver = require('file-saver');

const { Dragger } = Upload;
const confirm = Modal.confirm;
let confirmUpdate = {};

class CandidateBulkRegistration extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loadingModal: false,
      showModal: false,
      fileList: []
    };
  }
  
  componentDidMount = () => {
	  this.setState({ loadingModal: false,showModal: false,fileList: [] }); 
  }

	onClose = () => {
	  this.props.hideCandidate();
	};
	
	reload = () => {
		const { reload } =  this.props;
		reload();
		};

	showBulkRegisterConfirm = async () => {
		confirmUpdate = confirm({
	    width: 480,
	    title: 'Are you sure do you want to create a candidate?',
	    centered: true,
	    onOk: async () => { await this.onBulkRegisterConfirmClick(); }
	  });
	}

	onBulkRegisterConfirmClick = async () => {
	  await this.registerBulkCandidate().catch(this.handleError);
	};

	handleBulkRegisterSubmit = async () => {
	  const { selectedList } = this.state;
	  if (selectedList && selectedList.length > 0) {
	    this.showBulkRegisterConfirm();
	  } else {
	    AntMessage.error('Empty record cannot be registered.');
	  }
	}

	handleError = (err) => {
	  AntMessage.error(`${err.message}`);
	}

	registerBulkCandidate = async () => {
	  const { form, dispatch } = this.props;
	  const { selectedList } = this.state;
	  confirmUpdate.update({ cancelButtonProps : {disabled : true } });
	  await addAllCandidate(selectedList, dispatch);
	  confirmUpdate.update({ cancelButtonProps : {disabled : false } });
	  this.reload();
	  AntMessage.success('Candidate created successfully.');
	};

	downloadTemplate = async () => {
	  const { dispatch } = this.props;
	  const file = await downloadTemplate(dispatch);
	  const filName = 'candidate_templatefile.xls';
	  FileSaver.saveAs(file, filName);
	};

	beforeUpload = (file) => {
	  const fileType = file.name.split('.')[1];
	  if (fileType !== 'xls') {
	    AntMessage.error('File with wrong extension. Please upload .xls file');
	    return true;
	  } else {
	    return false;
	  }
	};

	handleFileChange = (event) => {
	  const { file, status } = event;
	  const fileType = file.name.split('.')[1];
	  if (fileType !== 'xls') {
	    return;
	  }
	  let fileList = event.fileList;
	  fileList = fileList.slice(-1);
	  this.setState({ fileList, selectedList: [] });
	  if (status === 'done') {
	    AntMessage.success(`${info.file.name} file uploaded successfully.`);
	  } else if (status === 'error') {
	    AntMessage.error(`${info.file.name} file upload failed.`);
	  }
	}

	previewModal = () => {
	  this.getParseData().catch(this.handleError);
	}

	getParseData = async () => {
	  const { fileList } = this.state;
	  const { dispatch } = this.props;
	  this.setState({ loadingModal: true });
	  await getParseData({ file: fileList[0].originFileObj }, dispatch);
	  this.setState({ loadingModal: false, showModal: true });
	}


	render() {
	  const { showModal, fileList, selectedList, loadingModal } = this.state;
	  const { dataList } = this.props;
	  const props = {
	    name: 'file',
	    multiple: true,
	    onChange: this.handleFileChange,
	  };
	  const columns = [
	    {
	      title: 'Candidate',
	      align: 'center',
	      dataIndex: 'mailId',
	      key: 'mailId',
	    }];
	  const rowSelection = {
	    onChange: (selectedRowKeys, selectedRows) => {
	      this.setState({ selectedList: selectedRowKeys });
	    },
	    selectedRowKeys: selectedList,
	  };
	  return (
    <div> 
    { !showModal && <div>
				File Template : <Button style={{ marginBottom: 50 }} type="primary" shape="circle" icon="download" onClick={this.downloadTemplate} />
        <Dragger {...props} fileList={fileList} beforeUpload={this.beforeUpload}>
          <p className="ant-upload-drag-icon">
            <Icon type="inbox" />
          </p>
          <p className="ant-upload-text">Click or drag file to this area to upload</p>
          <p className="ant-upload-hint">
				Support for a single or bulk upload. Strictly prohibit from uploading company data or other
				band files
          </p>
        </Dragger>
        <Button disabled={!(fileList && fileList.length > 0)} style={{ marginLeft: '45%', marginTop: 50 }} type="primary" onClick={this.previewModal} >
				Preview
        </Button> 
       </div> }
   { showModal &&   <div>
    <Table rowKey={record => record.mailId} rowSelection={rowSelection} bordered size="middle" loading={loadingModal} columns={columns} dataSource={dataList} />
   <Row type="flex" justify="center" >
   <Button onClick={()=>this.setState({ showModal : false })} style={{marginRight : 8}}>Previous</Button> 
   <Button onClick={this.handleBulkRegisterSubmit} type="primary" >Register</Button>
   </Row> </div>
   } </div>
	  );
	}
}

function mapStateToProps(state) {
	  return {
	    dataList: state.get('manager').toJS().getParseData,
	  };
	}

export default withRouter(connect(mapStateToProps)(CandidateBulkRegistration));
