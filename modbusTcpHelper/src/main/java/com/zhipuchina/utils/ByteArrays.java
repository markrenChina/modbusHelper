package com.zhipuchina.utils;

public class ByteArrays {

    private final static char[] hexChar = new char[] {
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };

    //byte数组 格式化成16进制，方便打印
    public static String toString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        sb.append("HEX:[");
        for (byte b : bytes) {
            int pre = (b & 0xF0) >> 4;
            int last = b & 0x0F;
            sb.append(hexChar[pre]).append(hexChar[last]).append(',');
        }
        if (bytes.length > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(']');
        return sb.toString();
    }

    public static String toString(int[] bytes){
        StringBuilder sb = new StringBuilder();
        sb.append("HEX:[");
        for (int b : bytes) {
            int pre = ((byte)b & 0xF0) >> 4;
            int last = (byte)b & 0x0F;
            sb.append(hexChar[pre]).append(hexChar[last]).append(',');
        }
        if (bytes.length > 0){
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(']');
        return sb.toString();
    }


    public static void main(String[] args) {
        byte[] test1 = new byte[]{};
        System.out.println(toString(test1));
    }
}
