import request from './request';

const BASE = '/api/v1/users';

export function listUsers() {
  return request.get(BASE);
}

export function getUser(id) {
  return request.get(`${BASE}/${id}`);
}

export function createUser(data) {
  return request.post(BASE, data);
}

export function deleteUser(id) {
  return request.delete(`${BASE}/${id}`);
}
