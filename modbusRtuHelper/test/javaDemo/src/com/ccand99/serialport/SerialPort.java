package com.ccand99.serialport;

import java.io.*;

public class SerialPort {

    final int dataBits;
    final int stopBits;
    final int baudRate;
    final String path;

    final int fd;

    public SerialPort(String path,int baudRate ,int dataBits, int stopBits) {
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.baudRate = baudRate;
        this.path = path;
        this.fd = SerialPortUtils.nativeOpen(path,baudRate,stopBits,dataBits,0,0,0);
    }

    //所有FileOutputStream的方法都可以被使用
    public void write(byte[] b) throws IOException {
        SerialPortUtils.nativeWrite(fd,b);
    }

    public int read(byte[] b) throws IOException {
        return SerialPortUtils.nativeRead(fd,b);
    }
}
