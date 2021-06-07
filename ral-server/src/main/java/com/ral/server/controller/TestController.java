package com.ral.server.controller;


import com.alibaba.fastjson.JSONObject;
import com.ral.server.game.db.user.dao.UserMapper;
import com.ral.server.game.db.user.model.User;
import com.ral.server.game.db.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @RequestMapping("test")
    @ResponseBody
    public JSONObject test(){
        JSONObject json = new JSONObject();
        json = userService.getUser();
        return json;
    }

    @RequestMapping("test2")
    @ResponseBody
    public JSONObject test2(){
        JSONObject json = new JSONObject();
        json = userService.getUserInfo();
        return json;
    }

    @RequestMapping("test3")
    @ResponseBody
    public JSONObject test3(){
        JSONObject json = new JSONObject();
        List<User> user = userMapper.getUserInfoByCondition(2l);
        json.put("user",user);
        return json;
    }

    @RequestMapping("test4")
    @ResponseBody
    public JSONObject test4(){
        JSONObject json = new JSONObject();
        List<User> user = userMapper.getUserInfoByConditionThree(2l);
        json.put("user",user);
        return json;
    }

    @RequestMapping("test5")
    @ResponseBody
    public JSONObject test5(){
        JSONObject json = new JSONObject();
        json.put("user",123);
        return json;
    }

    @RequestMapping("test6")
    @ResponseBody
    public JSONObject test6(){
        JSONObject json = new JSONObject();
        json.put("user",456);
        return json;
    }
}
