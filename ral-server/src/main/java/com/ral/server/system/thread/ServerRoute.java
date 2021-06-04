package com.ral.server.system.thread;

import com.alibaba.fastjson.JSONObject;
import com.ral.server.game.conf.Command;
import io.netty.channel.ChannelHandlerContext;

public class ServerRoute {



    public static void routAgent(ChannelHandlerContext ctx, byte[] bytes,int code ) {
        Command cmd = LoadGameLogic.getCommand(code);
        if (cmd != null) {
            try {
                cmd.executeAll(ctx, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Code not find, code = " + Integer.toHexString(code));
        }
    }
}
