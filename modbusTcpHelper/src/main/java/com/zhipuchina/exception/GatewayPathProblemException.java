package com.zhipuchina.exception;

//网关路径不可用
public class GatewayPathProblemException extends ModbusException {

    public GatewayPathProblemException() {
        super(10);
    }

    @Override
    public String toString() {
        return "Gateway path(s) not available";
    }
}
