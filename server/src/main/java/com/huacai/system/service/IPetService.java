package com.huacai.hospital.service;

import java.util.List;
import com.huacai.system.domain.Pet;

/**
 * 宠物档案Service接口
 *
 * @author huacai
 * @date 2026-02-15
 */
public interface IPetService
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
     * 批量新增宠物档案
     *
     * @param pets 宠物档案List
     * @return 结果
     */
    public int batchInsertPet(List<Pet> pets);

    /**
     * 修改宠物档案
     *
     * @param pet 宠物档案
     * @return 结果
     */
    public int updatePet(Pet pet);

    /**
     * 批量删除宠物档案
     *
     * @param petIds 需要删除的宠物档案主键集合
     * @return 结果
     */
    public int deletePetByPetIds(Long[] petIds);

    /**
     * 删除宠物档案信息
     *
     * @param petId 宠物档案主键
     * @return 结果
     */
    public int deletePetByPetId(Long petId);
}
