package com.ral.server.game.db.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ral.server.game.db.user.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    public List<User> getUserInfo();

    public List<User> getUserInfoByCondition(@Param("id")Long id);

    public List<User> getUserInfoByConditionThree(@Param("id")Long id);
}
