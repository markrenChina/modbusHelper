package com.zhipuchina.utils;

import com.zhipuchina.event.EventHandler;
import com.zhipuchina.event.EventManager;
import com.zhipuchina.model.*;

import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private static List<InputCoil> inputCoilBuffer = new ArrayList<>();
    private static List<OutputCoil> outputCoilBuffer = new ArrayList<>();
    private static List<InputRegister> inputRegisterBuffer = new ArrayList<>();
    private static List<OutputRegister> outputRegisterBuffer = new ArrayList<>();

    //TODO 支持从中间开始开辟
    public static void malloc(int startPos, int size) {
        malloc(MemoryTypes.code2MemoryTypes( startPos / 10000),size);
    }

    public static void malloc(MemoryTypes type, int size) {
        if (type == MemoryTypes.InputCoil) {
            for (int i = 0; i < size; i++) {
                inputCoilBuffer.add(new InputCoil());
            }
        } else if (type == MemoryTypes.OutputCoil) {
            for (int i = 0; i < size; i++) {
                outputCoilBuffer.add(new OutputCoil());
            }
        } else if (type == MemoryTypes.InputRegister) {
            for (int i = 0; i < size; i++) {
                inputRegisterBuffer.add(new InputRegister());
            }
        } else {
            for (int i = 0; i < size; i++) {
                outputRegisterBuffer.add(new OutputRegister());
            }
        }
    }

    public static Object getValue(int pos) {
        return getValue(MemoryTypes.code2MemoryTypes( pos / 10000),pos%10000);
    }
    public static Object getValue(MemoryTypes type, int offset) {
        if (type == MemoryTypes.InputCoil) {
            return inputCoilBuffer.get(offset).getValue();
        } else if (type == MemoryTypes.OutputCoil) {
            return outputCoilBuffer.get(offset).getValue();
        } else if (type == MemoryTypes.OutputRegister) {
            return outputRegisterBuffer.get(offset).getValue();
        } else {
            return inputRegisterBuffer.get(offset).getValue();
        }
    }


    public static void setValue(int pos,Object val) {
        setValue(MemoryTypes.code2MemoryTypes( pos / 10000),pos%10000,val);
    }
    public static void setValue(MemoryTypes type, int offset,Object val) {
        EventHandler event = EventManager.getEvent(type, offset);
        //todo 异步回调
        if (event != null){
            event.beforeProcess(getValue(type,offset),val);
        }
        if (type == MemoryTypes.OutputCoil) {
            outputCoilBuffer.get(offset).setValue(Boolean.valueOf(val.toString()));
        } else {
            outputRegisterBuffer.get(offset).setValue(Short.parseShort(val.toString()) );
        }
        //todo 异步回调
        if (event != null){
            event.afterProcess(getValue(type,offset),val);
        }
    }

    public static String getString(Class<? extends Register> type,int offset,int length) {
        StringBuilder sb = new StringBuilder(length);
        if (type == InputRegister.class){
            for (int i = 0; i < length; i++) {
                Object obj= inputRegisterBuffer.get(offset + i).getValue();
                int now = Integer.parseInt(obj.toString());
                int hByte = (now >> 8) & 0xFF;
                int lByte = now & 0xFF;
                if (hByte != 0){
                    sb.append((char) hByte);
                }else {
                    break;
                }
                if (lByte != 0){
                    sb.append((char) lByte);
                }else {
                    break;
                }
            }
        } else  {
            for (int i = 0; i < length; i++) {
                Object obj= outputRegisterBuffer.get(offset + i).getValue();
                int now = Integer.parseInt(obj.toString());
                int hByte = (now >> 8) & 0xFF;
                int lByte = now & 0xFF;
                if (hByte != 0){
                    sb.append((char) hByte);
                }else {
                    break;
                }
                if (lByte != 0){
                    sb.append((char) lByte);
                }else {
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static void setString(int offset,String val) {
        for (int i = 0; i*2 < val.length(); i++) {
            char hChar= val.charAt(i * 2);
            int v = hChar << 8;
            if (i *2 +1 < val.length()){
                char lChar = val.charAt(i*2+1);
                v+= lChar;
            }
            outputRegisterBuffer.get(offset+i).setValue((short)v);
        }
    }
}

