package com.zhipuchina.exception;

//执行请求的操作时，产生不可重新恢复的差错
public class DeviceFailureException extends ModbusException {

    public DeviceFailureException() {
        super(4);
    }

    @Override
    public String toString() {
        return "The server failed during execution. An\n" +
                "unrecoverable error occurred while the\n" +
                "slave/server was attempting to perform the\n" +
                "requested action";
    }
}
