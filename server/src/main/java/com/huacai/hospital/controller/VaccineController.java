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
import com.huacai.hospital.domain.Vaccine;
import com.huacai.hospital.service.IVaccineService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 疫苗Controller
 *
 * @author huacai
 * @date 2025-10-27
 */
@RestController
@RequestMapping("/hospital/vaccine")
public class VaccineController extends BaseController {
    @Autowired
    private IVaccineService vaccineService;

    /**
     * 查询疫苗列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Vaccine vaccine) {
        startPage();
        List<Vaccine> list = vaccineService.selectVaccineList(vaccine);
        return getDataTable(list);
    }

    /**
     * 导出疫苗列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Vaccine vaccine) {
        List<Vaccine> list = vaccineService.selectVaccineList(vaccine);
        ExcelUtil<Vaccine> util = new ExcelUtil<Vaccine>(Vaccine. class);
        util.exportExcel(response, list, "疫苗数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Vaccine> util = new ExcelUtil<Vaccine>(Vaccine. class);
        util.importTemplateExcel(response, "疫苗数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Vaccine> util = new ExcelUtil<Vaccine>(Vaccine. class);
        InputStream inputStream = file.getInputStream();
        List<Vaccine> list = util.importExcel(inputStream);
        inputStream.close();
        int count = vaccineService.batchInsertVaccine(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取疫苗详细信息
     */
    @GetMapping(value = "/{vaccineId}")
    public AjaxResult getInfo(@PathVariable("vaccineId") String vaccineId) {
        return success(vaccineService.selectVaccineByVaccineId(vaccineId));
    }

    /**
     * 新增疫苗
     */
    @PostMapping
    public AjaxResult add(@RequestBody Vaccine vaccine) {
        return toAjax(vaccineService.insertVaccine(vaccine));
    }

    /**
     * 修改疫苗
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Vaccine vaccine) {
        return toAjax(vaccineService.updateVaccine(vaccine));
    }

    /**
     * 删除疫苗
     */
    @DeleteMapping("/{vaccineIds}")
    public AjaxResult remove(@PathVariable String[] vaccineIds) {
        return toAjax(vaccineService.deleteVaccineByVaccineIds(vaccineIds));
    }
}
