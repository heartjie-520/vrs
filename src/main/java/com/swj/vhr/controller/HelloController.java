package com.swj.vhr.controller;

import com.swj.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: swj
 * @Description:
 * @Date: Create in 11:24 2021/1/13
 */
@RestController
public class HelloController {

    @Autowired
    private HrService hrService;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
