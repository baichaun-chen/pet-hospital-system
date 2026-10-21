package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.Reservation;
import com.huacai.hospital.domain.vo.PieVO;

/**
 * 预约Service接口
 *
 * @author huacai
 * @date 2025-10-27
 */
public interface IReservationService
{
    /**
     * 查询预约
     *
     * @param reservationId 预约主键
     * @return 预约
     */
    public Reservation selectReservationByReservationId(String reservationId);

    /**
     * 查询预约列表
     *
     * @param reservation 预约
     * @return 预约集合
     */
    public List<Reservation> selectReservationList(Reservation reservation);

    /**
     * 新增预约
     *
     * @param reservation 预约
     * @return 结果
     */
    public int insertReservation(Reservation reservation);

    /**
     * 批量新增预约
     *
     * @param reservations 预约List
     * @return 结果
     */
    public int batchInsertReservation(List<Reservation> reservations);

    /**
     * 修改预约
     *
     * @param reservation 预约
     * @return 结果
     */
    public int updateReservation(Reservation reservation);

    /**
     * 批量删除预约
     *
     * @param reservationIds 需要删除的预约主键集合
     * @return 结果
     */
    public int deleteReservationByReservationIds(String[] reservationIds);

    /**
     * 删除预约信息
     *
     * @param reservationId 预约主键
     * @return 结果
     */
    public int deleteReservationByReservationId(String reservationId);

    /**
     * 预约状态统计
     * @return
     */
    List<PieVO> selectCategoryChart();
}
