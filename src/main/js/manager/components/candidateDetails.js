import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import PropTypes from 'prop-types';
import { Divider,Statistic,Row,Col,Icon, Button ,Select,Table,Modal , message as AntMessage} from 'antd';
import {  getAllCandidate,searchCandidate, updateCandidatePassword } from '../reduxFlow/actions.js';
import CandidateRegistrationModal from './candidateRegistrationModal.js';

const confirm = Modal.confirm;
const styles = {
	    select: {
	      border: '2px solid',
	      borderColor: 'royalblue',
	      borderRadius: 6,
	      marginRight: 5,
	      marginBottom: 5,
	    },
};

let confirmReject = {};
class CandidateDetails extends Component {
  constructor(props) {
    super(props);
    this.state = { loading: false,
    	      showModal : false,
    	      mailId : ''
    };
  }

  componentDidMount() {
	  this.setState({  showModal : false});
	 const { dispatch } = this.props ; 
	 getAllCandidate(dispatch);
	 searchCandidate(dispatch);
	  }


	setTableData = () => {
	  const { getAllCandidate } = this.props;
	  const { mailId } = this.state;

	  if (!getAllCandidate || getAllCandidate.length == 0) {
	    return [];
	  }
	  let returnList = getAllCandidate.map(candidate => {    
		     const {  userCredentials } = candidate; 
			  const { mailId } = userCredentials;
			 return { mailId: mailId, key: mailId };
			  });
	  if(!mailId || mailId === ''){
		  return returnList;
		  }
	return  returnList.filter(data => data.key === mailId );
	}
	
	 resetCandidatePassword =async (row) =>{
	    const { dispatch } = this.props ; 
	    let param = {};
	    param.mailId = row.mailId;
	    confirmReject.update({ cancelButtonProps : {disabled : true } });
	    await updateCandidatePassword( param , dispatch );
	    confirmReject.update({ cancelButtonProps : {disabled : false } });
	    AntMessage.success(`${param.mailId} password has been successfully updated`);
	  }

	  showConfirm = async (row) => {
		  confirmReject =  confirm({
	      width: 480,
	      title: `Are you sure do you want to reset ${row.mailId} password?`,
	      centered: true,
	      onOk: async () => { await this.resetCandidatePassword(row); }
	    });
	  } 

	
	render() {
		const { total,searchCandidate } = this.props; 
		const { showModal,mailId }  = this.state;
		const columns = [
			  {
			    key: 'mailId',
			    title: 'Candidate',
			    dataIndex: 'mailId',
			    align : 'center'
			  },{
          title: 'Reset password',
          dataIndex: 'reset',
          key: 'reset',
          render:(text , row) => {
            return <Button onClick= {()=> this.showConfirm(row)}type="primary" icon="lock">Reset</Button>
          },
        },
			];

	  return (
  <div>
  <Row type="flex" justify="space-between">
  <Col>
    <Select
      showSearch
      allowClear
      placeholder="Candidate"
      style={{ width: 360 ,...styles.select}}
    onSelect={(value, option) => this.setState({ mailId: value }) }
    onChange={value =>  this.setState({ mailId: value })}
    optionFilterProp="children"
    filterOption={(input, option) =>
    option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0 }>
    {searchCandidate.map(candidate => <Option key={candidate}>{candidate}</Option>) }
</Select>
    </Col>
    <Col>
    <Button  type="primary" shape="circle" icon="plus" onClick={ () => this.setState({showModal :true }) } />
  </Col>
</Row>
<Divider/>
<Row type="flex" justify="start">
<Statistic
  prefix={total == 0 ? <Icon theme="twoTone" twoToneColor="red" type="dislike" /> : <Icon theme="twoTone" twoToneColor="#52c41a" type="like" />}
  value={total}
/>
</Row>
<Row style={{ marginTop: 16 }}>
  <Col>
    <Table
      bordered
      loading={this.state.loading} 
      columns={columns}
      dataSource={this.setTableData()}
      size="middle"
    />
  </Col>
</Row>
 { showModal && <CandidateRegistrationModal reload={() => this.componentDidMount()} close={() => this.setState({showModal : false })}  />}
  </div>
	  );
	}
}

function mapStateToProps(state) {
	  const  {count,value } = state.get('manager').toJS().getAllCandidate;
	  return {
		    getAllCandidate: value,
		    total : count,
		    searchCandidate :  state.get('manager').toJS().searchCandidate
	  };
	}

export default withRouter(connect(mapStateToProps)(CandidateDetails));
