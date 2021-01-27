package com.swj.vhr.controller;

import com.swj.vhr.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: swj
 * @Description: 登录跳转页面
 * @Date: Create in 15:49 2021/1/14
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    public RespBean login(){
        return  RespBean.error("尚未登录，请登录！！");
    }
}
