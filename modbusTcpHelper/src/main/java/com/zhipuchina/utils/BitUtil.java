package com.zhipuchina.utils;

public class BitUtil {
    //byte转int 无符号转化 &0xFF
    public static byte getInt0To8(int value) {
        return (byte) (value);
    }
    public static byte getInt8To16(int value) {
        return (byte) (value >> 8);
    }
}
