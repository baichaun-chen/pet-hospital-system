package com.huacai.hospital.mapper;

import java.util.List;

import com.huacai.hospital.domain.vo.PieVO;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.hospital.domain.Reservation;

/**
 * 预约Mapper接口
 *
 * @author huacai
 * @date 2025-10-27
 */
@Mapper
public interface ReservationMapper
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
     * 修改预约
     *
     * @param reservation 预约
     * @return 结果
     */
    public int updateReservation(Reservation reservation);

    /**
     * 删除预约
     *
     * @param reservationId 预约主键
     * @return 结果
     */
    public int deleteReservationByReservationId(String reservationId);

    /**
     * 批量删除预约
     *
     * @param reservationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteReservationByReservationIds(String[] reservationIds);

    /**
     * 预约状态统计
     * @return
     */
    List<PieVO> selectCategoryChart();
}
