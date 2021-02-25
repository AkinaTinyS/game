package com.ral.client.controller;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @RequestMapping("/test")
    public JSONObject test(){
        JSONObject json = new JSONObject();



        json.put("result","成功");
        return json;
    }

}
