package com.huacai.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huacai.system.general.annotation.Excel;
import com.huacai.system.general.core.domain.BaseEntity;
import lombok.*;

/**
 * 宠物档案对象 pet
 *
 * @author huacai
 * @date 2026-02-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 宠物ID */
    private Long petId;

    /** 所属用户ID */
    @Excel(name = "所属用户ID")
    private Long userId;

    /** 宠物名字 */
    @Excel(name = "宠物名字")
    private String name;

    /** 物种（狗/猫/兔等） */
    @Excel(name = "物种", readConverterExp = "狗=/猫/兔等")
    private String species;

    /** 品种 */
    @Excel(name = "品种")
    private String breed;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthDate;

    /** 体重（kg） */
    @Excel(name = "体重", readConverterExp = "k=g")
    private BigDecimal weight;

    /** 过敏史 */
    @Excel(name = "过敏史")
    private String allergy;

    /** 头像路径 */
    @Excel(name = "头像路径")
    private String avatar;


}
