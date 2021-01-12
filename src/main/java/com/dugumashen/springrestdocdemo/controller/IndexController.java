package com.dugumashen.springrestdocdemo.controller;

import com.dugumashen.springrestdocdemo.entity.User;
import com.dugumashen.springrestdocdemo.service.UserService;
import com.dugumashen.springrestdocdemo.util.PoiUtil;
import com.github.javafaker.Faker;
import com.github.pagehelper.PageInfo;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jinhq
 */
@RestController
public class IndexController {
    private static Logger logger=LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "success";
    }

    @RequestMapping("/app")
    public Map<String,Object> app(@RequestBody(required = false) Map map){
        Object value=map.get("key");
        Map<String,Object> objectMap=new HashMap<>();
        objectMap.put("status","error");
        if(value!=null){
            objectMap.put("status","ok");
        }
        return objectMap;
    }

    @RequestMapping("/queryAll")
    public List<User> s(Integer offset,Integer limit) {
        List<User> u = userService.queryAllByLimit(offset, limit);
        return u;
    }

    @RequestMapping("/insertData")
    public String insert(){

        Faker faker = new Faker(Locale.CHINA);

        List<User> users= Stream.generate(()-> {User u= new User();u.setName(faker.name().fullName());u.setPassword(UUID.randomUUID().toString());u.setStatus("init");return u;}).limit(100000).collect(Collectors.toList());
        userService.insertBatch(users);
        return "success";
    }


    @RequestMapping("/export")
    public void export(HttpServletResponse response, Integer sheetCount) {
        LinkedHashMap<String, String> title = new LinkedHashMap<>();
        title.put("name", "姓名");
        title.put("password", "密码");
        title.put("status", "状态");

        List<Map<String, String>> data = new LinkedList<>();

        int pageNum = 1;
        int pageSize = 20000;
        if (sheetCount != null) {
            pageSize = sheetCount;
        }
        User user = new User();
        SXSSFWorkbook workbook = null;
        PageInfo<User> pageInfo = null;
        do {
        pageInfo = userService.queryAll(user, pageNum, pageSize);
        List<User> us = pageInfo.getList();
        for (User u : us) {
            Map<String, String> map = new HashMap<>();
            map.put("name", u.getName());
            map.put("password", u.getPassword());
            map.put("status", u.getStatus());
            data.add(map);
        }
        logger.info("数据查询结束:{}", current());
        pageNum = pageInfo.getNextPage();
        logger.info("hasNext:{},nextPage:{},pageNum:{},pageSize:{},endRow:{}", pageInfo.isHasNextPage(), pageNum,
                pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getEndRow());
        logger.info("生成表格开始:{}", current());
        workbook = PoiUtil.generateSXSSFWorkbook("第" + pageInfo.getPageNum() + "页", title, data, workbook);
        logger.info("生成表格结束:{}", current());
        } while (pageInfo.isHasNextPage());


        setResponseHeader(response, "用户.xlsx");
        try {
            PoiUtil.exportExcel(response.getOutputStream(), workbook);
        } catch (IOException e) {
            logger.info("导出表格失败");
        }

    }

    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            //清除缓存
            response.reset();
            fileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
        } catch (UnsupportedEncodingException e) {
            logger.error("VolunteerController.setResponseHeader 设置响应头失败");
            throw new RuntimeException("导出志愿者数据失败");
        }
    }

    private String current(){
        LocalTime localTime= LocalTime.now();
        DateTimeFormatter s= DateTimeFormatter.ofPattern("HH:mm:ss");
        return s.format(localTime);
    }

    @RequestMapping("/jdbcExport")
    public void jdbcExport(HttpServletResponse response) {
        LinkedHashMap<String, String> title = new LinkedHashMap<>();
        title.put("name", "姓名");
        title.put("password", "密码");
        title.put("status", "状态");


        logger.info("jdbcExport 查询开始:{}", current());
        List<Map<String, String>> l = userService.jdbcQuery();
        logger.info("jdbcExport 查询结束:{}", current());
        SXSSFWorkbook workbook = PoiUtil.generateSXSSFWorkbook("用户", title, l, null);
        logger.info("jdbcExport 制表结束:{}", current());
        setResponseHeader(response, "用户.xlsx");
        try {
            PoiUtil.exportExcel(response.getOutputStream(), workbook);
        } catch (IOException e) {
            logger.info("jdbcExport 导出表格失败");
        }

    }

    @RequestMapping("/mbExport")
    public void mbExport(HttpServletResponse response){
        LinkedHashMap<String, String> title = new LinkedHashMap<>();
        title.put("name", "姓名");
        title.put("password", "密码");
        title.put("status", "状态");


        logger.info("mbExport 查询开始:{}", current());
        List<Map<String, String>> l = userService.selectAll();
        logger.info("mbExport 查询结束:{}", current());
        SXSSFWorkbook workbook = PoiUtil.generateSXSSFWorkbook("用户", title, l, null);
        logger.info("mbExport 制表结束:{}", current());
        setResponseHeader(response, "用户.xlsx");
        try {
            PoiUtil.exportExcel(response.getOutputStream(), workbook);
        } catch (IOException e) {
            logger.info("mbExport 导出表格失败");
        }
    }

    @RequestMapping("/jdbc")
    public Map<String,String> jdbc() {
        LocalDateTime start = LocalDateTime.now();
        List<Map<String, Object>> l = userService.jdbc2Query();
        LocalDateTime end = LocalDateTime.now();
        long k = start.until(end, ChronoUnit.SECONDS);
        Map<String, String> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        map.put("start", formatter.format(start));
        map.put("end", formatter.format(end));
        map.put("diff", String.valueOf(k));
        return map;
    }

    @RequestMapping("/mb")
    public Map<String,String> mb() {
        User user=new User();
        LocalDateTime start = LocalDateTime.now();
        List<User> l = userService.queryAllByLimit(0,120000);
        LocalDateTime end = LocalDateTime.now();
        long k = start.until(end, ChronoUnit.SECONDS);

        LocalTime sss=LocalTime.now();
        for (User us:l) {
            String name=us.getName();
            String password=us.getPassword();
            String status=us.getStatus();
        }
        LocalTime eee=LocalTime.now();
        long ss=sss.until(eee,ChronoUnit.SECONDS);

        Map<String, String> map = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        map.put("start", formatter.format(start));
        map.put("end", formatter.format(end));
        map.put("diff", String.valueOf(k));
        map.put("foreach",String.valueOf(ss));

        return map;
    }

    @RequestMapping("/calc")
    public Object calc() {
        int c = 5 / 0;
        return c;
    }
}
