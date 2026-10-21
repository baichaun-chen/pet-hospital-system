package com.huacai.hospital.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 预约对象 reservation
 *
 * @author huacai
 * @date 2025-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预约ID */
    private String reservationId;

    /** 预约类型 */
    @Excel(name = "预约类型")
    private String type;

    /** 预约状态 */
    @Excel(name = "预约状态")
    private String status;

    /** 宠物名字 */
    @Excel(name = "宠物名字")
    private String petName;

    /** 宠物品种 */
    @Excel(name = "宠物品种")
    private String petBread;

    /** 宠物年龄 */
    @Excel(name = "宠物年龄")
    private Long petAge;

    /** 症状 */
    @Excel(name = "症状")
    private String symptom;

    /** 预约日期 */
    @Excel(name = "预约日期")
    private String date;

    /** 预约时间 */
    @Excel(name = "预约时间")
    private String time;

    /** 医生ID */
    @Excel(name = "医生ID")
    private String doctorId;

    /** 疫苗ID */
    @Excel(name = "疫苗ID")
    private String vaccineId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    //医生姓名
    private String name;

    //职称
    private String title;

    //擅长
    private String specialty;

    //照片
    private String photo;

    //疫苗名称
    private String vaccineName;

    //预约用户
    private String userName;


}
