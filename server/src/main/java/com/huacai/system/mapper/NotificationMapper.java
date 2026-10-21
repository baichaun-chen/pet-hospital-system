package com.huacai.hospital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.hospital.domain.Notification;

/**
 * 消息通知Mapper接口
 *
 * @author huacai
 * @date 2026-02-20
 */
@Mapper
public interface NotificationMapper
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
     * 修改消息通知
     *
     * @param notification 消息通知
     * @return 结果
     */
    public int updateNotification(Notification notification);

    /**
     * 删除消息通知
     *
     * @param notifyId 消息通知主键
     * @return 结果
     */
    public int deleteNotificationByNotifyId(Long notifyId);

    /**
     * 批量删除消息通知
     *
     * @param notifyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNotificationByNotifyIds(Long[] notifyIds);
}
