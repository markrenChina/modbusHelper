package com.zhipuchina.pojo;

import com.zhipuchina.event.ReadCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

//todo 整加一个function 异步读写
public class Exchange {
    int functionCode;
    int address;  //相对地址
    int count;
    final CountDownLatch latch;
    final List<Object> result = new ArrayList<>();

    final ReadCallback callback;

    public Exchange(int functionCode, int address, int count) {
        this(functionCode,address,count,null,null);
    }

    public Exchange(int functionCode, int address, int count, CountDownLatch latch) {
        this(functionCode,address,count,latch,null);
    }

    public Exchange(int functionCode, int address, int count, ReadCallback callback) {
        this(functionCode,address,count,null,callback);
    }

    public Exchange(int functionCode, int address, int count, CountDownLatch latch, ReadCallback callback) {
        this.functionCode = functionCode;
        this.address = address;
        this.count = count;
        this.latch = latch;
        this.callback = callback;
    }

    public int getFunctionCode() {
        return functionCode;
    }

    public int getAddress() {
        return address;
    }

    public int getCount() {
        return count;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public List<Object> getResult() {
        return result;
    }

    public ReadCallback getCallback() {
        return callback;
    }
}
