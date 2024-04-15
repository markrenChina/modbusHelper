package com.zhipuchina.event;

import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.model.MemoryTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventManager {
    private static final HashMap<Integer, BeforeEventHandler> bEvents = new HashMap<>();
    private static final HashMap<Integer, List<AfterEventHandler>> aEvents = new HashMap<>();

    public static void register(MemoryTypes type, int offset, BeforeEventHandler handler) {
        Integer key = type.getCode() * 10000 + offset;
        register(key, handler);
    }

    public static void register(Integer key, BeforeEventHandler handler) {
        bEvents.put(key, handler);
    }

    public static void register(MemoryTypes type, int offset, AfterEventHandler handler) {
        Integer key = type.getCode() * 10000 + offset;
        register(key, handler);
    }

    public static void register(Integer key, AfterEventHandler handler) {
        if (aEvents.containsKey(key)){
            aEvents.get(key).add(handler);
        }else {
            List<AfterEventHandler> list = new ArrayList<>();
            list.add(handler);
            aEvents.put(key, list);
        }
    }

    public static BeforeEventHandler getBeforeEvent(MemoryTypes type, int offset) {
        Integer key = type.getCode() * 10000 + offset;
        return getBeforeEvent(key);
    }

    public static BeforeEventHandler getBeforeEvent(Integer key) {
        return bEvents.get(key);
    }

    public static List<AfterEventHandler> getAfterEvent(MemoryTypes type, int offset) {
        Integer key = type.getCode() * 10000 + offset;
        return getAfterEvent(key);
    }

    public static List<AfterEventHandler> getAfterEvent(Integer key) {
        return aEvents.get(key);
    }

    public static boolean isNeedAsync(int pos, int count){
        boolean res = false;
        for (int i = 0; i < count; i++) {
            BeforeEventHandler beforeEvent = getBeforeEvent(pos + i);
            res |= (beforeEvent != null && beforeEvent.isNeedAsync());
        }
        return res;
    }

    public static void doBeforeEvent(int pos, Integer oldValue, Integer newValue)  {
        GlobalLogger.logger.debug("doBeforeEvent pos = "+pos+ ",old val =" + oldValue +",new val="+ newValue);
        BeforeEventHandler beforeEvent = getBeforeEvent(pos);
        if (beforeEvent != null){
            beforeEvent.process(oldValue,newValue);
        }
    }

    public static void doBeforeEvent(int pos, int count, int[] oldValue, int[] newValue){
        for (int i = 0; i < count; i++) {
            doBeforeEvent(pos + i ,oldValue[i] , newValue[i]);
        }
    }

    public static void doAfterEvent(int pos, int oldValue, int newValue) {
        List<AfterEventHandler> afterEvents = getAfterEvent(pos);
        if (afterEvents != null){
            ModbusExecutors.exec(() -> {
                for (AfterEventHandler event : afterEvents) {
                    event.process(oldValue, newValue);
                }
            });
        }
    }

    public static void doAfterEvent(int pos, int count, int[] oldValue, int[] newValue) {
        for (int i = 0; i < count; i++) {
            doAfterEvent(pos + i, oldValue[i], newValue[i]);
        }
    }
}
