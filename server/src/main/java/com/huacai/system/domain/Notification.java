package com.huacai.hospital.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 消息通知对象 notification
 *
 * @author huacai
 * @date 2026-02-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 通知ID */
    private Long notifyId;

    /** 接收用户ID */
    @Excel(name = "接收用户ID")
    private Long userId;

    /** 通知标题 */
    @Excel(name = "通知标题")
    private String title;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String content;

    /** 通知类型（0=系统通知 1=预约提醒 2=疫苗到期） */
    @Excel(name = "通知类型", readConverterExp = "0==系统通知,1==预约提醒,2==疫苗到期")
    private Integer type;

    /** 是否已读（0=未读 1=已读） */
    @Excel(name = "是否已读", readConverterExp = "0==未读,1==已读")
    private Integer isRead;


}
