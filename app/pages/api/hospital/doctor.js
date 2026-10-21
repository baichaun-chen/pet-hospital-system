import request from '@/utils/request'

// 查询医生列表
export function listDoctor(query) {
  return request({
    url: '/hospital/doctor/list',
    method: 'get',
    params: query
  })
}

// 查询医生详细
export function getDoctor(doctorId) {
  return request({
    url: '/hospital/doctor/' + doctorId,
    method: 'get'
  })
}

// 新增医生
export function addDoctor(data) {
  return request({
    url: '/hospital/doctor',
    method: 'post',
    data: data
  })
}

// 修改医生
export function updateDoctor(data) {
  return request({
    url: '/hospital/doctor',
    method: 'put',
    data: data
  })
}

// 删除医生
export function delDoctor(doctorId) {
  return request({
    url: '/hospital/doctor/' + doctorId,
    method: 'delete'
  })
}
