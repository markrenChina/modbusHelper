package com.zhipuchina.exception;

//对端设备未响应
public class GatewayRefuseProblemException extends ModbusException {

    public GatewayRefuseProblemException() {
        super(11);
    }

    @Override
    public String toString() {
        return "The target device failed to respond (the\n" +
                "gateway generates this exception). ";
    }
}
