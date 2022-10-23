package com.zhipuchina.event;

import com.zhipuchina.model.Memory;
import com.zhipuchina.model.MemoryTypes;

import java.util.HashMap;

public class EventManager {
    private static final HashMap<Integer,EventHandler> events = new HashMap<>();

    public static void register(MemoryTypes type, int offset,EventHandler handler){
        Integer key = type.getCode() * 10000 + offset;
        register(key,handler);
    }

    public static void register(Integer key,EventHandler handler){
        events.put(key,handler);
    }

    public static EventHandler getEvent(MemoryTypes type, int offset){
        Integer key = type.getCode() * 10000 + offset;
        return getEvent(key);
    }

    public static EventHandler getEvent(Integer key){
        return events.get(key);
    }
}
