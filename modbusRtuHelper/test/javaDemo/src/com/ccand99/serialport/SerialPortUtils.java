package com.ccand99.serialport;

import java.io.File;


/**
 * 必须存在的类，包名和类名不能更改
 * 串口工具类（JNI）
 * @author markrenChina
 */
public class SerialPortUtils {

    /**
     * 打开串口
     * @param path  串口名  例如： Com3（windows大于10的Com需要前缀\\.） /dev/ttyS0
     * @param baudrate  波特率          默认 9600
     * @param stopBits  停止位          默认 1
     * @param dataBits  数据位          默认 8
     * @param parity    校验            默认 0
     * @param flowCon   流控            默认 0 //todo windows未实现
     * @param flags     （|）或读写标志   默认 入参0时默认用读写方式打开
     * @return -1 不成功 返回 文件描述符 windows下为句柄
     */
    public native static int nativeOpen(String path,int baudrate, int stopBits, int dataBits, int parity, int flowCon,
                                     int flags);

    /**
     * 关闭串口
     * @param fd 打开时获得的文件句柄
     */
    public native static void nativeClose(int fd);

    /**
     * 阻塞写入
     * @param fd 打开时获得的文件句柄
     * @param bytes 要写入的数据
     */
    public native static void nativeWrite(int fd,byte[] bytes);

    /**
     * 阻塞读取串口缓存区全部数据 maxsize 3036
     * @param fd 打开时获得的文件句柄
     * @return 读取到的数据
     */
    public native static byte[] nativeRead(int fd);

    /**
     * 阻塞读取指定字节数据 ,行为类似 InputStream#readNBytes
     * @see java.io.InputStream#readNBytes(int)
     * @param fd 打开时获得的文件句柄
     * @param size 期望读取到的长度
     * @return 读取到的数据
     */
    public native static byte[] nativeReadNBytes(int fd,int size);

    /**
     * 内部使用nativeRead读取全部读取并回调java，多线程读写时可能会粘包。
     * @param fd 打开时获得的文件句柄
     * @param callback 获取到数据时的回调函数
     */
    public native static void nativeReadCallBack(int fd,SerialPortReadCallback callback);

    static {
       // System.out.println(System.getProperty("java.library.path"));
        File file = new File(System.getProperty("user.dir"));
        if(Platform.isWindows){
            System.load(file.getParentFile().getParentFile().getAbsoluteFile() + "\\bin\\modbusRtuHelper.dll");
        } else if (Platform.isLinux) {
            System.load(file.getParentFile().getParentFile().getAbsoluteFile() + "\\bin\\modbusRtuHelper.so");
        }
    }
}
