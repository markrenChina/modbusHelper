package com.zhipuchina.event;

/**
 * 监听old到newV的变化，默认AfterEventHandler
 */
public class ChangeEventHandler implements AfterEventHandler{

    private Integer old;
    private Integer newV;

    private EventHandler handler;

    public ChangeEventHandler(Integer old, Integer newV, EventHandler handler) {
        this.old = old;
        this.newV = newV;
        this.handler = handler;
    }

    public ChangeEventHandler(Boolean old, Boolean newV, EventHandler handler) {
        this.old = old ? 1 : 0;
        this.newV = newV ? 1 : 0;
        this.handler = handler;
    }

    @Override
    public void process(Integer oldValue, Integer newValue) {
        if (old.equals(oldValue) && newV.equals(newValue)){
            handler.process();
        }
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }
}
