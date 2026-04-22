import request from './request';

const BASE = '/api/v1/status-reports';

export function getLatestAll() {
  return request.get(`${BASE}/latest`);
}

export function getLatestByVin(vin) {
  return request.get(`${BASE}/latest/${vin}`);
}

export function getStatusHistory(params) {
  return request.get(BASE, { params });
}

export function reportStatus(data) {
  return request.post(BASE, data);
}
