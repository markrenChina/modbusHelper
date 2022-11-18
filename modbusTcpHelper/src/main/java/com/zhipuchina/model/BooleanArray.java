package com.zhipuchina.model;

public class BooleanArray extends BaseArray{
    boolean[] data;

    public BooleanArray(int size) {
        this.data = new boolean[size];
    }

    public boolean[] getData() {
        return data;
    }
}
