import React, { Component } from 'react';
import { Modal } from 'antd';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';
import { G2, Chart, Geom, Axis, Tooltip, Coord, Label, Legend, View, Guide, Shape, Facet, Util } from 'bizcharts';
import DataSet from '@antv/data-set';

class TestResultChart extends Component {
  constructor(props) {
    super(props);
  }
  
  render() {
    const cols = { percent: { formatter: (val) => { val = `${Math.round(val * 100)}%`; return val; } } };
    const { showAnalysisModal, chartData } = this.props;
    const { DataView } = DataSet;
    const dv = new DataView();
    dv.source(chartData).transform({
      type: 'percent', field: 'count', dimension: 'item', as: 'percent',
    });
    return (<div>
      <Modal
        title="Result Analysis"
        visible={showAnalysisModal}
        footer={null}
        onCancel={() => this.props.close()}
      >
        <Chart height={420} data={dv} scale={cols} padding={[80, 100, 80, 80]} forceFit >
          <Coord type="theta" radius={0.75} />
          <Axis name="percent" />
          <Tooltip
            showTitle={false}
            itemTpl="<li><span style=&quot;background-color:{color};&quot; class=&quot;g2-tooltip-marker&quot;></span>{name}: {value}</li>"
          />
          <Geom
            type="intervalStack"
            position="percent"
            active
            color={['item', (item) => {
				switch (item) {
				case 'Correct Answer':
					return '#00ff00';
				case 'Wrong Answer':
					return '#ff1a1a';
				case 'Not Answer':
					return '#00bfff';
				}
			}]}
            tooltip={['item*percent', (item, percent) => {
			percent = `${Math.round(percent * 100)}%`;
			return { name: item, value: percent };
}]}
            style={{ lineWidth: 1, stroke: '#fff' }}
          >
            <Label content="percent" formatter={(val, item) => `${item.point.item}: ${val}`} />
          </Geom>
        </Chart>
      </Modal>
    </div>);
  }
}
export default withRouter(connect(null)(TestResultChart));
