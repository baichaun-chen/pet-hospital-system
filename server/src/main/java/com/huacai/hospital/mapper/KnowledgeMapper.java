package com.huacai.hospital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.huacai.hospital.domain.Knowledge;

/**
 * 养宠知识Mapper接口
 *
 * @author huacai
 * @date 2025-10-29
 */
@Mapper
public interface KnowledgeMapper
{
    /**
     * 查询养宠知识
     *
     * @param knowledgeId 养宠知识主键
     * @return 养宠知识
     */
    public Knowledge selectKnowledgeByKnowledgeId(String knowledgeId);

    /**
     * 查询养宠知识列表
     *
     * @param knowledge 养宠知识
     * @return 养宠知识集合
     */
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge);

    /**
     * 新增养宠知识
     *
     * @param knowledge 养宠知识
     * @return 结果
     */
    public int insertKnowledge(Knowledge knowledge);

    /**
     * 修改养宠知识
     *
     * @param knowledge 养宠知识
     * @return 结果
     */
    public int updateKnowledge(Knowledge knowledge);

    /**
     * 删除养宠知识
     *
     * @param knowledgeId 养宠知识主键
     * @return 结果
     */
    public int deleteKnowledgeByKnowledgeId(String knowledgeId);

    /**
     * 批量删除养宠知识
     *
     * @param knowledgeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKnowledgeByKnowledgeIds(String[] knowledgeIds);
}
