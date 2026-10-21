package com.huacai.hospital.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 预约统计VO(用于折线图数据)
 */
@Data
public class ReservationChartVO {
    //日期数组
    private List<String> date;
    //预约数量数组
    private List<Integer> count;
}
