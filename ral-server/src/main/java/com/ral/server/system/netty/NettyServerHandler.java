package com.ral.server.system.netty;

import com.ral.server.system.thread.GameTask;
import com.ral.server.system.thread.GameThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
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
        // 存储用户数据
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
            ByteBuf tb = null;
            if (msg instanceof byte[]) {
                //长度，协议号，用户
                tb = Unpooled.copiedBuffer((byte[]) msg);
                int len = tb.readInt();//判断拆包
                int code = tb.readInt();
                long userId  = tb.readLong();
                if(code!=1) {
                    byte[] bytes1 = new byte[tb.capacity() - 16];
                    ByteBuf four = tb.readBytes(bytes1);
                    sendToLogic(ctx,bytes1,code);
                    System.out.println(111);
                }else {
                    System.out.println("心跳协议");
                }
            } else if (msg instanceof ByteBuf) {
                tb = (ByteBuf) msg;
            } else if (msg instanceof ByteBuffer) {
                tb = Unpooled.copiedBuffer((ByteBuffer) msg);
            } else {
                String ostr = msg.toString();
                tb = Unpooled.copiedBuffer(ostr, Charset.forName("UTF-8"));
            }
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


    protected void sendToLogic(ChannelHandlerContext ctx,byte[] bytes,int code ){
        GameThreadPool.executeTask(new GameTask(ctx,bytes,code));
    }



}
