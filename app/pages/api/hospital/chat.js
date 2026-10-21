// 文件路径：pages/api/hospital/chat.js
import request from '@/utils/request'

// 发送消息并获取 AI 回复。走统一请求封装以携带登录 token，
// 会话标识由后端根据登录用户推导，前端不再传 sender/userId。
export function sendChatMessage(message) {
    return request({
        url: '/ai/chat/message',
        method: 'post',
        data: { message }
    })
}
