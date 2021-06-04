package com.ral.pb.common;

import com.google.protobuf.Message;

import java.io.Serializable;

public class ProtoBufMessage implements Serializable {

    /**
     * 数据长度
     */
    private int len;

    /**
     * 协议号
     */
    private int code;

    /**
     * 玩家id
     */
    private long playerId;

    /**
     * protobuf协议
     */
    private Message message;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
