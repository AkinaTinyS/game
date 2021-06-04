package com.ral.server.system.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline cp = ch.pipeline();
        //参数配置 读超时时间，写超时时间，所有类型读超时时间，时间格式
        cp.addLast(new IdleStateHandler(30,0,0, TimeUnit.SECONDS));
        //传输协议
//        cp.addLast(new StringDecoder());
//        cp.addLast(new StringEncoder());
        cp.addLast(new ByteArrayDecoder());
        cp.addLast(new ByteArrayEncoder());

        //业务逻辑handler
        cp.addLast("nettyServerHandler",new NettyServerHandler());
    }
}
