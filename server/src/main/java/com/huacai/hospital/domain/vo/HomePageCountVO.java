package com.huacai.hospital.domain.vo;

import lombok.Data;

/**
 * 后台首页统计数据VO
 */
@Data
public class HomePageCountVO {
    // 总用户数
    private Integer userCount;
    // 预约数
    private Integer reservationCount;
    // 医生数
    private Integer doctorCount;
    // 疫苗数
    private Integer vaccineCount;
}
