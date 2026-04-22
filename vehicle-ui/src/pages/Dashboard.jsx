import { useEffect, useState } from 'react';
import { Card, Col, Row, Statistic, Table, Tag, Progress } from 'antd';
import { CarOutlined, UserOutlined, DashboardOutlined, ThunderboltOutlined } from '@ant-design/icons';
import { listVehicles } from '../api/vehicleApi';
import { listUsers } from '../api/userApi';
import { getLatestAll } from '../api/statusApi';

const modelColorMap = {
  'AITO M5': '#1677ff',
  'AITO M7': '#52c41a',
  'AITO M9': '#faad14',
};

function Dashboard() {
  const [vehicles, setVehicles] = useState([]);
  const [users, setUsers] = useState([]);
  const [statusList, setStatusList] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let ignore = false;
    Promise.all([listVehicles(), listUsers(), getLatestAll()])
      .then(([vList, uList, sList]) => {
        if (!ignore) {
          setVehicles(vList || []);
          setUsers(uList || []);
          setStatusList(sList || []);
        }
      })
      .finally(() => { if (!ignore) setLoading(false); });
    return () => { ignore = true; };
  }, []);

  // 状态统计
  const avgBattery = statusList.length
    ? (statusList.reduce((sum, s) => sum + (s.batteryLevel || 0), 0) / statusList.length).toFixed(1)
    : 0;
  const lowBatteryCount = statusList.filter((s) => s.batteryLevel < 30).length;

  // 车型统计
  const modelStats = {};
  vehicles.forEach((v) => {
    modelStats[v.model] = (modelStats[v.model] || 0) + 1;
  });

  // 最近 5 辆车
  const recentVehicles = [...vehicles].reverse().slice(0, 5);

  const recentColumns = [
    { title: 'VIN', dataIndex: 'vin', key: 'vin',
      render: (vin) => <span style={{ fontFamily: 'monospace', fontSize: 12 }}>{vin}</span>,
    },
    { title: '车型', dataIndex: 'model', key: 'model',
      render: (model) => <Tag color={model === 'AITO M9' ? 'gold' : model === 'AITO M7' ? 'green' : 'blue'}>{model}</Tag>,
    },
    { title: '车主 ID', dataIndex: 'ownerUserId', key: 'ownerUserId' },
  ];

  const total = vehicles.length || 1;

  return (
    <div>
      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={6}>
          <Card hoverable>
            <Statistic title="车辆总数" value={vehicles.length} prefix={<CarOutlined />} loading={loading} />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic title="用户总数" value={users.length} prefix={<UserOutlined />} loading={loading} />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic title="车型种类" value={Object.keys(modelStats).length} prefix={<DashboardOutlined />} loading={loading} />
          </Card>
        </Col>
        <Col span={6}>
          <Card hoverable>
            <Statistic title="人均车辆" value={users.length ? (vehicles.length / users.length).toFixed(1) : 0} suffix="辆" loading={loading} />
          </Card>
        </Col>
      </Row>

      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={8}>
          <Card hoverable>
            <Statistic title="上报车辆数" value={statusList.length} prefix={<ThunderboltOutlined />} loading={loading} />
          </Card>
        </Col>
        <Col span={8}>
          <Card hoverable>
            <Statistic title="车队平均电量" value={avgBattery} suffix="%" loading={loading} />
          </Card>
        </Col>
        <Col span={8}>
          <Card hoverable>
            <Statistic title="低电量车辆" value={lowBatteryCount} valueStyle={{ color: '#cf1322' }} loading={loading} />
          </Card>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={10}>
          <Card title="车型分布" size="small">
            {Object.entries(modelStats).map(([model, count]) => (
              <div key={model} style={{ marginBottom: 12 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 4 }}>
                  <span>{model}</span>
                  <span style={{ color: '#999' }}>{count} 辆</span>
                </div>
                <Progress
                  percent={Math.round((count / total) * 100)}
                  strokeColor={modelColorMap[model] || '#1677ff'}
                  size="small"
                />
              </div>
            ))}
          </Card>
        </Col>
        <Col span={14}>
          <Card title="最近添加的车辆" size="small">
            <Table
              rowKey="id"
              columns={recentColumns}
              dataSource={recentVehicles}
              pagination={false}
              size="small"
              loading={loading}
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
}

export default Dashboard;
