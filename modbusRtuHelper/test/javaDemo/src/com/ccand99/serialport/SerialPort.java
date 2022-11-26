package com.ccand99.serialport;

import java.io.*;

/**
 * 封装示例
 *
 */
public class SerialPort {

    private int dataBits;
    private int stopBits;
    private int baudRate;
    private final String path;

    private int fd;

    public SerialPort(String path,int baudRate ,int dataBits, int stopBits) {
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.baudRate = baudRate;
        this.path = path;
        this.fd = SerialPortUtils.nativeOpen(path,baudRate,stopBits,dataBits,0,0,0);
    }

    public void reOpen(){
        if (fd != -1){
            close();
        }
        this.fd = SerialPortUtils.nativeOpen(path,baudRate,stopBits,dataBits,0,0,0);
    }

    //所有FileOutputStream的方法都可以被使用
    public void write(byte[] b) throws IOException {
        SerialPortUtils.nativeWrite(fd,b);
    }

    public byte[] read() throws IOException {
        return SerialPortUtils.nativeRead(fd);
    }

    public byte[] readNBytes(int size){
        return SerialPortUtils.nativeReadNBytes(fd,size);
    }

    public void readCallback(SerialPortReadCallback callback){
        new Thread( () -> {
            while (true){
                SerialPortUtils.nativeReadCallBack(fd,callback);
                System.out.println("read callback finnish");
            }
        }).start();
    }

    public void close(){
        SerialPortUtils.nativeClose(fd);
        fd = -1;
    }


    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(int baudRate) {
        this.baudRate = baudRate;
    }

    public String getPath() {
        return path;
    }
}
