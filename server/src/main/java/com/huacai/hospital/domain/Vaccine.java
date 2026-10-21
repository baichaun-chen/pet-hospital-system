package com.huacai.hospital.domain;

import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 疫苗对象 vaccine
 *
 * @author huacai
 * @date 2025-10-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vaccine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 疫苗ID */
    private String vaccineId;

    /** 疫苗名称 */
    @Excel(name = "疫苗名称")
    private String name;

    /** 描述 */
    @Excel(name = "描述")
    private String description;


}
