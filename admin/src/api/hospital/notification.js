import request from '@/utils/request'

// 查询消息通知列表
export function listNotification(query) {
  return request({
    url: '/hospital/notification/list',
    method: 'get',
    params: query
  })
}

// 查询消息通知详细
export function getNotification(notifyId) {
  return request({
    url: '/hospital/notification/' + notifyId,
    method: 'get'
  })
}

// 新增消息通知
export function addNotification(data) {
  return request({
    url: '/hospital/notification',
    method: 'post',
    data: data
  })
}

// 修改消息通知
export function updateNotification(data) {
  return request({
    url: '/hospital/notification',
    method: 'put',
    data: data
  })
}

// 删除消息通知
export function delNotification(notifyId) {
  return request({
    url: '/hospital/notification/' + notifyId,
    method: 'delete'
  })
}
