package com.zhipuchina.model;

import com.zhipuchina.event.EventManager;
import com.zhipuchina.exception.IllegalDataAddressException;
import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exception.ModbusExceptionFactory;
import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.utils.BitUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

    public static int getValue(int pos) {
        Memory buffer = getBuffer(pos);
        return buffer.getValue(pos);
    }

    public static int[] getValue(int pos, int count) throws ModbusException {
        Memory buffer = getBuffer(pos);
        return buffer.getValue(pos, count);
    }

    public static List<Integer>  getValueAsInt(int pos, int count) throws ModbusException {
        Memory buffer = getBuffer(pos);
        return buffer.getValueAsInt(pos, count);
    }

    public static int getValue(MemoryTypes type, int offset) {
        return getValue(type.getCode() * 10000 + offset);
    }

    public static int[] getValue(MemoryTypes type, int offset, int count) throws ModbusException {
        return getValue(type.getCode() * 10000 + offset, count);
    }

    public static String getString(MemoryTypes type, int offset, int count) throws ModbusException {
        int[] value = getValue(type.getCode() * 10000 + offset, count);
        int i = 0;
        for (; i < value.length; i++) {
            if (BitUtil.getInt8To16(value[i]) == '\0' || BitUtil.getInt0To8(value[i]) == '\0'){
                break;
            }
        }
        value = Arrays.copyOf(value,i);
        return new String(BitUtil.intLow2ByteToByteArray(value),StandardCharsets.US_ASCII);
    }

    public static void setString(MemoryTypes type, int offset,int count ,String val) throws ModbusException {
        if (type == MemoryTypes.HoldingRegister || type == MemoryTypes.InputRegister){
            int[] value = BitUtil.byteArray2ByteTo1IntArray(val.getBytes(StandardCharsets.US_ASCII));
            value = Arrays.copyOf(value,count);
            setValue(type,offset,value.length,value);
        }else {
            throw new IllegalDataAddressException();
        }
    }

    public static List<Integer> getValueAsInt(MemoryTypes type, int offset, int count) throws ModbusException {
        return getValueAsInt(type.getCode() * 10000 + offset, count);
    }

    public static void setValue(int pos, Integer val) throws ModbusException {
        Memory buffer = getBuffer(pos);
        int oldValue = buffer.getValue(pos);
        if (EventManager.isNeedAsync(pos, 1)) {
            ModbusExecutors.exec(() -> {
                EventManager.doBeforeEvent(pos, oldValue, val);
                buffer.setValue(pos, val);
                EventManager.doAfterEvent(pos, oldValue, val);
            });
            throw ModbusExceptionFactory.create(5);
        }

        EventManager.doBeforeEvent(pos, oldValue, val);
        buffer.setValue(pos, val);
        EventManager.doAfterEvent(pos, oldValue, val);
    }

    public static void setValue(int pos, boolean val) throws ModbusException {
        setValue(pos, val ? 1 : 0);
    }

    public static void setValue(int pos, int count, int[] val) throws ModbusException {
        Memory buffer = getBuffer(pos);
        int[] oldValue = buffer.getValue(pos, count);
        if (EventManager.isNeedAsync(pos, count)){
            ModbusExecutors.exec(() -> {
                EventManager.doBeforeEvent(pos, count, oldValue, val);
                buffer.setValue(pos, count, val);
                EventManager.doAfterEvent(pos, count, oldValue, val);
            });
            throw ModbusExceptionFactory.create(5);
        }
        EventManager.doBeforeEvent(pos, count, oldValue, val);
        buffer.setValue(pos, count, val);
        EventManager.doAfterEvent(pos, count, oldValue, val);
    }

    public static void setValue(MemoryTypes type, int offset, Integer val)throws ModbusException {
        setValue(type.getCode() * 10000 + offset, val);
    }

    public static void setValue(MemoryTypes type, int offset, boolean val)throws ModbusException {
        setValue(type.getCode() * 10000 + offset, val);
    }

    public static void setValue(MemoryTypes type, int offset, int count, int[] val) throws ModbusException {
        setValue(type.getCode() * 10000 + offset, count, val);
    }

    public static int count(int endPos, int startPos) {
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

    public static void main(String[] args) throws ModbusException {
        malloc(MemoryTypes.HoldingRegister,1,15);
        setString(MemoryTypes.HoldingRegister,1,15,"hz10000021255");
        System.out.println(getString(MemoryTypes.HoldingRegister,1,15));
    }
}

