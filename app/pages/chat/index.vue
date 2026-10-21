<template>
    <view class="container">
        <!-- 聊天消息区域 -->
        <scroll-view
          class="chat-container"
          scroll-y
          :scroll-top="scrollTop"
          scroll-with-animation
        >
            <view class="message-list">
                <!-- 欢迎消息 -->
                <view v-if="messages.length === 0" class="welcome-message">
                    <view class="welcome-icon">👋</view>
                    <text class="welcome-title">您好！我是AI宠物问诊助手</text>
                    <text class="welcome-desc">我可以为您的宠物提供初步的健康咨询和症状分析</text>
                </view>

                <!-- 聊天消息 -->
                <view
                  v-for="(message, index) in messages"
                  :key="index"
                  :class="['message', message.type === 'user' ? 'user-message' : 'ai-message']"
                >
                    <view class="message-avatar">
                        <view v-if="message.type === 'user'" class="avatar user-avatar">👤</view>
                        <view v-else class="avatar ai-avatar">🤖</view>
                    </view>
                    <view class="message-content-wrapper">
                        <view class="message-content">
                            <text class="message-text">{{ message.content }}</text>
                            <!-- 当消息类型为AI且正在加载时，显示打字动画 -->
                            <view v-if="message.type === 'ai' && isLoading && index === messages.length - 1"
                                  class="typing-indicator">
                                <text>AI正在输入</text>
                                <view class="typing-dots">
                                    <view class="typing-dot"></view>
                                    <view class="typing-dot"></view>
                                    <view class="typing-dot"></view>
                                </view>
                            </view>
                        </view>
                        <text class="message-time">{{ message.time }}</text>
                    </view>
                </view>
            </view>
        </scroll-view>

        <!-- 输入区域 -->
        <view class="input-container">
            <view class="input-wrapper">
                <textarea
                  v-model="inputMessage"
                  :maxlength="500"
                  placeholder="请描述您的宠物的症状或健康问题..."
                  :disabled="isLoading"
                  @confirm="sendMessage"
                  class="custom-input"
                  :style="{height: textareaHeight + 'rpx'}"
                  @linechange="handleLineChange"
                />
                <view>
                    <button
                      @click="sendMessage"
                      :disabled="isLoading || !inputMessage.trim()"
                      class="send-button"
                      :class="{ 'disabled': isLoading || !inputMessage.trim() }"
                    >
                        <text v-if="!isLoading">发送</text>
                        <view v-else class="loading-spinner"></view>
                    </button>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup>
import { ref, nextTick, onBeforeUnmount } from 'vue'
// ✅ 修改为相对路径，根据项目结构：pages/chat/index.vue 和 pages/api/hospital/chat.js 的相对关系
import { sendChatMessage } from "../api/hospital/chat";

// 响应式数据
const inputMessage = ref('')
const messages = ref([])
const isLoading = ref(false)
const scrollTop = ref(0)
const textareaHeight = ref(80)

// 发送消息
const sendMessage = async () => {
    if (!inputMessage.value.trim() || isLoading.value) return

    // 添加用户消息到聊天记录
    const userMessage = {
        type: 'user',
        content: inputMessage.value,
        time: getCurrentTime()
    }
    messages.value.push(userMessage)

    // 清空输入框
    const messageToSend = inputMessage.value
    inputMessage.value = ''
    textareaHeight.value = 80

    // 滚动到底部
    scrollToBottom()

    try {
        isLoading.value = true

        // 添加一个空的AI消息占位
        const aiMessage = {
            type: 'ai',
            content: '',
            time: getCurrentTime()
        }
        messages.value.push(aiMessage)

        // 调用 POST 接口
        const response = await sendChatMessage(messageToSend)
        // 假设返回格式为 { code, msg, data }，data 是字符串列表
        if (response.code === 200) {
            const replies = response.data
            // 将多条回复合并，用换行分隔
            const lastMessage = messages.value[messages.value.length - 1]
            lastMessage.content = replies.join('\n')
            lastMessage.time = getCurrentTime()
        } else {
            throw new Error(response.msg || '未知错误')
        }
    } catch (error) {
        console.error('发送消息失败:', error)
        // 显示错误消息
        const errorMessage = {
            type: 'ai',
            content: '抱歉，服务暂时不可用，请稍后再试。',
            time: getCurrentTime()
        }
        messages.value.push(errorMessage)
    } finally {
        isLoading.value = false
        scrollToBottom()
    }
}

// 处理文本域行数变化
const handleLineChange = (e) => {
    const lineCount = e.detail.lineCount
    textareaHeight.value = Math.min(Math.max(80, lineCount * 40), 160)
}

// 滚动到底部
const scrollToBottom = () => {
    nextTick(() => {
        scrollTop.value = 99999
    })
}

// 获取当前时间
const getCurrentTime = () => {
    const now = new Date()
    return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
}

// 组件卸载前无需清理 SSE，所以移除 onBeforeUnmount 中的逻辑
// 如果保留 onBeforeUnmount 可留空
</script>

