package com.huacai.hospital.service.impl;

import java.util.List;

import com.huacai.system.general.utils.DateUtils;
import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.KnowledgeMapper;
import com.huacai.hospital.domain.Knowledge;
import com.huacai.hospital.service.IKnowledgeService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 养宠知识Service业务层处理
 *
 * @author huacai
 * @date 2025-10-29
 */
@Service
public class KnowledgeServiceImpl implements IKnowledgeService {
    @Autowired
    private KnowledgeMapper knowledgeMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询养宠知识
     *
     * @param knowledgeId 养宠知识主键
     * @return 养宠知识
     */
    @Override
    public Knowledge selectKnowledgeByKnowledgeId(String knowledgeId) {
        return knowledgeMapper.selectKnowledgeByKnowledgeId(knowledgeId);
    }

    /**
     * 查询养宠知识列表
     *
     * @param knowledge 养宠知识
     * @return 养宠知识
     */
    @Override
    public List<Knowledge> selectKnowledgeList(Knowledge knowledge) {
        return knowledgeMapper.selectKnowledgeList(knowledge);
    }

    /**
     * 新增养宠知识
     *
     * @param knowledge 养宠知识
     * @return 结果
     */
    @Override
    public int insertKnowledge(Knowledge knowledge) {
        knowledge.setCreateTime(DateUtils.getNowDate());
        //生成一个新的UUID并插入至养宠知识对象中
        knowledge.setKnowledgeId(IdUtils.fastSimpleUUID());
        return knowledgeMapper.insertKnowledge(knowledge);
    }

    /**
     * 批量新增养宠知识
     *
     * @param knowledges 养宠知识List
     * @return 结果
     */
    @Override
    public int batchInsertKnowledge(List<Knowledge> knowledges) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(knowledges)) {
            try {
                for (int i = 0; i < knowledges.size(); i++) {
                    int row = knowledgeMapper.insertKnowledge(knowledges.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == knowledges.size() - 1;
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
     * 修改养宠知识
     *
     * @param knowledge 养宠知识
     * @return 结果
     */
    @Override
    public int updateKnowledge(Knowledge knowledge) {
        return knowledgeMapper.updateKnowledge(knowledge);
    }

    /**
     * 批量删除养宠知识
     *
     * @param knowledgeIds 需要删除的养宠知识主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeByKnowledgeIds(String[] knowledgeIds) {
        return knowledgeMapper.deleteKnowledgeByKnowledgeIds(knowledgeIds);
    }

    /**
     * 删除养宠知识信息
     *
     * @param knowledgeId 养宠知识主键
     * @return 结果
     */
    @Override
    public int deleteKnowledgeByKnowledgeId(String knowledgeId) {
        return knowledgeMapper.deleteKnowledgeByKnowledgeId(knowledgeId);
    }
}
