import request from '@/utils/request'

// 查询养宠知识列表
export function listKnowledge(query) {
  return request({
    url: '/hospital/knowledge/list',
    method: 'get',
    params: query
  })
}

// 查询养宠知识详细
export function getKnowledge(knowledgeId) {
  return request({
    url: '/hospital/knowledge/' + knowledgeId,
    method: 'get'
  })
}

// 新增养宠知识
export function addKnowledge(data) {
  return request({
    url: '/hospital/knowledge',
    method: 'post',
    data: data
  })
}

// 修改养宠知识
export function updateKnowledge(data) {
  return request({
    url: '/hospital/knowledge',
    method: 'put',
    data: data
  })
}

// 删除养宠知识
export function delKnowledge(knowledgeId) {
  return request({
    url: '/hospital/knowledge/' + knowledgeId,
    method: 'delete'
  })
}
