package com.dugumashen.springrestdocdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.dugumashen.springrestdocdemo.bean.LoginDTO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author
 */
@RestController
public class GenDocsDemo {

    @GetMapping("/user")
    public Object queryUser(@RequestParam String name) {
        return Collections.singletonMap("name", name);
    }

    @PostMapping("/login")
    public Object login(@RequestBody(required = false) LoginDTO loginDTO) {
        System.out.println("参数: " + JSONObject.toJSONString(loginDTO));
        Map<String,Object> map=new HashMap<>();
        map.put("token",UUID.randomUUID().toString());
        return map;
    }

}
