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
import com.huacai.hospital.domain.Doctor;
import com.huacai.hospital.service.IDoctorService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 医生Controller
 *
 * @author huacai
 * @date 2025-10-27
 */
@RestController
@RequestMapping("/hospital/doctor")
public class DoctorController extends BaseController {
    @Autowired
    private IDoctorService doctorService;

    /**
     * 查询医生列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Doctor doctor) {
        startPage();
        List<Doctor> list = doctorService.selectDoctorList(doctor);
        return getDataTable(list);
    }

    /**
     * 导出医生列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Doctor doctor) {
        List<Doctor> list = doctorService.selectDoctorList(doctor);
        ExcelUtil<Doctor> util = new ExcelUtil<Doctor>(Doctor. class);
        util.exportExcel(response, list, "医生数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Doctor> util = new ExcelUtil<Doctor>(Doctor. class);
        util.importTemplateExcel(response, "医生数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Doctor> util = new ExcelUtil<Doctor>(Doctor. class);
        InputStream inputStream = file.getInputStream();
        List<Doctor> list = util.importExcel(inputStream);
        inputStream.close();
        int count = doctorService.batchInsertDoctor(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取医生详细信息
     */
    @GetMapping(value = "/{doctorId}")
    public AjaxResult getInfo(@PathVariable("doctorId") String doctorId) {
        return success(doctorService.selectDoctorByDoctorId(doctorId));
    }

    /**
     * 新增医生
     */
    @PostMapping
    public AjaxResult add(@RequestBody Doctor doctor) {
        return toAjax(doctorService.insertDoctor(doctor));
    }

    /**
     * 修改医生
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Doctor doctor) {
        return toAjax(doctorService.updateDoctor(doctor));
    }

    /**
     * 删除医生
     */
    @DeleteMapping("/{doctorIds}")
    public AjaxResult remove(@PathVariable String[] doctorIds) {
        return toAjax(doctorService.deleteDoctorByDoctorIds(doctorIds));
    }
}
