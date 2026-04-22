import axios from 'axios';
import { message } from 'antd';

const request = axios.create({
  timeout: 10000,
});

// 响应拦截器：统一解析 ApiResponse
request.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.code === 0) {
      return res.data;
    }
    message.error(res.message || '请求失败');
    return Promise.reject(new Error(res.message));
  },
  (error) => {
    const msg = error.response?.data?.message || error.message || '网络异常';
    message.error(msg);
    return Promise.reject(error);
  }
);

export default request;
