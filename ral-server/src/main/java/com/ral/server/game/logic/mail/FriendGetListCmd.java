package com.ral.server.game.logic.mail;


import com.alibaba.fastjson.JSONObject;
import com.ral.server.game.conf.AbstractLogicCommon;
import com.ral.server.game.conf.Cmd;
import com.ral.server.game.conf.GameProtocol;
import com.ral.server.game.gameplayer.GamePlayer;

@Cmd(code = GameProtocol.FRIEND_PROTOCOL,desc = "测试协议2")
public class FriendGetListCmd extends AbstractLogicCommon {

    @Override
    public void execute(GamePlayer player, JSONObject data){
        System.out.println("执行测试协议2");
    }

}