<style scoped>
/* 样式部分保持不变，与您提供的完全相同，此处省略以节省篇幅，实际使用时保留原样式 */
.container {
    display: flex;
    flex-direction: column;
    height: 93vh;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
}
/* 头部样式 */
.header {
    background: linear-gradient(135deg, #4a90e2 0%, #63b3ed 100%);
    color: white;
    padding: 30rpx;
    text-align: center;
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
}

.header-title {
    font-size: 36rpx;
    font-weight: bold;
    display: block;
}

.header-subtitle {
    font-size: 24rpx;
    opacity: 0.9;
    display: block;
    margin-top: 10rpx;
}

/* 聊天区域样式 */
.chat-container {
    flex: 1;
    padding: 20rpx;
    overflow: auto;
}

.message-list {
    display: flex;
    flex-direction: column;
}

/* 欢迎消息样式 */
.welcome-message {
    text-align: center;
    padding: 60rpx 30rpx;
    color: #495057;
}

.welcome-icon {
    font-size: 80rpx;
    margin-bottom: 30rpx;
}

.welcome-title {
    font-size: 36rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 20rpx;
}

.welcome-desc {
    font-size: 28rpx;
    display: block;
    line-height: 1.5;
    max-width: 500rpx;
    margin: 0 auto;
}

/* 消息样式 */
.message {
    display: flex;
    margin-bottom: 40rpx;
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20rpx);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.message-avatar {
    margin: 0 20rpx;
}

.avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 36rpx;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.user-avatar {
    background: linear-gradient(135deg, #3498db, #2980b9);
    color: white;
}

.ai-avatar {
    background: linear-gradient(135deg, #9b59b6, #8e44ad);
    color: white;
}

.message-content-wrapper {
    max-width: 70%;
}

.message-content {
    padding: 24rpx 32rpx;
    border-radius: 32rpx;
    position: relative;
    box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.08);
}

.user-message .message-content {
    background: linear-gradient(135deg, #3498db, #2980b9);
    color: white;
    border-bottom-right-radius: 8rpx;
}

.ai-message .message-content {
    background: #ffffff;
    color: #495057;
    border-bottom-left-radius: 8rpx;
    border: 1rpx solid #eef0f5;
}

.message-text {
    line-height: 1.6;
    font-size: 28rpx;
    white-space: pre-wrap;
    word-wrap: break-word;
}

.message-time {
    font-size: 22rpx;
    color: #adb5bd;
    margin-top: 12rpx;
    display: block;
}

.user-message .message-time {
    text-align: right;
}

/* 打字指示器 */
.typing-indicator {
    display: flex;
    align-items: center;
    margin-top: 16rpx;
    color: #666;
    font-size: 24rpx;
}

.typing-dots {
    display: flex;
    margin-left: 16rpx;
}

.typing-dot {
    width: 12rpx;
    height: 12rpx;
    border-radius: 50%;
    background-color: #666;
    margin: 0 4rpx;
    animation: typing 1.4s infinite ease-in-out;
}

.typing-dot:nth-child(1) {
    animation-delay: -0.32s;
}

.typing-dot:nth-child(2) {
    animation-delay: -0.16s;
}

@keyframes typing {
    0%, 80%, 100% {
        transform: scale(0.8);
        opacity: 0.5;
    }
    40% {
        transform: scale(1);
        opacity: 1;
    }
}

/* 输入区域样式 */
.input-container {
    background: white;
    border-top: 1rpx solid #e0e0e0;
    padding: 20rpx;
}

.input-wrapper {
    display: flex;
    flex-direction: column;
    border: 1rpx solid #e0e0e0;
    border-radius: 20rpx;
    background-color: #f8f9fa;
    padding: 10rpx;
}

.custom-input {
    width: 100%;
    background-color: transparent;
    border-radius: 10rpx;
    padding: 20rpx;
    font-size: 28rpx;
    border: none;
    box-sizing: border-box;
    resize: none;
}

.custom-input:focus {
    outline: none;
}

.send-button {
    background: linear-gradient(135deg, #3498db, #2980b9);
    color: white;
    border-radius: 10rpx;
    padding: 15rpx 30rpx;
    font-size: 26rpx;
    font-weight: 600;
    border: none;
    box-shadow: 0 4rpx 16rpx rgba(52, 152, 219, 0.3);
    transition: all 0.3s ease;
    min-width: 120rpx;
    display: flex;
    align-items: center;
    justify-content: center;
}

.send-button:active:not(.disabled) {
    transform: translateY(2rpx);
    box-shadow: 0 2rpx 8rpx rgba(52, 152, 219, 0.3);
}

.send-button.disabled {
    background: #bdc3c7;
    box-shadow: none;
    transform: none;
}

.loading-spinner {
    width: 32rpx;
    height: 32rpx;
    border: 4rpx solid transparent;
    border-top: 4rpx solid white;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}
</style>
