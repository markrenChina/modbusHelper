package com.zhipuchina.exception;

//接受到的数据地址是不允许的地址
public class IllegalDataAddressException extends ModbusException {

    public IllegalDataAddressException() {
        super(2);
    }

    @Override
    public String toString() {
        return "The data address received in the query is not an allowable address for the slave or is invalid.";
    }
}
