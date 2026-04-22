import { Modal, Form, Input, Select, InputNumber, message } from 'antd';
import { createVehicle } from '../api/vehicleApi';

const modelOptions = [
  { label: 'AITO M5', value: 'AITO M5' },
  { label: 'AITO M7', value: 'AITO M7' },
  { label: 'AITO M9', value: 'AITO M9' },
];

function VehicleForm({ open, onCancel, onSuccess }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await createVehicle(values);
      message.success('车辆创建成功');
      form.resetFields();
      onSuccess();
    } catch (_) {
      // 校验失败或接口报错，antd 自动提示
    }
  };

  return (
    <Modal
      title="新增车辆"
      open={open}
      onOk={handleOk}
      onCancel={() => { form.resetFields(); onCancel(); }}
      okText="确认"
      cancelText="取消"
      destroyOnClose
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="vin"
          label="VIN 车辆识别码"
          rules={[
            { required: true, message: '请输入 VIN' },
            { len: 17, message: 'VIN 必须为 17 位' },
          ]}
        >
          <Input placeholder="17 位 VIN 码" maxLength={17} style={{ textTransform: 'uppercase' }} />
        </Form.Item>
        <Form.Item
          name="model"
          label="车型"
          rules={[{ required: true, message: '请选择车型' }]}
        >
          <Select placeholder="请选择车型" options={modelOptions} />
        </Form.Item>
        <Form.Item
          name="ownerUserId"
          label="车主用户 ID"
        >
          <InputNumber placeholder="关联车主 ID" min={1} style={{ width: '100%' }} />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default VehicleForm;
