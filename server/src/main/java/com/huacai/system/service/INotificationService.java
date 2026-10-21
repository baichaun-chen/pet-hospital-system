package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.Notification;

/**
 * 消息通知Service接口
 *
 * @author huacai
 * @date 2026-02-20
 */
public interface INotificationService
{
    /**
     * 查询消息通知
     *
     * @param notifyId 消息通知主键
     * @return 消息通知
     */
    public Notification selectNotificationByNotifyId(Long notifyId);

    /**
     * 查询消息通知列表
     *
     * @param notification 消息通知
     * @return 消息通知集合
     */
    public List<Notification> selectNotificationList(Notification notification);

    /**
     * 新增消息通知
     *
     * @param notification 消息通知
     * @return 结果
     */
    public int insertNotification(Notification notification);

    /**
     * 批量新增消息通知
     *
     * @param notifications 消息通知List
     * @return 结果
     */
    public int batchInsertNotification(List<Notification> notifications);

    /**
     * 修改消息通知
     *
     * @param notification 消息通知
     * @return 结果
     */
    public int updateNotification(Notification notification);

    /**
     * 批量删除消息通知
     *
     * @param notifyIds 需要删除的消息通知主键集合
     * @return 结果
     */
    public int deleteNotificationByNotifyIds(Long[] notifyIds);

    /**
     * 删除消息通知信息
     *
     * @param notifyId 消息通知主键
     * @return 结果
     */
    public int deleteNotificationByNotifyId(Long notifyId);
}
