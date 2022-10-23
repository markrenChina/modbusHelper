package com.zhipuchina.model;

public class Memory<T> {
    protected T value;
    private final int code;

    public Memory(int code) {
        this.code = code;
    }

    public T getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public void setValue(T val) {
        value = val;
    }
}
