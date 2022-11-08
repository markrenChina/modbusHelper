package com.zhipuchina.exception;

//接受到的值是不允许的地址
public class IllegalDataValueException extends ModbusException {

    public IllegalDataValueException() {
        super(3);
    }

    @Override
    public String toString() {
        return "A value contained in the query data field is not an allowable value for the slave or is invalid. ";
    }
}
