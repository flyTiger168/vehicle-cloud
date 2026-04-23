import request from './request';

const BASE = '/api/v1/vehicles';

export function listVehicles(page = 0, pageSize = 10) {
  return request.get(BASE, { params: { page, pageSize } });
}

export function getVehicle(id) {
  return request.get(`${BASE}/${id}`);
}

export function createVehicle(data) {
  return request.post(BASE, data);
}

export function deleteVehicle(id) {
  return request.delete(`${BASE}/${id}`);
}
