package com.zhipuchina.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Exchange {
    int functionCode;
    int address;  //相对地址
    int count;
    final CountDownLatch latch;
    final List<Object> result = new ArrayList<>();

    public Exchange(int functionCode, int address, int count) {
        this(functionCode,address,count,null);
    }

    public Exchange(int functionCode, int address, int count, CountDownLatch latch) {
        this.functionCode = functionCode;
        this.address = address;
        this.count = count;
        this.latch = latch;
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
}
