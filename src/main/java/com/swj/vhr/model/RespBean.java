package com.swj.vhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: swj
 * @Description: 返回类
 * @Date: Create in 10:47 2021/1/14
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RespBean {
    private Integer  status;
    private String msg;
    private Object obj;

    public static  RespBean ok(String msg){
        return  new RespBean(200,msg,null);
    }

    public static  RespBean ok(String msg,Object obj){
        return  new RespBean(200,msg,obj);
    }

    public static  RespBean error(String msg){
        return  new RespBean(500,msg,null);
    }

    public static  RespBean error(String msg,Object obj){
        return  new RespBean(500,msg,obj);
    }



}
