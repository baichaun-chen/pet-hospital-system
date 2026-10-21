package com.huacai.hospital.controller;

import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger log = LoggerFactory.getLogger(ChatRasaController.class);

    private static final int MAX_MESSAGE_LENGTH = 1000;

    @Value("${rasa.url}")
    private String rasaUrl;

    /** 使用带连接/读取超时的容器内 Bean，避免自建 RestTemplate 无超时导致线程被拖住 */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 发送消息到 Rasa 并获取回答。Rasa 会话标识由登录态推导，不接受客户端传入的 userId。
     */
    @PostMapping("/message")
    public AjaxResult sendMessage(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");

        if (message == null || message.trim().isEmpty()) {
            return AjaxResult.error("参数缺失");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            return AjaxResult.error("消息过长，请分段发送");
        }

        Map<String, String> rasaRequest = new HashMap<>();
        rasaRequest.put("sender", "user_" + getUserId());
        rasaRequest.put("message", message);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> requestEntity =
                    new HttpEntity<>(rasaRequest, headers);

            Map[] response = restTemplate.postForObject(rasaUrl, requestEntity, Map[].class);

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
            log.error("调用 Rasa 失败", e);
            return AjaxResult.error("咨询繁忙，请稍后再试");
        }
    }
}
