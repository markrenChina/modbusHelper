package com.zhipuchina.model;

public class ByteArray extends BaseArray {
    byte[] data;

    public ByteArray(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
