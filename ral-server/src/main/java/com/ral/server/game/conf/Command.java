package com.ral.server.game.conf;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public interface Command {

    /**
     * 逻辑层执行方法
     * @param ctx
     * @param data
     */
    public void executeAll(ChannelHandlerContext ctx, byte[] bytes);
}
