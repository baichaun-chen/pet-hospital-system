// 文件路径：pages/api/hospital/chat.js

// 发送消息并获取回复（非流式 POST）
export function sendChatMessage(message) {
    return new Promise((resolve, reject) => {
        uni.request({
            url: 'http://localhost:8080/ai/chat/message', // 请根据实际后端地址修改
            method: 'POST',
            data: { message },
            success: (res) => {
                if (res.data && res.data.code === 200) {
                    resolve(res.data);
                } else {
                    reject(new Error(res.data?.msg || '请求失败'));
                }
            },
            fail: (err) => {
                reject(err);
            }
        });
    });
}

// 如果还需要流式接口，可以保留 sseChat
export function sseChat(memoryId, message) {
    const baseUrl = 'http://localhost:8080'; // 请根据实际后端地址修改
    return new Promise((resolve) => {
        const eventSource = new EventSource(`${baseUrl}/ai/chat/sse?message=${message}&memoryId=${memoryId}`);
        resolve(eventSource);
    });
}