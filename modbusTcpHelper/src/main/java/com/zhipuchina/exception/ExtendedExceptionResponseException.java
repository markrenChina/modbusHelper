package com.zhipuchina.exception;

//从站已经接受请求并且正在处理，但是需要较长的处理时间，返回这个响应防止主站发生超时错误
public class ExtendedExceptionResponseException extends ModbusException {

    public ExtendedExceptionResponseException() {
        super(0xFF);
    }

    @Override
    public String toString() {
        return "The exception response PDU contains\n" +
                "extended exception information. A subsequent\n" +
                "2 byte length field indicates the size in bytes of\n" +
                "this function-code specific exception\n" +
                "information";
    }
}
