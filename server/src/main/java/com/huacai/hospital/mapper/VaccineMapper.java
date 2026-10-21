package com.huacai.hospital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.hospital.domain.Vaccine;

/**
 * 疫苗Mapper接口
 *
 * @author huacai
 * @date 2025-10-27
 */
@Mapper
public interface VaccineMapper
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
     * 修改疫苗
     *
     * @param vaccine 疫苗
     * @return 结果
     */
    public int updateVaccine(Vaccine vaccine);

    /**
     * 删除疫苗
     *
     * @param vaccineId 疫苗主键
     * @return 结果
     */
    public int deleteVaccineByVaccineId(String vaccineId);

    /**
     * 批量删除疫苗
     *
     * @param vaccineIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVaccineByVaccineIds(String[] vaccineIds);
}
