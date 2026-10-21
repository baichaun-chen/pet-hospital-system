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
import com.huacai.hospital.domain.Reservation;
import com.huacai.hospital.service.IReservationService;
import com.huacai.system.general.utils.poi.ExcelUtil;
import com.huacai.system.general.core.page.TableDataInfo;

/**
 * 预约Controller
 *
 * @author huacai
 * @date 2025-10-27
 */
@RestController
@RequestMapping("/hospital/reservation")
public class ReservationController extends BaseController {
    @Autowired
    private IReservationService reservationService;

    /**
     * 查询预约列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Reservation reservation) {
        startPage();
        List<Reservation> list = reservationService.selectReservationList(reservation);
        return getDataTable(list);
    }

    /**
     * 查询个人的所有预约列表
     */
    @GetMapping("/selectMyReservationList")
    public AjaxResult selectMyReservationList() {
        Reservation reservation = new Reservation();
        reservation.setUserId(getUserId());
        List<Reservation> list = reservationService.selectReservationList(reservation);
        return success(list);
    }

    /**
     * 导出预约列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Reservation reservation) {
        List<Reservation> list = reservationService.selectReservationList(reservation);
        ExcelUtil<Reservation> util = new ExcelUtil<Reservation>(Reservation. class);
        util.exportExcel(response, list, "预约数据");
    }

    /**
     * 下载模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Reservation> util = new ExcelUtil<Reservation>(Reservation. class);
        util.importTemplateExcel(response, "预约数据");
    }

    /**
     * 导入数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<Reservation> util = new ExcelUtil<Reservation>(Reservation. class);
        InputStream inputStream = file.getInputStream();
        List<Reservation> list = util.importExcel(inputStream);
        inputStream.close();
        int count = reservationService.batchInsertReservation(list);
        return AjaxResult.success("导入成功" + count + "条信息！");
    }

    /**
     * 获取预约详细信息
     */
    @GetMapping(value = "/{reservationId}")
    public AjaxResult getInfo(@PathVariable("reservationId") String reservationId) {
        return success(reservationService.selectReservationByReservationId(reservationId));
    }

    /**
     * 新增预约
     */
    @PostMapping
    public AjaxResult add(@RequestBody Reservation reservation) {
        return toAjax(reservationService.insertReservation(reservation));
    }

    /**
     * 修改预约
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Reservation reservation) {
        return toAjax(reservationService.updateReservation(reservation));
    }

    /**
     * 删除预约
     */
    @DeleteMapping("/{reservationIds}")
    public AjaxResult remove(@PathVariable String[] reservationIds) {
        return toAjax(reservationService.deleteReservationByReservationIds(reservationIds));
    }
}
