package com.ral.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger idle_count = new AtomicInteger(1);

    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 建立连接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接到客户端地址："+ctx.channel().remoteAddress());
        ctx.writeAndFlush("发送给客户端第一条消息");
        super.channelActive(ctx);
    }


    /**
     * 超时处理 如果5秒没有接收客户端到心跳就触发，如果超过两次就关闭
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object obj)throws Exception{
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
//            if (IdleState.READER_IDLE.equals(event.state())) {
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                System.out.println("已经30秒没有接收到客户端的信息了");
                if (idle_count.get() > 1) {
                    System.out.println("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
                idle_count.getAndIncrement();
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }


    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        System.out.println("第" + count.get() + "次" + ",服务端接受的消息:" + msg);
        try {
            String message = (String) msg;
            // 如果是protobuf类型的数据
//            if (msg instanceof String) {
//                UserMsg.User user = (UserMsg.User) msg;
//                if (user.getState() == 1) {
//                    log.info("客户端业务处理成功!");
//                } else if(user.getState() == 2){
//                    log.info("接受到客户端发送的心跳!");
//                }else{
//                    log.info("未知命令!");
//                }
//            } else {
//                log.info("未知数据!" + msg);
//                return;
//            }
            count.getAndIncrement();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
        count.getAndIncrement();
    }


    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
