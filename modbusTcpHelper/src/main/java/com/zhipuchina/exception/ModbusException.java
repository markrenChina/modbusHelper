package com.zhipuchina.exception;

public abstract class ModbusException extends Exception{
    private final int code;

    public ModbusException(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public abstract String toString();
}
