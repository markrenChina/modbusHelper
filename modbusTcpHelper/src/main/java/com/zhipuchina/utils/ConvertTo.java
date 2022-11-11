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

    public static byte[] primitive(Object in){
        return primitive(in.toString());
    }

    public static byte[] primitive(String in){
        try {
            try {
                return primitive(Short.parseShort(in));
            }catch (NumberFormatException e){
                return primitive(Boolean.parseBoolean(in));
            }
        }catch (Exception e){
            e.printStackTrace();
            GlobalLogger.logger.error(e.getLocalizedMessage());
            System.exit(-1);
            return new byte[0];
        }
    }

    public static short getShort(byte[] val){
        if (val.length == 2){
            return getShort(val[0],val[1]);
        }else {
            return (short)(val[0] & 0xFF);
        }
    }

    public static short getShort(byte high ,byte low){
        return (short) (((high&0xFF) << 8) | (low & 0xFF));
    }

    public static int getInteger(byte high ,byte low){
        return (((high&0xFF) << 8) | (low & 0xFF));
    }

    public static boolean getBoolean(byte[] val){
        return (val[0] & 0x01) == 1;
    }
}
