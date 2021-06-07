package com.ral.server.game.db.user.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ral.server.game.db.user.model.User;

public interface UserService extends IService<User> {


    public JSONObject getUser();

    public JSONObject getUserInfo();

}
