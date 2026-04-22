import { useEffect, useState, useCallback } from 'react';
import { Table, Button, Space, Input, Tag, Progress, Modal, Select, message } from 'antd';
import { ReloadOutlined, ThunderboltOutlined } from '@ant-design/icons';
import { getLatestAll, reportStatus } from '../api/statusApi';
import { listVehicles } from '../api/vehicleApi';

function VehicleStatus() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchVin, setSearchVin] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [vehicles, setVehicles] = useState([]);
  const [selectedVin, setSelectedVin] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    let ignore = false;
    getLatestAll()
      .then((list) => { if (!ignore) setData(list || []); })
      .finally(() => { if (!ignore) setLoading(false); });
    return () => { ignore = true; };
  }, [refreshKey]);

  const reload = useCallback(() => {
    setLoading(true);
    setRefreshKey((k) => k + 1);
  }, []);

  const handleOpenModal = useCallback(() => {
    setModalOpen(true);
    listVehicles().then((list) => setVehicles(list || []));
  }, []);

  const handleReport = async () => {
    if (!selectedVin) {
      message.warning('请选择一辆车');
      return;
    }
    const speedOptions = [0, 40, 60, 80, 100];
    const randomData = {
      vin: selectedVin,
      batteryLevel: Math.floor(Math.random() * 76) + 20,
      latitude: +(30.52 + Math.random() * 0.1).toFixed(4),
      longitude: +(104.02 + Math.random() * 0.1).toFixed(4),
      mileage: +(10000 + Math.random() * 40000).toFixed(1),
      speed: speedOptions[Math.floor(Math.random() * speedOptions.length)],
      temperature: +(18 + Math.random() * 17).toFixed(1),
      reportTime: new Date().toISOString(),
    };
    setSubmitting(true);
    try {
      await reportStatus(randomData);
      message.success('模拟上报成功');
      setModalOpen(false);
      setSelectedVin(null);
      reload();
    } finally {
      setSubmitting(false);
    }
  };

  const filteredData = searchVin
    ? data.filter((item) => item.vin && item.vin.toUpperCase().includes(searchVin.toUpperCase()))
    : data;

  const getBatteryColor = (level) => {
    if (level > 60) return '#52c41a';
    if (level >= 30) return '#faad14';
    return '#ff4d4f';
  };

  const columns = [
    {
      title: 'VIN', dataIndex: 'vin', key: 'vin', width: 200,
      render: (vin) => <span style={{ fontFamily: 'monospace' }}>{vin}</span>,
    },
    {
      title: '电量', dataIndex: 'batteryLevel', key: 'batteryLevel', width: 180,
      render: (val) => (
        <Progress
          percent={val}
          size="small"
          strokeColor={getBatteryColor(val)}
          format={(p) => `${p}%`}
        />
      ),
    },
    {
      title: '位置', key: 'location', width: 160,
      render: (_, record) => `${record.latitude}, ${record.longitude}`,
    },
    {
      title: '里程', dataIndex: 'mileage', key: 'mileage', width: 120,
      render: (val) => `${Number(val).toLocaleString('en-US', { minimumFractionDigits: 1, maximumFractionDigits: 1 })} km`,
    },
    {
      title: '车速', dataIndex: 'speed', key: 'speed', width: 100,
      render: (val) => val === 0
        ? <Tag color="default">静止</Tag>
        : `${val} km/h`,
    },
    {
      title: '温度', dataIndex: 'temperature', key: 'temperature', width: 90,
      render: (val) => `${val} ℃`,
    },
    {
      title: '上报时间', dataIndex: 'reportTime', key: 'reportTime', width: 180,
      render: (val) => val ? new Date(val).toLocaleString() : '-',
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Input.Search
            placeholder="输入 VIN 搜索"
            allowClear
            style={{ width: 240 }}
            onSearch={(val) => setSearchVin(val)}
            onChange={(e) => { if (!e.target.value) setSearchVin(''); }}
          />
          <Button icon={<ReloadOutlined />} onClick={reload}>刷新</Button>
        </Space>
        <Button type="primary" icon={<ThunderboltOutlined />} onClick={handleOpenModal}>
          模拟上报
        </Button>
      </div>
      <Table
        rowKey="vin"
        columns={columns}
        dataSource={filteredData}
        loading={loading}
        pagination={{ pageSize: 10, showTotal: (total) => `共 ${total} 条` }}
      />
      <Modal
        title="模拟状态上报"
        open={modalOpen}
        onCancel={() => { setModalOpen(false); setSelectedVin(null); }}
        footer={null}
        destroyOnClose
      >
        <div style={{ marginBottom: 16 }}>
          <Select
            placeholder="选择车辆"
            style={{ width: '100%' }}
            value={selectedVin}
            onChange={(val) => setSelectedVin(val)}
            options={vehicles.map((v) => ({
              label: `${v.vin} - ${v.model}`,
              value: v.vin,
            }))}
            showSearch
            filterOption={(input, option) =>
              option.label.toLowerCase().includes(input.toLowerCase())
            }
          />
        </div>
        <Button type="primary" block loading={submitting} onClick={handleReport}>
          生成并上报
        </Button>
      </Modal>
    </div>
  );
}

export default VehicleStatus;
