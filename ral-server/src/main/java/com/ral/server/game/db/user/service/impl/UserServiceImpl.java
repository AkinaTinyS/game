package com.ral.server.game.db.user.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ral.server.game.db.user.dao.UserMapper;
import com.ral.server.game.db.user.model.User;
import com.ral.server.game.db.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getUser() {
        JSONObject json = new JSONObject();
        json.put("getUser","user");
        User user = userMapper.selectById(1);
        json.put("user",user);
        return json;
    }

    @Override
    public JSONObject getUserInfo() {
        JSONObject json = new JSONObject();
        json.put("getUser","user");
        List<User> user = userMapper.getUserInfo();
        json.put("user",user);
        return json;
    }
}
