package com.huacai.hospital.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import com.huacai.hospital.domain.Notification;
import com.huacai.hospital.service.INotificationService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 消息通知Controller
 *
 * @author huacai
 * @date 2026-02-20
 */
@RestController
@RequestMapping("/hospital/notification")
public class NotificationController extends BaseController {
    @Autowired
    private INotificationService notificationService;

    /**
     * 查询消息通知列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Notification notification) {
        startPage();
        List<Notification> list = notificationService.selectNotificationList(notification);
        return getDataTable(list);
    }

    /**
     * 导出消息通知列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Notification notification) {
        List<Notification> list = notificationService.selectNotificationList(notification);
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification. class);
        util.exportExcel(response, list, "消息通知数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification. class);
        util.importTemplateExcel(response, "消息通知数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Notification> util = new ExcelUtil<Notification>(Notification. class);
        InputStream inputStream = file.getInputStream();
        List<Notification> list = util.importExcel(inputStream);
        inputStream.close();
        int count = notificationService.batchInsertNotification(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取消息通知详细信息
     */
    @GetMapping(value = "/{notifyId}")
    public AjaxResult getInfo(@PathVariable("notifyId") Long notifyId) {
        return success(notificationService.selectNotificationByNotifyId(notifyId));
    }

    /**
     * 新增消息通知
     */
    @PostMapping
    public AjaxResult add(@RequestBody Notification notification) {
        return toAjax(notificationService.insertNotification(notification));
    }

    /**
     * 修改消息通知
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Notification notification) {
        return toAjax(notificationService.updateNotification(notification));
    }

    /**
     * 删除消息通知
     */
    @DeleteMapping("/{notifyIds}")
    public AjaxResult remove(@PathVariable Long[] notifyIds) {
        return toAjax(notificationService.deleteNotificationByNotifyIds(notifyIds));
    }
}
