package com.huacai.hospital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.system.domain.Pet;

/**
 * 宠物档案Mapper接口
 *
 * @author huacai
 * @date 2026-02-15
 */
@Mapper
public interface PetMapper
{
    /**
     * 查询宠物档案
     *
     * @param petId 宠物档案主键
     * @return 宠物档案
     */
    public Pet selectPetByPetId(Long petId);

    /**
     * 查询宠物档案列表
     *
     * @param pet 宠物档案
     * @return 宠物档案集合
     */
    public List<Pet> selectPetList(Pet pet);

    /**
     * 新增宠物档案
     *
     * @param pet 宠物档案
     * @return 结果
     */
    public int insertPet(Pet pet);

    /**
     * 修改宠物档案
     *
     * @param pet 宠物档案
     * @return 结果
     */
    public int updatePet(Pet pet);

    /**
     * 删除宠物档案
     *
     * @param petId 宠物档案主键
     * @return 结果
     */
    public int deletePetByPetId(Long petId);

    /**
     * 批量删除宠物档案
     *
     * @param petIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePetByPetIds(Long[] petIds);
}
