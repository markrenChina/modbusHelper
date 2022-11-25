package com.ccand99.serialport;

import java.io.File;
import java.io.FileDescriptor;

public class SerialPortUtils {

    //-1不成功
    public native static int nativeOpen(String path,int baudrate, int stopBits, int dataBits, int parity, int flowCon,
                                     int flags);

    public native static void nativeClose(int fd);

    public native static void nativeWrite(int fd,byte[] bytes);
    public native static int nativeRead(int fd,byte[] bytes);

    static {
        System.out.println(System.getProperty("java.library.path"));
        File file = new File(System.getProperty("user.dir"));
        if(Platform.isWindows){
//            System.out.println(file.getParentFile().getParentFile().getAbsoluteFile() + "\\lib\\modbusRtuHelper.dll");
            System.load(file.getParentFile().getParentFile().getAbsoluteFile() + "\\bin\\modbusRtuHelper.dll");
//            System.loadLibrary("modbusRtuHelper");
        }
    }
}
