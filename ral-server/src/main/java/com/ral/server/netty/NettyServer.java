package com.ral.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Component
public class NettyServer {


    public ChannelFuture channelFuture;

    public EventLoopGroup bossGroup;

    public EventLoopGroup workerGroup;

    /**
     * 启动加载netty服务端
     */
    @PostConstruct
    public void init() throws InterruptedException {
        System.out.println("开始启动netty");
        //创建boss线程组 用于接收客户端的连接
        bossGroup = new NioEventLoopGroup(1);
        //创建worker线程组 用于SocketChannel的数据读写
        workerGroup = new NioEventLoopGroup(12);
        //创建ServerBootstrap对象
        ServerBootstrap b = new ServerBootstrap();
        //设置bootstrap
        b.group(bossGroup,workerGroup)
                //设置被实例化为NioServerSocketChannel类
                .channel(NioServerSocketChannel.class)
                //设置NioServerSocketChannel的处理器
                .handler(new LoggingHandler(LogLevel.INFO))
                //设置连接服务端的client的SocketChannel的处理器
                .childHandler(new NettyServerInitializer());
        System.out.println("启动netty");
        ChannelFuture f = b.bind(9000).sync();
        f.channel().closeFuture().sync();

    }



}
