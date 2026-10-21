package com.huacai.hospital.service.impl;

import java.util.List;
import com.huacai.system.general.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.MedicalRecordMapper;
import com.huacai.hospital.domain.MedicalRecord;
import com.huacai.hospital.service.IMedicalRecordService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 病历记录Service业务层处理
 *
 * @author huacai
 * @date 2026-02-20
 */
@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService
{
    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询病历记录
     *
     * @param recordId 病历记录主键
     * @return 病历记录
     */
    @Override
    public MedicalRecord selectMedicalRecordByRecordId(Long recordId)
    {
        return medicalRecordMapper.selectMedicalRecordByRecordId(recordId);
    }

    /**
     * 查询病历记录列表
     *
     * @param medicalRecord 病历记录
     * @return 病历记录
     */
    @Override
    public List<MedicalRecord> selectMedicalRecordList(MedicalRecord medicalRecord)
    {
        return medicalRecordMapper.selectMedicalRecordList(medicalRecord);
    }

    /**
     * 新增病历记录
     *
     * @param medicalRecord 病历记录
     * @return 结果
     */
    @Override
    public int insertMedicalRecord(MedicalRecord medicalRecord)
    {
        medicalRecord.setCreateTime(DateUtils.getNowDate());
        return medicalRecordMapper.insertMedicalRecord(medicalRecord);
    }

    /**
     * 批量新增病历记录
     *
     * @param medicalRecords 病历记录List
     * @return 结果
     */
    @Override
    public int batchInsertMedicalRecord(List<MedicalRecord> medicalRecords)
    {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(medicalRecords)) {
            try {
                for (int i = 0; i < medicalRecords.size(); i++) {
                    int row = medicalRecordMapper.insertMedicalRecord(medicalRecords.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i >0 && i%100 == 0) || i == medicalRecords.size() - 1;
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
     * 修改病历记录
     *
     * @param medicalRecord 病历记录
     * @return 结果
     */
    @Override
    public int updateMedicalRecord(MedicalRecord medicalRecord)
    {
        return medicalRecordMapper.updateMedicalRecord(medicalRecord);
    }

    /**
     * 批量删除病历记录
     *
     * @param recordIds 需要删除的病历记录主键
     * @return 结果
     */
    @Override
    public int deleteMedicalRecordByRecordIds(Long[] recordIds)
    {
        return medicalRecordMapper.deleteMedicalRecordByRecordIds(recordIds);
    }

    /**
     * 删除病历记录信息
     *
     * @param recordId 病历记录主键
     * @return 结果
     */
    @Override
    public int deleteMedicalRecordByRecordId(Long recordId)
    {
        return medicalRecordMapper.deleteMedicalRecordByRecordId(recordId);
    }
}
