package com.huacai.hospital.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.DoctorMapper;
import com.huacai.hospital.domain.Doctor;
import com.huacai.hospital.service.IDoctorService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 医生Service业务层处理
 *
 * @author huacai
 * @date 2025-10-27
 */
@Service
public class DoctorServiceImpl implements IDoctorService {
    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询医生
     *
     * @param doctorId 医生主键
     * @return 医生
     */
    @Override
    public Doctor selectDoctorByDoctorId(String doctorId) {
        return doctorMapper.selectDoctorByDoctorId(doctorId);
    }

    /**
     * 查询医生列表
     *
     * @param doctor 医生
     * @return 医生
     */
    @Override
    public List<Doctor> selectDoctorList(Doctor doctor) {
        return doctorMapper.selectDoctorList(doctor);
    }

    /**
     * 新增医生
     *
     * @param doctor 医生
     * @return 结果
     */
    @Override
    public int insertDoctor(Doctor doctor) {
        doctor.setCreateTime(DateUtils.getNowDate());
        //生成一个UUID插入至医生对象中
        doctor.setDoctorId(IdUtils.fastSimpleUUID());
        return doctorMapper.insertDoctor(doctor);
    }

    /**
     * 批量新增医生
     *
     * @param doctors 医生List
     * @return 结果
     */
    @Override
    public int batchInsertDoctor(List<Doctor> doctors) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(doctors)) {
            try {
                for (int i = 0; i < doctors.size(); i++) {
                    int row = doctorMapper.insertDoctor(doctors.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == doctors.size() - 1;
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
     * 修改医生
     *
     * @param doctor 医生
     * @return 结果
     */
    @Override
    public int updateDoctor(Doctor doctor) {
        return doctorMapper.updateDoctor(doctor);
    }

    /**
     * 批量删除医生
     *
     * @param doctorIds 需要删除的医生主键
     * @return 结果
     */
    @Override
    public int deleteDoctorByDoctorIds(String[] doctorIds) {
        return doctorMapper.deleteDoctorByDoctorIds(doctorIds);
    }

    /**
     * 删除医生信息
     *
     * @param doctorId 医生主键
     * @return 结果
     */
    @Override
    public int deleteDoctorByDoctorId(String doctorId) {
        return doctorMapper.deleteDoctorByDoctorId(doctorId);
    }
}
