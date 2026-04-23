import { useState } from 'react';
import { Routes, Route, Link, useLocation } from 'react-router-dom';
import { Layout, Menu, theme } from 'antd';
import {
  DashboardOutlined,
  CarOutlined,
  UserOutlined,
} from '@ant-design/icons';
import Dashboard from './pages/Dashboard';
import VehicleList from './pages/VehicleList';
import UserList from './pages/UserList';

const { Header, Sider, Content } = Layout;

const menuItems = [
  { key: '/', icon: <DashboardOutlined />, label: <Link to="/">仪表盘</Link> },
  { key: '/vehicles', icon: <CarOutlined />, label: <Link to="/vehicles">车辆管理</Link> },
  { key: '/users', icon: <UserOutlined />, label: <Link to="/users">用户管理</Link> },
];

function App() {
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();
  const { token: { colorBgContainer, borderRadiusLG } } = theme.useToken();

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <div style={{
          height: 32,
          margin: 16,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
        }}>
          <CarOutlined style={{ color: '#fff', fontSize: 24, marginRight: collapsed ? 0 : 8 }} />
          {!collapsed && (
            <span style={{ color: '#fff', fontSize: 16, fontWeight: 'bold', whiteSpace: 'nowrap' }}>
              车联网云平台
            </span>
          )}
        </div>
        <Menu
          theme="dark"
          selectedKeys={[location.pathname]}
          mode="inline"
          items={menuItems}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 24px', background: colorBgContainer, display: 'flex', alignItems: 'center' }}>
          <span style={{ fontSize: 18, fontWeight: 500 }}>AITO 车联网管理后台</span>
        </Header>
        <Content style={{ margin: 16 }}>
          <div style={{
            padding: 24,
            minHeight: 360,
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
          }}>
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/vehicles" element={<VehicleList />} />
              <Route path="/users" element={<UserList />} />
            </Routes>
          </div>
        </Content>
      </Layout>
    </Layout>
  );
}

export default App;
