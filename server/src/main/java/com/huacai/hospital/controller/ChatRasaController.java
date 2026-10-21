package com.huacai.hospital.controller;

import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.*;

/**
 * Rasa 聊天机器人接口（宠物健康问答）
 *
 * @author huacai
 */
@RestController
@RequestMapping("/hospital/chat")
public class ChatRasaController extends BaseController {

    private static final String RASA_WEBHOOK_URL = "http://localhost:5005/webhooks/rest/webhook";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 发送消息到 Rasa 并获取回答
     */
    @PostMapping("/message")
    public AjaxResult sendMessage(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String message = payload.get("message");

        if (userId == null || message == null || message.trim().isEmpty()) {
            return AjaxResult.error("参数缺失");
        }

        // 构造 Rasa 请求体
        Map<String, String> rasaRequest = new HashMap<>();
        rasaRequest.put("sender", "user_" + userId); // Rasa 会话跟踪ID
        rasaRequest.put("message", message);

        try {
            // 设置 JSON 请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestEntity =
                    new HttpEntity<>(rasaRequest, headers);

            // 调用 Rasa API
            Map[] response = restTemplate.postForObject(
                    RASA_WEBHOOK_URL,
                    requestEntity,
                    Map[].class
            );

            // 提取所有 text 回复
            List<String> replies = new ArrayList<>();
            if (response != null) {
                for (Map msg : response) {
                    if (msg.containsKey("text")) {
                        replies.add((String) msg.get("text"));
                    }
                }
            }

            return AjaxResult.success(replies);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("咨询繁忙，请稍后再试");
        }
    }
}