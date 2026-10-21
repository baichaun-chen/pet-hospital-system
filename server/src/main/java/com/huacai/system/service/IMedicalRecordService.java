package com.huacai.hospital.service;

import java.util.List;
import com.huacai.hospital.domain.MedicalRecord;

/**
 * 病历记录Service接口
 *
 * @author huacai
 * @date 2026-02-20
 */
public interface IMedicalRecordService
{
    /**
     * 查询病历记录
     *
     * @param recordId 病历记录主键
     * @return 病历记录
     */
    public MedicalRecord selectMedicalRecordByRecordId(Long recordId);

    /**
     * 查询病历记录列表
     *
     * @param medicalRecord 病历记录
     * @return 病历记录集合
     */
    public List<MedicalRecord> selectMedicalRecordList(MedicalRecord medicalRecord);

    /**
     * 新增病历记录
     *
     * @param medicalRecord 病历记录
     * @return 结果
     */
    public int insertMedicalRecord(MedicalRecord medicalRecord);

    /**
     * 批量新增病历记录
     *
     * @param medicalRecords 病历记录List
     * @return 结果
     */
    public int batchInsertMedicalRecord(List<MedicalRecord> medicalRecords);

    /**
     * 修改病历记录
     *
     * @param medicalRecord 病历记录
     * @return 结果
     */
    public int updateMedicalRecord(MedicalRecord medicalRecord);

    /**
     * 批量删除病历记录
     *
     * @param recordIds 需要删除的病历记录主键集合
     * @return 结果
     */
    public int deleteMedicalRecordByRecordIds(Long[] recordIds);

    /**
     * 删除病历记录信息
     *
     * @param recordId 病历记录主键
     * @return 结果
     */
    public int deleteMedicalRecordByRecordId(Long recordId);
}
