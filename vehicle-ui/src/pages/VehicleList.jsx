import { useEffect, useState, useCallback } from 'react';
import { Table, Button, Space, Popconfirm, Select, message, Tag } from 'antd';
import { PlusOutlined, DeleteOutlined, ReloadOutlined } from '@ant-design/icons';
import { listVehicles, deleteVehicle } from '../api/vehicleApi';
import VehicleForm from '../components/VehicleForm';

const modelColorMap = {
  'AITO M5': 'blue',
  'AITO M7': 'green',
  'AITO M9': 'gold',
};

function VehicleList() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formOpen, setFormOpen] = useState(false);
  const [filterModel, setFilterModel] = useState(null);
  const [refreshKey, setRefreshKey] = useState(0);
  const [pagination, setPagination] = useState({ current: 1, pageSize: 10, total: 0 });

  const fetchData = useCallback((page = 0, pageSize = 10) => {
    setLoading(true);
    listVehicles(page, pageSize)
      .then((res) => {
        setData(res.content || []);
        setPagination((prev) => ({
          ...prev,
          current: (res.number ?? 0) + 1,
          pageSize: res.size ?? pageSize,
          total: res.totalElements ?? 0,
        }));
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    fetchData(pagination.current - 1, pagination.pageSize);
  }, [refreshKey]); // eslint-disable-line react-hooks/exhaustive-deps

  const reload = useCallback(() => {
    setRefreshKey((k) => k + 1);
  }, []);

  const handleTableChange = (pag) => {
    setPagination(pag);
    fetchData(pag.current - 1, pag.pageSize);
  };

  const handleDelete = async (id) => {
    await deleteVehicle(id);
    message.success('删除成功');
    reload();
  };

  const filteredData = filterModel
    ? data.filter((v) => v.model === filterModel)
    : data;

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: 'VIN', dataIndex: 'vin', key: 'vin', width: 200,
      render: (vin) => <span style={{ fontFamily: 'monospace' }}>{vin}</span>,
    },
    { title: '车型', dataIndex: 'model', key: 'model', width: 120,
      render: (model) => <Tag color={modelColorMap[model] || 'default'}>{model}</Tag>,
    },
    { title: '车主 ID', dataIndex: 'ownerUserId', key: 'ownerUserId', width: 100 },
    {
      title: '操作', key: 'action', width: 100,
      render: (_, record) => (
        <Popconfirm title="确认删除该车辆？" onConfirm={() => handleDelete(record.id)} okText="确认" cancelText="取消">
          <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
        </Popconfirm>
      ),
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Space>
          <Select
            placeholder="按车型筛选"
            allowClear
            style={{ width: 160 }}
            onChange={(val) => setFilterModel(val)}
            options={[
              { label: 'AITO M5', value: 'AITO M5' },
              { label: 'AITO M7', value: 'AITO M7' },
              { label: 'AITO M9', value: 'AITO M9' },
            ]}
          />
          <Button icon={<ReloadOutlined />} onClick={reload}>刷新</Button>
        </Space>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setFormOpen(true)}>
          新增车辆
        </Button>
      </div>
      <Table
        rowKey="id"
        columns={columns}
        dataSource={filteredData}
        loading={loading}
        pagination={{ ...pagination, showTotal: (total) => `共 ${total} 辆`, showSizeChanger: true }}
        onChange={handleTableChange}
      />
      <VehicleForm
        open={formOpen}
        onCancel={() => setFormOpen(false)}
        onSuccess={() => { setFormOpen(false); reload(); }}
      />
    </div>
  );
}

export default VehicleList;
