package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fanbo
 * @date 2020/8/3 15:21
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    //去年一年的会员数量
    @GetMapping(value = "/getMemberReport")
    public Result getMemberReport(){
        //months
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //过去一年的数据
        calendar.add(Calendar.MONTH,-12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //12个月数据
        for (int i = 0; i <12; i++) {
            calendar.add(Calendar.MONTH,1);
            //日期
            Date date = calendar.getTime();
            //展示年-月
            months.add(sdf.format(date));
        }

        //memberCount
        List<Integer> memberCount = memberService.getMemberReportCount(months);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    //套餐预约饼状图
    @GetMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        List<String> setmealNames = new ArrayList<>();
        List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
        if (setmealCount!=null){
            for (Map<String, Object> map : setmealCount) {
//                向setmealNames中添加name
                setmealNames.add((String) map.get("name"));
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    //运营数据
    @GetMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> report =reportService.getBusinessReport();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,report);
    }

    //导出到excel表
    @GetMapping(value = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        //获取模板路径
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        //创建
        try {
            OutputStream out = response.getOutputStream();
            XSSFWorkbook wb = new XSSFWorkbook(template);
            //获取工作表 第一页
            XSSFSheet sheetAt = wb.getSheetAt(0);
            //获取数据
            Map<String, Object> reportData = reportService.getBusinessReport();

            //赋值到对应行和单元格
            //日期 2,5
            sheetAt.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());
            //新增会员数 4,5
            sheetAt.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            //本周新增会员数 5,5
            sheetAt.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            //总会员数 4,7
            sheetAt.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            //本月新增会员数 5,7
            sheetAt.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));
            //今日预约数 7,5
            sheetAt.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            //本周预约数 8,5
            sheetAt.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            //本月预约数 9,5
            sheetAt.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            //今日到诊数 7,7
            sheetAt.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            //本周到诊数 8,7
            sheetAt.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            //本月到诊数 9,7
            sheetAt.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>)reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> map : hotSetmeal) {
                sheetAt.getRow(row).getCell(4).setCellValue((String)map.get("name"));

                sheetAt.getRow(row).getCell(5).setCellValue((Long)map.get("setmeal_count"));

                BigDecimal proportion = (BigDecimal) map.get("proportion");
                sheetAt.getRow(row).getCell(6).setCellValue(proportion.doubleValue());

                sheetAt.getRow(row).getCell(7).setCellValue((String)map.get("remark"));
                row++;
            }

            //导出文件Content-Type: application/vnd.ms-excel
            response.setContentType("application/vnd.ms-excel");

            String filename = "运营统计数据报表.xlsx";
            // 解决中文乱码
            filename = new String(filename.getBytes(), "ISO-8859-1");

            // 设置头信息
            //attachment（意味着消息体应该被下载到本地；大多数浏览器会呈现一个"保存为"的对话框，将filename的值预填为下载后的文件名
            response.setHeader("Content-Disposition","attachement;filename=" + filename);
            wb.write(out);
            out.flush();
            out.close();
           return null;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }

    //导出运营统计数据报表
    @GetMapping("/exportBusinessReportPdf")
    public Result exportBusinessReportPdf(HttpServletRequest request,HttpServletResponse response){
        //getRealPath("/") webapp下
        String realPath = request.getSession().getServletContext().getRealPath("/template");
        // File.separator 的作用相当于 '\'
        String jrxml = realPath+ File.separator+"health_business3.jrxml";
        String jasper = realPath+ File.separator+"health_business3.jasper";

        //对模板进行编辑
        try {
            JasperCompileManager.compileReportToFile(jrxml,jasper);
            Map<String,Object> map = reportService.getBusinessReport();
            //热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>)map.get("hotSetmeal");
            //自定义数据JRBeanCollectionDataSource
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, new JRBeanCollectionDataSource(hotSetmeal));
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition","attachement;filename=businessReport.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"导出运营数据统计pdf文件失败");
    }

}
