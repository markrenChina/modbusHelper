package com.zhipuchina.utils;

public class BitUtil {
    //byte转int 无符号转化 &0xFF
    public static byte getInt0To8(int value) {
        return (byte) (value);
    }
    public static byte getInt8To16(int value) {
        return (byte) (value >> 8);
    }

    public static int[] zeroHelper = new int[]{
            0x7F, 0xBF, 0xDF, 0xEF,
            0xF7, 0xFB, 0xFD, 0xFE
    };

    public static byte[] intLow2ByteToByteArray(int[] val){
        byte[] res = new byte[val.length * 2];
        for (int i = 0; i < val.length ; i++) {
            res[i*2] = getInt8To16(val[i]);
            res[i*2 +1] = getInt0To8(val[i]);
        }
        return res;
    }

    public static int[] byteArray2ByteTo1IntArray(byte[] val){
        int alone =  val.length % 2;
        int[] res = new int[val.length / 2 + alone];
        for (int i = 0; i < res.length - 1 ; i++) {
            res[i] = ConvertTo.getInteger(val[i*2],val[i*2+1]);
        }
        if (alone == 0){
            res[res.length - 1] = ConvertTo.getInteger(val[val.length-2],val[val.length -1]);
        }else{
            res[res.length - 1] = ConvertTo.getInteger(val[val.length-1],(byte) 0x00);
        }
        return res;
    }

    /**
     * count 范围1~8，byte从左往右算起
     */
    public static byte setBit(byte src, int count, int val) {
        if (val == 1) {
            return (byte) (1 << (8 - count) | src);
        } else {
            //需要and 只有位置0其他都是1
            return (byte) (zeroHelper[count-1] & src);
        }
    }

    /**
     * count 范围1~8，byte从左往右算起
     * @return 1 true 0 false
     */
    public static boolean getBit(byte src, int count) {
        int tmp = src & 0xFF;
        tmp = tmp >> (8-count);
        return (tmp & 0x01) == 0x01;
    }

    public static byte reversal(byte val){
        int tmp = val & 0xFF;
        int res = 0;
        for (int i = 1; i < 8; i++) {
            int lastValue =  tmp & 0x01;
            res |= lastValue;
            tmp = tmp >> 1;
            res = res << 1;
        }
        int lastValue =  tmp & 0x01;
        res |= lastValue;
        return (byte) res;
    }

    public static void main(String[] args) {
        //0xFE 最后一位置1，应该是0xFF
        byte v1 = setBit((byte) 0xFE,8,1);
        System.out.println(v1 == (byte) 0xFF);
        //0xFF 最后一位置0，应该是0xFE
        byte v2 = setBit((byte) 0xFF,8,0);
        System.out.println(v2 == (byte) 0xFE);
        //0x7F 第一位置1，应该是0xFF
        byte v3 = setBit((byte) 0x7F,1,1);
        System.out.println(v3 == (byte) 0xFF);
        //0xFF 第一位置0，应该是0x7F
        byte v4 = setBit((byte) 0xFF,1,0);
        System.out.println(v4 == (byte) 0x7F);

        byte v5 = setBit((byte) 0xE9,4,1);
        System.out.println(v5 == (byte) 0xF9);
    }
}
