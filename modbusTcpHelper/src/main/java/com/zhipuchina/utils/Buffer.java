package com.zhipuchina.utils;

import com.zhipuchina.event.AfterEventHandler;
import com.zhipuchina.event.BeforeEventHandler;
import com.zhipuchina.event.EventManager;
import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.model.*;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private static Coil coilBuffer = new Coil(null);
    private static Register registerBuffer = new Register(null);

    private static Memory getBuffer(int startPos, int size) {
        if (startPos / 10000 <= 1 && (startPos + size) / 10000 <= 1) {
            return coilBuffer;
        } else if (startPos / 10000 >= 3 && startPos / 10000 <= 4
                && (startPos + size) / 10000 >= 3 && (startPos + size) / 10000 <= 4) {
            return registerBuffer;
        }
        throw new RuntimeException("illegal address");
    }

    private static Memory getBuffer(int pos) {
        if (pos / 10000 <= 1) {
            return coilBuffer;
        } else if (pos / 10000 >= 3 && pos / 10000 <= 4) {
            return registerBuffer;
        }
        throw new RuntimeException("illegal address");
    }

    //TODO 支持从中间开始开辟
    public static void malloc(int startPos, int size) {
        //malloc(MemoryTypes.code2MemoryTypes(startPos / 10000), size);
        Memory buffer = getBuffer(startPos, size);
        buffer.malloc(startPos, size);
    }

    public static void malloc(MemoryTypes type, int offset, int size) {
        malloc(type.getCode() * 10000 + offset, size);
    }

    public static byte[] getValue(int pos) {
        Memory buffer = getBuffer(pos);
        return buffer.getValue(pos);
    }

    public static byte[] getValue(int pos, int count) {
        Memory buffer = getBuffer(pos);
        return buffer.getValue(pos,count);
    }

    public static byte[] getValue(MemoryTypes type, int offset) {
        return getValue(type.getCode() * 10000 + offset);
    }

    public static byte[] getValue(MemoryTypes type, int offset, int count) {
        return getValue(type.getCode() * 10000 + offset,count);
    }



    public static void setValue(int pos, byte[] val) {
        Memory buffer = getBuffer(pos);
        byte[] oldValue = buffer.getValue(pos);
        EventManager.doBeforeEvent(pos, oldValue, val);
        buffer.setValue(pos, val);
        EventManager.doAfterEvent(pos, oldValue, val);
    }

    public static void setValue(int pos, boolean val) {
        setValue(pos, ConvertTo.primitive(val));
    }

    public static void setValue(int pos, int count, byte[] val) {
        Memory buffer = getBuffer(pos);
        byte[] oldValue = buffer.getValue(pos, count);
        EventManager.doBeforeEvent(pos,count,oldValue, val);
        buffer.setValue(pos, count, val);
        EventManager.doAfterEvent(pos,count,oldValue, val);
    }

    public static void setValue(MemoryTypes type, int offset, byte[] val) {
        setValue(type.getCode() * 10000 + offset, val);
    }

    public static void setValue(MemoryTypes type, int offset, boolean val) {
        setValue(type.getCode() * 10000 + offset, val);
    }

    public static void setValue(MemoryTypes type, int offset, int count, byte[] val) {
        setValue(type.getCode() * 10000 + offset, count, val);
    }

    public static int count(int endPos, int startPos){
        assert endPos >= startPos;
        return endPos - startPos + 1;
    }

//    public static String getString(Class<? extends Register> type, int offset, int length) {
//        StringBuilder sb = new StringBuilder(length);
//        if (type == InputRegister.class) {
//            for (int i = 0; i < length; i++) {
//                Object obj = inputRegisterBuffer.get(offset + i).getValue();
//                int now = Integer.parseInt(obj.toString());
//                int hByte = (now >> 8) & 0xFF;
//                int lByte = now & 0xFF;
//                if (hByte != 0) {
//                    sb.append((char) hByte);
//                } else {
//                    break;
//                }
//                if (lByte != 0) {
//                    sb.append((char) lByte);
//                } else {
//                    break;
//                }
//            }
//        } else {
//            for (int i = 0; i < length; i++) {
//                Object obj = outputRegisterBuffer.get(offset + i).getValue();
//                int now = Integer.parseInt(obj.toString());
//                int hByte = (now >> 8) & 0xFF;
//                int lByte = now & 0xFF;
//                if (hByte != 0) {
//                    sb.append((char) hByte);
//                } else {
//                    break;
//                }
//                if (lByte != 0) {
//                    sb.append((char) lByte);
//                } else {
//                    break;
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    public static void setString(int offset, String val) {
//        for (int i = 0; i * 2 < val.length(); i++) {
//            char hChar = val.charAt(i * 2);
//            int v = hChar << 8;
//            if (i * 2 + 1 < val.length()) {
//                char lChar = val.charAt(i * 2 + 1);
//                v += lChar;
//            }
//            outputRegisterBuffer.get(offset + i).setValue((short) v);
//        }
//    }
}

