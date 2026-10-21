package com.huacai.ai.service;

import java.util.List;

public interface IRasaService {
    /**
     * 获取Rasa回复
     * @param sessionId 会话ID（保持上下文）
     * @param message 用户消息
     * @return 回复列表（通常1条，可能多条）
     */
    List<String> getResponse(String sessionId, String message);
}