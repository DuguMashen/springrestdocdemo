package com.dugumashen.springrestdocdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jinhq
 */
@RestController
public class IndexController {
    @RequestMapping("/index")
    public String index() {
        return "success";
    }
}
