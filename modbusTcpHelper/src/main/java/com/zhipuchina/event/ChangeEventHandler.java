package com.zhipuchina.event;

/**
 * 监听old到newV的变化，默认AfterEventHandler
 */
public class ChangeEventHandler implements AfterEventHandler{

    private Object old;
    private Object newV;

    private EventHandler handler;

    public ChangeEventHandler(Object old, Object newV, EventHandler handler) {
        this.old = old;
        this.newV = newV;
        this.handler = handler;
    }

    @Override
    public void process(Object oldValue, Object newValue) {
        if (old.toString().equals(oldValue.toString())  && newV.toString().equals(newValue.toString())){
            handler.process();
        }
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }
}
