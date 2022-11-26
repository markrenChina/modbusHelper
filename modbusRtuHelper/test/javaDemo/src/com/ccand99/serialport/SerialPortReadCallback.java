package com.ccand99.serialport;

/**
 * 必须存在的类，包名和类名不能更改
 * 读取回调接口
 * @author markrenChina
 */
@FunctionalInterface
public interface SerialPortReadCallback {
    public void recv(byte[] bytes);
}
