import request from '@/utils/request'

// 查询宠物档案列表
export function listPet(query) {
  return request({
    url: '/hospital/pet/list',
    method: 'get',
    params: query
  })
}

// 查询宠物档案详细
export function getPet(petId) {
  return request({
    url: '/hospital/pet/' + petId,
    method: 'get'
  })
}

// 新增宠物档案
export function addPet(data) {
  return request({
    url: '/hospital/pet',
    method: 'post',
    data: data
  })
}

// 修改宠物档案
export function updatePet(data) {
  return request({
    url: '/hospital/pet',
    method: 'put',
    data: data
  })
}

// 删除宠物档案
export function delPet(petId) {
  return request({
    url: '/hospital/pet/' + petId,
    method: 'delete'
  })
}
