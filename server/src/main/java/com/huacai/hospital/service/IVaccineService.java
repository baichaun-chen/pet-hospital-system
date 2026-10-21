package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.Vaccine;

/**
 * 疫苗Service接口
 *
 * @author huacai
 * @date 2025-10-27
 */
public interface IVaccineService
{
    /**
     * 查询疫苗
     *
     * @param vaccineId 疫苗主键
     * @return 疫苗
     */
    public Vaccine selectVaccineByVaccineId(String vaccineId);

    /**
     * 查询疫苗列表
     *
     * @param vaccine 疫苗
     * @return 疫苗集合
     */
    public List<Vaccine> selectVaccineList(Vaccine vaccine);

    /**
     * 新增疫苗
     *
     * @param vaccine 疫苗
     * @return 结果
     */
    public int insertVaccine(Vaccine vaccine);

    /**
     * 批量新增疫苗
     *
     * @param vaccines 疫苗List
     * @return 结果
     */
    public int batchInsertVaccine(List<Vaccine> vaccines);

    /**
     * 修改疫苗
     *
     * @param vaccine 疫苗
     * @return 结果
     */
    public int updateVaccine(Vaccine vaccine);

    /**
     * 批量删除疫苗
     *
     * @param vaccineIds 需要删除的疫苗主键集合
     * @return 结果
     */
    public int deleteVaccineByVaccineIds(String[] vaccineIds);

    /**
     * 删除疫苗信息
     *
     * @param vaccineId 疫苗主键
     * @return 结果
     */
    public int deleteVaccineByVaccineId(String vaccineId);
}
