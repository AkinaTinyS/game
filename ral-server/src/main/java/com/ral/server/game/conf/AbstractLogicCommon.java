package com.ral.server.game.conf;

import com.alibaba.fastjson.JSONObject;
import com.ral.server.game.gameplayer.GamePlayer;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractLogicCommon implements Command {


    public void executeAll(ChannelHandlerContext ctx, byte[] bytes){
        //封装玩家数据
        System.out.println(ctx.channel().id());
        GamePlayer player = new GamePlayer();
        execute(player,null);
    }


    public abstract void execute(GamePlayer player,JSONObject data);

}
