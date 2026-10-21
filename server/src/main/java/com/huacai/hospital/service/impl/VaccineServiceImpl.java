package com.huacai.hospital.service.impl;

import java.util.List;

import com.huacai.system.general.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.huacai.hospital.mapper.VaccineMapper;
import com.huacai.hospital.domain.Vaccine;
import com.huacai.hospital.service.IVaccineService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.CollectionUtils;

/**
 * 疫苗Service业务层处理
 *
 * @author huacai
 * @date 2025-10-27
 */
@Service
public class VaccineServiceImpl implements IVaccineService {
    @Autowired
    private VaccineMapper vaccineMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 查询疫苗
     *
     * @param vaccineId 疫苗主键
     * @return 疫苗
     */
    @Override
    public Vaccine selectVaccineByVaccineId(String vaccineId) {
        return vaccineMapper.selectVaccineByVaccineId(vaccineId);
    }

    /**
     * 查询疫苗列表
     *
     * @param vaccine 疫苗
     * @return 疫苗
     */
    @Override
    public List<Vaccine> selectVaccineList(Vaccine vaccine) {
        return vaccineMapper.selectVaccineList(vaccine);
    }

    /**
     * 新增疫苗
     *
     * @param vaccine 疫苗
     * @return 结果
     */
    @Override
    public int insertVaccine(Vaccine vaccine) {
        //生成一个UUID并插入至疫苗对象中
        vaccine.setVaccineId(IdUtils.fastSimpleUUID());
        return vaccineMapper.insertVaccine(vaccine);
    }

    /**
     * 批量新增疫苗
     *
     * @param vaccines 疫苗List
     * @return 结果
     */
    @Override
    public int batchInsertVaccine(List<Vaccine> vaccines) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        int count = 0;
        if (!CollectionUtils.isEmpty(vaccines)) {
            try {
                for (int i = 0; i < vaccines.size(); i++) {
                    int row = vaccineMapper.insertVaccine(vaccines.get(i));
                    // 防止内存溢出，每100次提交一次,并清除缓存
                    boolean bool = (i > 0 && i % 100 == 0) || i == vaccines.size() - 1;
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
     * 修改疫苗
     *
     * @param vaccine 疫苗
     * @return 结果
     */
    @Override
    public int updateVaccine(Vaccine vaccine) {
        return vaccineMapper.updateVaccine(vaccine);
    }

    /**
     * 批量删除疫苗
     *
     * @param vaccineIds 需要删除的疫苗主键
     * @return 结果
     */
    @Override
    public int deleteVaccineByVaccineIds(String[] vaccineIds) {
        return vaccineMapper.deleteVaccineByVaccineIds(vaccineIds);
    }

    /**
     * 删除疫苗信息
     *
     * @param vaccineId 疫苗主键
     * @return 结果
     */
    @Override
    public int deleteVaccineByVaccineId(String vaccineId) {
        return vaccineMapper.deleteVaccineByVaccineId(vaccineId);
    }
}
