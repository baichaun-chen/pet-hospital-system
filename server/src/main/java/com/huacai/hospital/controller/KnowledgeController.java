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
import com.huacai.hospital.domain.Knowledge;
import com.huacai.hospital.service.IKnowledgeService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 养宠知识Controller
 *
 * @author huacai
 * @date 2025-10-29
 */
@RestController
@RequestMapping("/hospital/knowledge")
public class KnowledgeController extends BaseController {
    @Autowired
    private IKnowledgeService knowledgeService;

    /**
     * 查询养宠知识列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Knowledge knowledge) {
        startPage();
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        return getDataTable(list);
    }

    /**
     * 导出养宠知识列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Knowledge knowledge) {
        List<Knowledge> list = knowledgeService.selectKnowledgeList(knowledge);
        ExcelUtil<Knowledge> util = new ExcelUtil<Knowledge>(Knowledge. class);
        util.exportExcel(response, list, "养宠知识数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Knowledge> util = new ExcelUtil<Knowledge>(Knowledge. class);
        util.importTemplateExcel(response, "养宠知识数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Knowledge> util = new ExcelUtil<Knowledge>(Knowledge. class);
        InputStream inputStream = file.getInputStream();
        List<Knowledge> list = util.importExcel(inputStream);
        inputStream.close();
        int count = knowledgeService.batchInsertKnowledge(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取养宠知识详细信息
     */
    @GetMapping(value = "/{knowledgeId}")
    public AjaxResult getInfo(@PathVariable("knowledgeId") String knowledgeId) {
        return success(knowledgeService.selectKnowledgeByKnowledgeId(knowledgeId));
    }

    /**
     * 新增养宠知识
     */
    @PostMapping
    public AjaxResult add(@RequestBody Knowledge knowledge) {
        return toAjax(knowledgeService.insertKnowledge(knowledge));
    }

    /**
     * 修改养宠知识
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Knowledge knowledge) {
        return toAjax(knowledgeService.updateKnowledge(knowledge));
    }

    /**
     * 删除养宠知识
     */
    @DeleteMapping("/{knowledgeIds}")
    public AjaxResult remove(@PathVariable String[] knowledgeIds) {
        return toAjax(knowledgeService.deleteKnowledgeByKnowledgeIds(knowledgeIds));
    }
}
