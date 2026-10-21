package com.huacai.hospital.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.huacai.hospital.domain.vo.PieVO;
import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.SecurityUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.ReservationMapper;
import com.huacai.hospital.domain.Reservation;
import com.huacai.hospital.service.IReservationService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

import static com.huacai.system.general.utils.SecurityUtils.getUserId;


/**
 * 预约Service业务层处理
 *
 * @author huacai
 * @date 2025-10-27
 */
@Service
public class ReservationServiceImpl implements IReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询预约
     *
     * @param reservationId 预约主键
     * @return 预约
     */
    @Override
    public Reservation selectReservationByReservationId(String reservationId) {
        return reservationMapper.selectReservationByReservationId(reservationId);
    }

    /**
     * 查询预约列表
     *
     * @param reservation 预约
     * @return 预约
     */
    @Override
    public List<Reservation> selectReservationList(Reservation reservation) {
        return reservationMapper.selectReservationList(reservation);
    }

    /**
     * 新增预约
     *
     * @param reservation 预约
     * @return 结果
     */
    @Override
    public int insertReservation(Reservation reservation) {
        reservation.setCreateTime(DateUtils.getNowDate());
        //获取当前操作用户ID并插入至预约对象中
        reservation.setUserId(getUserId());

        //获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        //定义格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        //将日期时间格式化为字符串
        String formatDteTime = now.format(formatter);
        //将预约单号插入至预约对象中
        reservation.setReservationId("OR" + formatDteTime + getUserId());
        return reservationMapper.insertReservation(reservation);
    }

    /**
     * 批量新增预约
     *
     * @param reservations 预约List
     * @return 结果
     */
    @Override
    public int batchInsertReservation(List<Reservation> reservations) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(reservations)) {
            try {
                for (int i = 0; i < reservations.size(); i++) {
                    int row = reservationMapper.insertReservation(reservations.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == reservations.size() - 1;
                    if (bool) {
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            } finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改预约
     *
     * @param reservation 预约
     * @return 结果
     */
    @Override
    public int updateReservation(Reservation reservation) {
        return reservationMapper.updateReservation(reservation);
    }

    /**
     * 批量删除预约
     *
     * @param reservationIds 需要删除的预约主键
     * @return 结果
     */
    @Override
    public int deleteReservationByReservationIds(String[] reservationIds) {
        return reservationMapper.deleteReservationByReservationIds(reservationIds);
    }

    /**
     * 删除预约信息
     *
     * @param reservationId 预约主键
     * @return 结果
     */
    @Override
    public int deleteReservationByReservationId(String reservationId) {
        return reservationMapper.deleteReservationByReservationId(reservationId);
    }

    /**
     * 预约状态统计
     * @return
     */
    @Override
    public List<PieVO> selectCategoryChart() {
        return reservationMapper.selectCategoryChart();
    }
}
