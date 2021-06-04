package com.ral.client.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.ral.client.netty.NettyClientHandler;
import com.ral.pb.common.ProtoBufMessage;
import com.ral.pb.test.Test;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


//    @Autowired
//    private BuildToServerMessage buildToServerMessage;


    @RequestMapping("/test4")
    public JSONObject test4() throws InterruptedException {
        JSONObject json = new JSONObject();
        json.put("msg","success");


        Test.TestFirstMessage.Builder test = Test.TestFirstMessage.newBuilder();
        test.setId(2);
        test.setMsg("测试数据内容");
        byte[] bytes = test.build().toByteArray();
        ByteBuf buf = Unpooled.buffer(bytes.length+16);
        // 长度，协议号，用户
        buf.writeInt(bytes.length+16);//1
        buf.writeInt(2);//2
        buf.writeLong(123456);//3
        buf.writeBytes(bytes);

        ChannelHandlerContext context = NettyClientHandler.map.get(1);
        ChannelFuture result = context.channel().writeAndFlush(buf.array());
        Thread.sleep(1000);
        System.out.println(result.isDone());

        return json;
    }





    @RequestMapping("/test")
    public JSONObject test() throws InterruptedException, InvalidProtocolBufferException {
        JSONObject json = new JSONObject();
        ChannelHandlerContext context = NettyClientHandler.map.get(1);
        json.put("protocol",2);
        JSONObject data = new JSONObject();
        data.put("roleId",10001);
        data.put("roleName","测试角色");
        json.put("data",data);
        String dataStr = json.toJSONString();

        Test.TestFirstMessage.Builder abc  = Test.TestFirstMessage.newBuilder();
        abc.setId(1);
        abc.setMsg("abc");
        byte[] bytes = abc.build().toByteArray();


        ProtoBufMessage pbf = new ProtoBufMessage();
        pbf.setCode(2);
        pbf.setMessage(abc.build());
        pbf.setPlayerId(123456);
        pbf.setLen(bytes.length);
        ByteBuf buf = Unpooled.buffer(bytes.length+16);
        //数据格式 长度，协议号，用户， int 4 long 8

//        byte[] send = new byte[bytes.length+16];
        buf.writeInt(pbf.getLen());//1
        buf.writeInt(pbf.getCode());//2
        buf.writeLong(pbf.getPlayerId());//3
        buf.writeBytes(bytes);


        int one = buf.readInt();
        int two = buf.readInt();
        long three = buf.readLong();

        byte[] bytes1 = new byte[buf.capacity()-16];
        ByteBuf four = buf.readBytes(bytes1);

        System.out.println(bytes);
        System.out.println("========");
        System.out.println(bytes1);

        Test.TestFirstMessage abcd  = Test.TestFirstMessage.parseFrom(bytes1);


        System.out.println(123);



//        ByteBuf tb = Unpooled.copiedBuffer(buf.array());
//        byte[] buffer = new byte[tb.readableBytes()];
//        tb.readBytes(tb);


        ChannelFuture result = context.channel().writeAndFlush(buf.array());
        Thread.sleep(1000);
        System.out.println(result.isDone());
//        context.writeAndFlush(json);
        return json;
    }


    @RequestMapping("/test2")
    public JSONObject test2() throws InterruptedException {
        JSONObject json = new JSONObject();
//        ChannelHandlerContext context = NettyClientHandler.map.get(1);
//        json.put("protocol",2);
        JSONObject data = new JSONObject();
        data.put("roleId",10001);
        data.put("roleName","测试角色");
        json.put("data",data);
        String dataStr = json.toJSONString();
        byte[] bytes = dataStr.getBytes();

//        ChannelFuture result = context.channel().writeAndFlush(bytes);
        Thread.sleep(1000);
//        System.out.println(result.isDone());
//        context.writeAndFlush(json);

//        ChannelHandlerContext context = NettyClientHandler.map.get(1);



//        BinaryWebSocketFrame frame = new BinaryWebSocketFrame();
//        frame.duplicate()
//        frame.content();
//        Test.TestFirstMessage.Builder abc  = Test.TestFirstMessage.newBuilder();
//        abc.setId(1);
//        abc.setMsg("abc");
        return json;
    }


    @RequestMapping("/test3")
    public JSONObject test3() throws InterruptedException {

        Test.TestFirstMessage.Builder data  = Test.TestFirstMessage.newBuilder();
        data.setId(18);
        data.setMsg("dataMsg");
//        ProtoBufMessage msg = buildToServerMessage.build(abc);

        ChannelHandlerContext context = NettyClientHandler.map.get(1);
//        Message message = abc.build();
//        Test.TestFirstMessage message = abc.build();
//        System.out.println(message);


        JSONObject json = new JSONObject();
//        JSONObject data = new JSONObject();
//        data.put("roleId",10001);
//        data.put("roleName","测试角色");
//        json.put("data",data);
        String dataStr = json.toJSONString();
        byte[] bytes = dataStr.getBytes();


        ChannelFuture result = context.channel().writeAndFlush(bytes);
        Thread.sleep(1000);
        System.out.println(result.isDone());



        return null;
    }



}
