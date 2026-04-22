import { Modal, Form, Input, message } from 'antd';
import { createUser } from '../api/userApi';

function UserForm({ open, onCancel, onSuccess }) {
  const [form] = Form.useForm();

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await createUser(values);
      message.success('用户创建成功');
      form.resetFields();
      onSuccess();
    } catch (_) {
      // 校验失败或接口报错
    }
  };

  return (
    <Modal
      title="新增用户"
      open={open}
      onOk={handleOk}
      onCancel={() => { form.resetFields(); onCancel(); }}
      okText="确认"
      cancelText="取消"
      destroyOnClose
    >
      <Form form={form} layout="vertical">
        <Form.Item
          name="name"
          label="姓名"
          rules={[{ required: true, message: '请输入姓名' }]}
        >
          <Input placeholder="请输入姓名" />
        </Form.Item>
        <Form.Item
          name="phone"
          label="手机号"
          rules={[
            { required: true, message: '请输入手机号' },
            { pattern: /^1\d{10}$/, message: '手机号格式不正确' },
          ]}
        >
          <Input placeholder="11 位手机号" maxLength={11} />
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default UserForm;
