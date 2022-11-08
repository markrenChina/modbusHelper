package com.zhipuchina.exception;

//从机执行中错误
public class NegativeAcknowledgeException extends ModbusException {

    public NegativeAcknowledgeException() {
        super(7);
    }

    @Override
    public String toString() {
        return "The slave cannot perform the program function\n" +
                "received in the query. This code is returned\n" +
                "for an unsuccessful programming request\n" +
                "using function code 13 or 14 (codes not\n" +
                "supported by this model). The master should\n" +
                "request diagnostic information from the slave.";
    }
}
