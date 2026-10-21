package com.huacai.hospital.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
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
import com.huacai.system.domain.Pet;
import com.huacai.hospital.service.IPetService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 宠物档案Controller
 *
 * @author huacai
 * @date 2026-02-15
 */
@RestController
@RequestMapping("/hospital/pet")
public class PetController extends BaseController {
    @Autowired
    private IPetService petService;

    /**
     * 查询宠物档案列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Pet pet) {
        startPage();
        List<Pet> list = petService.selectPetList(pet);
        return getDataTable(list);
    }

    /**
     * 导出宠物档案列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pet pet) {
        List<Pet> list = petService.selectPetList(pet);
        ExcelUtil<Pet> util = new ExcelUtil<Pet>(Pet. class);
        util.exportExcel(response, list, "宠物档案数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Pet> util = new ExcelUtil<Pet>(Pet. class);
        util.importTemplateExcel(response, "宠物档案数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Pet> util = new ExcelUtil<Pet>(Pet. class);
        InputStream inputStream = file.getInputStream();
        List<Pet> list = util.importExcel(inputStream);
        inputStream.close();
        int count = petService.batchInsertPet(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取宠物档案详细信息
     */
    @GetMapping(value = "/{petId}")
    public AjaxResult getInfo(@PathVariable("petId") Long petId) {
        return success(petService.selectPetByPetId(petId));
    }

    /**
     * 新增宠物档案
     */
    @PostMapping
    public AjaxResult add(@RequestBody Pet pet) {
        return toAjax(petService.insertPet(pet));
    }

    /**
     * 修改宠物档案
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Pet pet) {
        return toAjax(petService.updatePet(pet));
    }

    /**
     * 删除宠物档案
     */
    @DeleteMapping("/{petIds}")
    public AjaxResult remove(@PathVariable Long[] petIds) {
        return toAjax(petService.deletePetByPetIds(petIds));
    }
}
