package com.huacai.hospital.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
import com.huacai.hospital.domain.MedicalRecord;
import com.huacai.hospital.service.IMedicalRecordService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 病历记录Controller
 *
 * @author huacai
 * @date 2026-02-20
 */
@RestController
@RequestMapping("/hospital/record")
public class MedicalRecordController extends BaseController {
    @Autowired
    private IMedicalRecordService medicalRecordService;

    /**
     * 查询病历记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MedicalRecord medicalRecord) {
        startPage();
        List<MedicalRecord> list = medicalRecordService.selectMedicalRecordList(medicalRecord);
        return getDataTable(list);
    }

    /**
     * 导出病历记录列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, MedicalRecord medicalRecord) {
        List<MedicalRecord> list = medicalRecordService.selectMedicalRecordList(medicalRecord);
        ExcelUtil<MedicalRecord> util = new ExcelUtil<MedicalRecord>(MedicalRecord. class);
        util.exportExcel(response, list, "病历记录数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<MedicalRecord> util = new ExcelUtil<MedicalRecord>(MedicalRecord. class);
        util.importTemplateExcel(response, "病历记录数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<MedicalRecord> util = new ExcelUtil<MedicalRecord>(MedicalRecord. class);
        InputStream inputStream = file.getInputStream();
        List<MedicalRecord> list = util.importExcel(inputStream);
        inputStream.close();
        int count = medicalRecordService.batchInsertMedicalRecord(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取病历记录详细信息
     */
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId) {
        return success(medicalRecordService.selectMedicalRecordByRecordId(recordId));
    }

    /**
     * 新增病历记录
     */
    @PostMapping
    public AjaxResult add(@RequestBody MedicalRecord medicalRecord) {
        return toAjax(medicalRecordService.insertMedicalRecord(medicalRecord));
    }

    /**
     * 修改病历记录
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MedicalRecord medicalRecord) {
        return toAjax(medicalRecordService.updateMedicalRecord(medicalRecord));
    }

    /**
     * 删除病历记录
     */
    @DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds) {
        return toAjax(medicalRecordService.deleteMedicalRecordByRecordIds(recordIds));
    }
}
