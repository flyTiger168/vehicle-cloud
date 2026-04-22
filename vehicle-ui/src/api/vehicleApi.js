import request from './request';

const BASE = '/api/v1/vehicles';

export function listVehicles() {
  return request.get(BASE);
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
