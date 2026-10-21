package com.huacai.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 病历记录对象 medical_record
 *
 * @author huacai
 * @date 2026-02-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 病历ID */
    private Long recordId;

    /** 关联预约ID */
    @Excel(name = "关联预约ID")
    private Long reservationId;

    /** 医生ID（冗余） */
    @Excel(name = "医生ID", readConverterExp = "冗=余")
    private Long doctorId;

    /** 诊断结果 */
    @Excel(name = "诊断结果")
    private String diagnosis;

    /** 治疗方案 */
    @Excel(name = "治疗方案")
    private String treatment;

    /** 处方药 */
    @Excel(name = "处方药")
    private String prescription;

    /** 建议复诊日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "建议复诊日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date followUpDate;


}
