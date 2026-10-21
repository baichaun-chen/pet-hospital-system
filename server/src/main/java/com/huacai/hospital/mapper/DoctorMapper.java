package com.huacai.hospital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.hospital.domain.Doctor;

/**
 * 医生Mapper接口
 *
 * @author huacai
 * @date 2025-10-27
 */
@Mapper
public interface DoctorMapper
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
     * 修改医生
     *
     * @param doctor 医生
     * @return 结果
     */
    public int updateDoctor(Doctor doctor);

    /**
     * 删除医生
     *
     * @param doctorId 医生主键
     * @return 结果
     */
    public int deleteDoctorByDoctorId(String doctorId);

    /**
     * 批量删除医生
     *
     * @param doctorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDoctorByDoctorIds(String[] doctorIds);
}
