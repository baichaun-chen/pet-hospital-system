package com.huacai.ai.controller;

import com.huacai.ai.service.IRasaService;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/chat")
public class ChatController extends BaseController {

    /** 单条消息长度上限，避免超长文本直接透传给 Rasa */
    private static final int MAX_MESSAGE_LENGTH = 1000;

    @Autowired
    private IRasaService rasaService;

    /**
     * 获取AI助手回复。会话标识由登录态推导，不接受客户端传入，
     * 否则任何人只要猜到别人的 sender 就能读写对方的会话上下文。
     */
    @PostMapping("/message")
    public AjaxResult sendMessage(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null || message.trim().isEmpty()) {
            return AjaxResult.error("消息不能为空");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            return AjaxResult.error("消息过长，请分段发送");
        }

        String sender = "user_" + getUserId();
        List<String> responses = rasaService.getResponse(sender, message);

        return AjaxResult.success(responses);
    }
}
