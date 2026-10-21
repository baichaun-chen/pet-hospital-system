package com.huacai.hospital.service.impl;

import java.util.List;
import com.huacai.system.general.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.PetMapper;
import com.huacai.system.domain.Pet;
import com.huacai.hospital.service.IPetService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 宠物档案Service业务层处理
 *
 * @author huacai
 * @date 2026-02-15
 */
@Service
public class PetServiceImpl implements IPetService
{
    @Autowired
    private PetMapper petMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询宠物档案
     *
     * @param petId 宠物档案主键
     * @return 宠物档案
     */
    @Override
    public Pet selectPetByPetId(Long petId)
    {
        return petMapper.selectPetByPetId(petId);
    }

    /**
     * 查询宠物档案列表
     *
     * @param pet 宠物档案
     * @return 宠物档案
     */
    @Override
    public List<Pet> selectPetList(Pet pet)
    {
        return petMapper.selectPetList(pet);
    }

    /**
     * 新增宠物档案
     *
     * @param pet 宠物档案
     * @return 结果
     */
    @Override
    public int insertPet(Pet pet)
    {
        pet.setCreateTime(DateUtils.getNowDate());
        return petMapper.insertPet(pet);
    }

    /**
     * 批量新增宠物档案
     *
     * @param pets 宠物档案List
     * @return 结果
     */
    @Override
    public int batchInsertPet(List<Pet> pets)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(pets)) {
            try {
                for (int i = 0; i < pets.size(); i++) {
                    int row = petMapper.insertPet(pets.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == pets.size() - 1;
                    if (bool){
                        sqlSession.commit();
                        sqlSession.clearCache();
                    }
                    count = i + 1;
                }
            }catch (Exception e){
                e.printStackTrace();
                // 没有提交的数据可以回滚
                sqlSession.rollback();
            }finally {
                sqlSession.close();
                return count;
            }
        }
        return count;
    }

    /**
     * 修改宠物档案
     *
     * @param pet 宠物档案
     * @return 结果
     */
    @Override
    public int updatePet(Pet pet)
    {
        return petMapper.updatePet(pet);
    }

    /**
     * 批量删除宠物档案
     *
     * @param petIds 需要删除的宠物档案主键
     * @return 结果
     */
    @Override
    public int deletePetByPetIds(Long[] petIds)
    {
        return petMapper.deletePetByPetIds(petIds);
    }

    /**
     * 删除宠物档案信息
     *
     * @param petId 宠物档案主键
     * @return 结果
     */
    @Override
    public int deletePetByPetId(Long petId)
    {
        return petMapper.deletePetByPetId(petId);
    }
}
