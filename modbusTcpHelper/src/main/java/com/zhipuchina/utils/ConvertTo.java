package com.zhipuchina.utils;

import com.zhipuchina.handler.GlobalLogger;

public class ConvertTo {

    public static byte[] primitive(boolean in) {
        if (in) {
            return new byte[]{0x01};
        } else {
            return new byte[]{0x00};
        }
    }

    public static byte[] primitive(short in) {
        return new byte[] { BitUtil.getInt8To16(in) , BitUtil.getInt0To8(in)};
    }

    public static byte[] primitive(String in){
        try {
            try {
                return primitive(Boolean.parseBoolean(in));
            }catch (ClassCastException e){
                return primitive(Short.parseShort(in));
            }
        }catch (Exception e){
            e.printStackTrace();
            GlobalLogger.logger.error(e.getLocalizedMessage());
            System.exit(-1);
            return new byte[0];
        }
    }

    public static short getShort(byte[] val){
        return getShort(val[0],val[1]);
    }

    public static short getShort(byte v1 ,byte v2){
        return (short) (((v1&0xFF) << 8) | (v2 & 0xFF));
    }

    public static boolean getBoolean(byte[] val){
        return (val[0] & 0x01) == 1;
    }
}
