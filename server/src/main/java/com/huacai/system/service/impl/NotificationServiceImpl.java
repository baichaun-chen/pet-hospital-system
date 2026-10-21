package com.huacai.hospital.service.impl;

import java.util.List;
import com.huacai.system.general.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.NotificationMapper;
import com.huacai.hospital.domain.Notification;
import com.huacai.hospital.service.INotificationService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 消息通知Service业务层处理
 *
 * @author huacai
 * @date 2026-02-20
 */
@Service
public class NotificationServiceImpl implements INotificationService
{
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询消息通知
     *
     * @param notifyId 消息通知主键
     * @return 消息通知
     */
    @Override
    public Notification selectNotificationByNotifyId(Long notifyId)
    {
        return notificationMapper.selectNotificationByNotifyId(notifyId);
    }

    /**
     * 查询消息通知列表
     *
     * @param notification 消息通知
     * @return 消息通知
     */
    @Override
    public List<Notification> selectNotificationList(Notification notification)
    {
        return notificationMapper.selectNotificationList(notification);
    }

    /**
     * 新增消息通知
     *
     * @param notification 消息通知
     * @return 结果
     */
    @Override
    public int insertNotification(Notification notification)
    {
        notification.setCreateTime(DateUtils.getNowDate());
        return notificationMapper.insertNotification(notification);
    }

    /**
     * 批量新增消息通知
     *
     * @param notifications 消息通知List
     * @return 结果
     */
    @Override
    public int batchInsertNotification(List<Notification> notifications)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(notifications)) {
            try {
                for (int i = 0; i < notifications.size(); i++) {
                    int row = notificationMapper.insertNotification(notifications.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == notifications.size() - 1;
                    if (bool){
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            }catch (Exception e){
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            }finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改消息通知
     *
     * @param notification 消息通知
     * @return 结果
     */
    @Override
    public int updateNotification(Notification notification)
    {
        return notificationMapper.updateNotification(notification);
    }

    /**
     * 批量删除消息通知
     *
     * @param notifyIds 需要删除的消息通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationByNotifyIds(Long[] notifyIds)
    {
        return notificationMapper.deleteNotificationByNotifyIds(notifyIds);
    }

    /**
     * 删除消息通知信息
     *
     * @param notifyId 消息通知主键
     * @return 结果
     */
    @Override
    public int deleteNotificationByNotifyId(Long notifyId)
    {
        return notificationMapper.deleteNotificationByNotifyId(notifyId);
    }
}
