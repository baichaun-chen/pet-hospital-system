package com.huacai.ai.controller;

import com.huacai.ai.service.IRasaService;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/chat")
@CrossOrigin(origins = "*")
public class ChatController extends BaseController {

    @Autowired
    private IRasaService rasaService;

    /**
     * 获取AI助手回复
     * 支持小程序游客模式 + 未来登录用户
     */
    @PostMapping("/message")
    public AjaxResult sendMessage(@RequestBody Map<String, String> payload, HttpSession session) {
        // 从请求体中提取 message 参数
        String message = payload.get("message");
        if (message == null || message.trim().isEmpty()) {
            return AjaxResult.error("消息不能为空");
        }

        // 【关键修改】优先使用小程序主动传递的 sender（支持游客模式）
        String sender = payload.get("sender");

        // 如果小程序没传 sender，才使用 HttpSession（兼容其他调用方式）
        if (sender == null || sender.trim().isEmpty()) {
            sender = (String) session.getAttribute("rasa_session_id");
            if (sender == null) {
                sender = "user_" + System.currentTimeMillis();
                session.setAttribute("rasa_session_id", sender);
            }
        }

        // 调用Rasa获取回复
        List<String> responses = rasaService.getResponse(sender, message);

        // 返回统一格式
        return AjaxResult.success(responses);
    }
}