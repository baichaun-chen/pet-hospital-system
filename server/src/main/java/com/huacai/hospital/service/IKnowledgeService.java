package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.Knowledge;

/**
 * 养宠知识Service接口
 *
 * @author huacai
 * @date 2025-10-29
 */
public interface IKnowledgeService
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
     * 批量新增养宠知识
     *
     * @param knowledges 养宠知识List
     * @return 结果
     */
    public int batchInsertKnowledge(List<Knowledge> knowledges);

    /**
     * 修改养宠知识
     *
     * @param knowledge 养宠知识
     * @return 结果
     */
    public int updateKnowledge(Knowledge knowledge);

    /**
     * 批量删除养宠知识
     *
     * @param knowledgeIds 需要删除的养宠知识主键集合
     * @return 结果
     */
    public int deleteKnowledgeByKnowledgeIds(String[] knowledgeIds);

    /**
     * 删除养宠知识信息
     *
     * @param knowledgeId 养宠知识主键
     * @return 结果
     */
    public int deleteKnowledgeByKnowledgeId(String knowledgeId);
}
