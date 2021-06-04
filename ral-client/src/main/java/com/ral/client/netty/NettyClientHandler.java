package com.ral.client.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private NettyClient nettyClient;

    /**
     * 循环次数
     */
    private AtomicInteger fcount = new AtomicInteger(1);


    public static ConcurrentHashMap<Integer, ChannelHandlerContext> map = new ConcurrentHashMap<>();

    public ChannelHandlerContext getMap() {
        if (map != null && map.size() > 0) {
            return map.get(1);
        }
        return null;
    }

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("建立连接时：" + new Date());
        ctx.fireChannelActive();
        if (null == map || map.size() < 1) {
            map.put(1, ctx);
        }
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭连接时：" + new Date());
        final EventLoop eventLoop = ctx.channel().eventLoop();
        nettyClient.doConnect(new Bootstrap(), eventLoop);
        super.channelInactive(ctx);
    }


    /**
     * 心跳请求处理 每4秒发送一次心跳请求;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        System.out.println("循环请求的时间：" + new Date() + "，次数" + fcount.get());
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果写通道处于空闲状态,就发送心跳命令
            System.out.println(event.state());
//                        if (IdleState.WRITER_IDLE.equals(event.state())) {
            if (IdleState.READER_IDLE.equals(event.state())) {
//                                UserMsg.User.Builder userState = UserMsg.User.newBuilder().setState(2);
                JSONObject json = new JSONObject();
                json.put("protocol",1);
                json.put("msg","heartbeat");
                String msg = JSONObject.toJSONString(json);

                ByteBuf buf = Unpooled.buffer(16);
                //数据格式 长度，协议号，用户， int 4 long 8

//        byte[] send = new byte[bytes.length+16];
                buf.writeInt(16);//1
                buf.writeInt(1);//2
                buf.writeLong(0);//3

                System.out.println(msg);
                ctx.channel().writeAndFlush(buf.array());
                fcount.getAndIncrement();
            }
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果不是protobuf类型的数据
//                if (!(msg instanceof UserMsg.User)) {
//                        log.info("未知数据!" + msg);
//                        return;
//                }
        try {

            // 得到protobuf的数据
//                        UserMsg.User userMsg = (UserMsg.User) msg;
            // 进行相应的业务处理。。。
            // 这里就从简了，只是打印而已
            JSONObject json = new JSONObject();
            json.put("protocol",1);
            json.put("msg","heartbeat");
//            String msgStr = (String) msg;
            String msgStr = json.toJSONString(json);
//            System.out.println("来自服务端的消息:" + msgStr);
            // 这里返回一个已经接受到数据的状态
//                        UserMsg.User.Builder userState = UserMsg.User.newBuilder().setState(1);
            ctx.writeAndFlush(msgStr);
            System.out.println("客户端告诉服务端消息已接收!消息内容：来自客户端的消息:消息接收成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
