package com.zhipuchina.exception;

//从站正在处理长持续时间的请求
public class DeviceBusyException extends ModbusException {

    public DeviceBusyException() {
        super(6);
    }

    @Override
    public String toString() {
        return "The slave is engaged in processing a longduration program command. The master\n" +
                "should retransmit the message later when the\n" +
                "slave is free";
    }
}
