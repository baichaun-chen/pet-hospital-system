package com.huacai.hospital.controller;

import com.huacai.hospital.domain.Doctor;
import com.huacai.hospital.domain.Reservation;
import com.huacai.hospital.domain.Vaccine;
import com.huacai.hospital.domain.vo.HomePageCountVO;
import com.huacai.hospital.domain.vo.PieVO;
import com.huacai.hospital.domain.vo.ReservationChartVO;
import com.huacai.hospital.service.IDoctorService;
import com.huacai.hospital.service.IReservationService;
import com.huacai.hospital.service.IVaccineService;
import com.huacai.system.domain.SysUser;
import com.huacai.system.general.core.controller.BaseController;
import com.huacai.system.general.core.domain.AjaxResult;
import com.huacai.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台首页Controller
 */
@RestController
@RequestMapping("/home/page")
public class HomePageController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private IReservationService reservationService;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IVaccineService vaccineService;

    /**
     * 查询总用户数 预约数 医生数 疫苗数
     */
    @GetMapping("/selectHomeCount")
    public AjaxResult selectHomeCount() {
        //总用户数
        int userCount = userService.selectUserList(new SysUser()).size();
        //预约数
        int reservationCount = reservationService.selectReservationList(new Reservation()).size();
        //医生数
        int doctorCount = doctorService.selectDoctorList(new Doctor()).size();
        //疫苗数
        int vaccineCount = vaccineService.selectVaccineList(new Vaccine()).size();

        //插入至后台首页数据统计VO对象中
        HomePageCountVO homePageCountVO = new HomePageCountVO();
        homePageCountVO.setUserCount(userCount);
        homePageCountVO.setReservationCount(reservationCount);
        homePageCountVO.setDoctorCount(doctorCount);
        homePageCountVO.setVaccineCount(vaccineCount);

        return AjaxResult.success(homePageCountVO);
    }

    /**
     * 查询近7天预约统计数据
     */
    @GetMapping("/selectReservationStat")
    public AjaxResult selectReservationStat() {
        ReservationChartVO reservationChartVO = new ReservationChartVO();
        //存储日期列表
        ArrayList<String> dateList = new ArrayList<>();
        //存储预约数量列表
        ArrayList<Integer> countList = new ArrayList<>();
        //获取当前日期
        LocalDate today = LocalDate.now();

        //查询近7天的数据(包括今天)
        for (int i = 6; i >= 0; i--) {
            //计算从6天前到今天的每一天
            LocalDate date = today.minusDays(i);
            //格式化日期为MM-dd格式用于显示在图表X轴
            String dateString = date.format(DateTimeFormatter.ofPattern("MM-dd"));
            //创建预约查询条件对象
            Reservation reservation = new Reservation();
            //设置查询日期
            reservation.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            //查询指定日期的预约记录数量
            int count = reservationService.selectReservationList(reservation).size();
            //将日期和对应的预约数量添加到列表中
            dateList.add(dateString);
            countList.add(count);
        }
        //将日期列表和预约数量列表设置到vo对象中
        reservationChartVO.setDate(dateList);
        reservationChartVO.setCount(countList);
        return AjaxResult.success(reservationChartVO);
    }

    /**
     * 预约状态统计
     */
    @GetMapping("/selectReservationStatusChart")
    public AjaxResult selectReservationStatusChart() {
        List<PieVO> pieVOList = reservationService.selectCategoryChart();
        return AjaxResult.success(pieVOList);
    }

}
