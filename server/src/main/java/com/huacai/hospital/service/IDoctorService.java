package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.Doctor;

/**
 * 医生Service接口
 *
 * @author huacai
 * @date 2025-10-27
 */
public interface IDoctorService
{
    /**
     * 查询医生
     *
     * @param doctorId 医生主键
     * @return 医生
     */
    public Doctor selectDoctorByDoctorId(String doctorId);

    /**
     * 查询医生列表
     *
     * @param doctor 医生
     * @return 医生集合
     */
    public List<Doctor> selectDoctorList(Doctor doctor);

    /**
     * 新增医生
     *
     * @param doctor 医生
     * @return 结果
     */
    public int insertDoctor(Doctor doctor);

    /**
     * 批量新增医生
     *
     * @param doctors 医生List
     * @return 结果
     */
    public int batchInsertDoctor(List<Doctor> doctors);

    /**
     * 修改医生
     *
     * @param doctor 医生
     * @return 结果
     */
    public int updateDoctor(Doctor doctor);

    /**
     * 批量删除医生
     *
     * @param doctorIds 需要删除的医生主键集合
     * @return 结果
     */
    public int deleteDoctorByDoctorIds(String[] doctorIds);

    /**
     * 删除医生信息
     *
     * @param doctorId 医生主键
     * @return 结果
     */
    public int deleteDoctorByDoctorId(String doctorId);
}
