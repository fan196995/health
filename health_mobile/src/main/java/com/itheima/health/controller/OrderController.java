package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/1 0:41
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,Object> orderInfo){
        //验证手机验证码
        String telephone = (String) orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String codeRedis = jedis.get(key);
        if (StringUtils.isEmpty(codeRedis)){
            return new Result(false, "请点击发送验证码");
        }

        if (!codeRedis.equals(orderInfo.get("validateCode"))){
            return new Result(false, "验证码不正确");
        }
        //清除redis中验证码
        jedis.del(key);
        //设置预约类型 微信预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submit(orderInfo);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    @PostMapping("/findById")
    public Result findById(int id){
        Map<String,Object> map =orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }

    @GetMapping("/exportSetmealInfo")
    //订单id
    public Result exportSetmealInfo(HttpServletResponse response,int id) {
        Map<String, Object> map = orderService.findById(id);
        int setmealId = (int) map.get("setmealId");
        Setmeal setmeal = setmealService.findDetailById(setmealId);

        //生成PDF文件
        Document document = new Document();
        try {
            //导出文件Content-Type: application/pdf
            // 设置头信息
            //attachment（意味着消息体应该被下载到本地；大多数浏览器会呈现一个"保存为"的对话框，将filename的值预填为下载后的文件名
            String filename = "exportSetmeal.pdf";
            response.setContentType("application/pdf");
            //response.setHeader("Content-Type","application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            //字体
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            Font font = new Font(baseFont, 10f, Font.NORMAL, Color.BLUE);

            document.add(new Paragraph("体检人:" + (String) map.get("member"), font));
            document.add(new Paragraph("体检套餐:" + (String) map.get("setmeal"), font));
            Date orderDate = (Date) map.get("orderDate");
            document.add(new Paragraph("体检日期:" + DateUtils.parseDate2String(orderDate, "yyyy-MM-dd"), font));
            document.add(new Paragraph("预约类型:" + (String) map.get("orderType"), font));

            //表头
            Table table = new Table(3);
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式

            table.addCell(buildCell("项目名称", font));
            table.addCell(buildCell("项目内容", font));
            table.addCell(buildCell("项目解读", font));

            //数据
            List<CheckGroup> checkGroups = setmeal.getCheckGroups();
            if (checkGroups!=null) {
                for (CheckGroup checkGroup : checkGroups) {
                    table.addCell(buildCell(checkGroup.getName(), font));

                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    String checkItemStr = "";
                    if (checkItems!=null) {
                        StringBuilder sb = new StringBuilder();
                        for (CheckItem checkItem : checkItems) {
                            sb.append(checkItem.getName()).append("  ");
                        }
                        //去掉最后一个空格
                        sb.setLength(sb.length()-1);
                        checkItemStr = sb.toString();
                    }
                    table.addCell(buildCell(checkItemStr, font));
                    table.addCell(buildCell(checkGroup.getRemark(), font));
                }
            }
            document.add(table);
            document.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "导出预约信息失败");
    }

    private Cell buildCell(String name, Font font) throws BadElementException {
        Phrase phrase = new Phrase(name,font);
        return new Cell(phrase);
    }
}
