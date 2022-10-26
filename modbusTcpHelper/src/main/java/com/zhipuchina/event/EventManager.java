package com.zhipuchina.event;

import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.model.Memory;
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
        List<AfterEventHandler> events = aEvents.computeIfAbsent(key, k -> new ArrayList<>());
        events.add(handler);
        //System.out.println(events);
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

    public static void doBeforeEvent(int pos, byte[] oldValue, byte[] newValue) {
        BeforeEventHandler beforeEvent = getBeforeEvent(pos);
        if (beforeEvent != null){
            beforeEvent.process(oldValue,newValue);
        }
    }

    public static void doBeforeEvent(int pos, int count, byte[] oldValue, byte[] newValue) {
        for (int i = 0; i < count; i++) {
            doBeforeEvent(pos + i ,oldValue , newValue);
        }
    }

    public static void doAfterEvent(int pos, byte[] oldValue, byte[] newValue) {
        List<AfterEventHandler> afterEvents = getAfterEvent(pos);
        if (afterEvents != null){
            ModbusExecutors.exec(() -> {
                for (AfterEventHandler event : afterEvents) {
                    event.process(oldValue, newValue);
                }
            });
        }
    }

    public static void doAfterEvent(int pos, int count, byte[] oldValue, byte[] newValue) {
        for (int i = 0; i < count; i++) {
            doAfterEvent(pos + i,
                    new byte[]{oldValue[i * 2], oldValue[i * 2 + 1]},
                    new byte[]{newValue[i * 2], newValue[i * 2 + 1]});
        }
    }
}
