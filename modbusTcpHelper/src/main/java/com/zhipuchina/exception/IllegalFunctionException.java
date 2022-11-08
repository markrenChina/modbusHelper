package com.zhipuchina.exception;

//接受到的功能码是不允许的操作
public class IllegalFunctionException extends ModbusException {

    public IllegalFunctionException() {
        super(1);
    }

    @Override
    public String toString() {
        return "The function code received in the query is not allowed or invalid.";
    }
}
