package com.zhipuchina.exception;

//从站已经接受请求并且正在处理，但是需要较长的处理时间，返回这个响应防止主站发生超时错误
public class AcknowledgeException extends ModbusException {

    public AcknowledgeException() {
        super(5);
    }

    @Override
    public String toString() {
        return "The slave/server has accepted the request and\n" +
                "is processing it, but a long duration of time is\n" +
                "required to do so. This response is returned to\n" +
                "prevent a timeout error from occurring in the\n" +
                "master. \n";
    }
}
