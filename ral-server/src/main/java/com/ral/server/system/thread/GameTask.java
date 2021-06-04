package com.ral.server.system.thread;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

public class GameTask implements Runnable {

    private ChannelHandlerContext ctx;

    private byte[] bytes;

    private int code;


    public GameTask(ChannelHandlerContext ctx,byte[] bytes,int code){
        this.ctx = ctx;
        this.bytes = bytes;
        this.code = code;
    }


    /**
     * 协议分发
     */
    @Override
    public void run() {
        if(code==1){
            //心跳协议
        }else if(code<10000){
            ServerRoute.routAgent(ctx,bytes,code);
        }else {
            //未找到协议号
        }
    }
}
