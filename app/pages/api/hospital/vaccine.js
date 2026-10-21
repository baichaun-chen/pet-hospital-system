import request from '@/utils/request'

// 查询疫苗列表
export function listVaccine(query) {
  return request({
    url: '/hospital/vaccine/list',
    method: 'get',
    params: query
  })
}

// 查询疫苗详细
export function getVaccine(vaccineId) {
  return request({
    url: '/hospital/vaccine/' + vaccineId,
    method: 'get'
  })
}

// 新增疫苗
export function addVaccine(data) {
  return request({
    url: '/hospital/vaccine',
    method: 'post',
    data: data
  })
}

// 修改疫苗
export function updateVaccine(data) {
  return request({
    url: '/hospital/vaccine',
    method: 'put',
    data: data
  })
}

// 删除疫苗
export function delVaccine(vaccineId) {
  return request({
    url: '/hospital/vaccine/' + vaccineId,
    method: 'delete'
  })
}
