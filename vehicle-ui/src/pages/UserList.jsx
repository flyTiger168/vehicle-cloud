import { useEffect, useState, useCallback } from 'react';
import { Table, Button, Popconfirm, message, Modal, Tag } from 'antd';
import { PlusOutlined, DeleteOutlined, ReloadOutlined, CarOutlined } from '@ant-design/icons';
import { listUsers, deleteUser } from '../api/userApi';
import { listVehicles } from '../api/vehicleApi';
import UserForm from '../components/UserForm';

function UserList() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formOpen, setFormOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);
  const [vehicleModal, setVehicleModal] = useState({ open: false, userName: '', vehicles: [] });

  useEffect(() => {
    let ignore = false;
    listUsers()
      .then((list) => { if (!ignore) setData(list || []); })
      .finally(() => { if (!ignore) setLoading(false); });
    return () => { ignore = true; };
  }, [refreshKey]);

  const reload = useCallback(() => {
    setLoading(true);
    setRefreshKey((k) => k + 1);
  }, []);

  const handleDelete = async (id) => {
    await deleteUser(id);
    message.success('删除成功');
    reload();
  };

  const handleShowVehicles = async (record) => {
    const allVehicles = await listVehicles();
    const userVehicles = (allVehicles || []).filter((v) => v.ownerUserId === record.id);
    setVehicleModal({ open: true, userName: record.name, vehicles: userVehicles });
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '姓名', dataIndex: 'name', key: 'name', width: 100 },
    { title: '手机号', dataIndex: 'phone', key: 'phone', width: 140,
      render: (phone) => <span style={{ fontFamily: 'monospace' }}>{phone}</span>,
    },
    {
      title: '操作', key: 'action', width: 200,
      render: (_, record) => (
        <>
          <Button type="link" icon={<CarOutlined />} onClick={() => handleShowVehicles(record)}>
            名下车辆
          </Button>
          <Popconfirm title="确认删除该用户？" onConfirm={() => handleDelete(record.id)} okText="确认" cancelText="取消">
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </>
      ),
    },
  ];

  const vehicleColumns = [
    { title: 'VIN', dataIndex: 'vin', key: 'vin',
      render: (vin) => <span style={{ fontFamily: 'monospace' }}>{vin}</span>,
    },
    { title: '车型', dataIndex: 'model', key: 'model',
      render: (model) => <Tag color={model === 'AITO M9' ? 'gold' : model === 'AITO M7' ? 'green' : 'blue'}>{model}</Tag>,
    },
  ];

  return (
    <div>
      <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between' }}>
        <Button icon={<ReloadOutlined />} onClick={reload}>刷新</Button>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setFormOpen(true)}>
          新增用户
        </Button>
      </div>
      <Table
        rowKey="id"
        columns={columns}
        dataSource={data}
        loading={loading}
        pagination={{ pageSize: 10, showTotal: (total) => `共 ${total} 人` }}
      />
      <UserForm
        open={formOpen}
        onCancel={() => setFormOpen(false)}
        onSuccess={() => { setFormOpen(false); reload(); }}
      />
      <Modal
        title={`${vehicleModal.userName} 的名下车辆`}
        open={vehicleModal.open}
        onCancel={() => setVehicleModal({ open: false, userName: '', vehicles: [] })}
        footer={null}
        width={500}
      >
        {vehicleModal.vehicles.length === 0 ? (
          <p style={{ textAlign: 'center', color: '#999' }}>暂无车辆</p>
        ) : (
          <Table
            rowKey="id"
            columns={vehicleColumns}
            dataSource={vehicleModal.vehicles}
            pagination={false}
            size="small"
          />
        )}
      </Modal>
    </div>
  );
}

export default UserList;
