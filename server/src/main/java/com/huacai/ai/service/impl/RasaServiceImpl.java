package com.huacai.ai.service.impl;

import com.huacai.ai.service.IRasaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RasaServiceImpl implements IRasaService {

    private static final Logger log = LoggerFactory.getLogger(RasaServiceImpl.class);

    @Value("${rasa.url}")
    private String rasaUrl;

    /** 注入 RestTemplateConfig 中带连接/读取超时的实例，不要自建无超时模板 */
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<String> getResponse(String sessionId, String message) {
        // 构造Rasa请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("sender", sessionId);
        requestBody.put("message", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 调用Rasa webhook
            Map[] response = restTemplate.postForObject(rasaUrl, entity, Map[].class);
            List<String> replies = new ArrayList<>();
            if (response != null) {
                for (Map msg : response) {
                    if (msg.containsKey("text")) {
                        replies.add((String) msg.get("text"));
                    }
                }
            }
            return replies;
        } catch (Exception e) {
            log.error("调用 Rasa 失败", e);
            return Collections.singletonList("服务繁忙，请稍后再试。");
        }
    }
}
