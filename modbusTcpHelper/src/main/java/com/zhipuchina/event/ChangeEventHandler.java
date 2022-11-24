package com.zhipuchina.event;

/**
 * 监听old到newV的变化，默认AfterEventHandler
 */
public class ChangeEventHandler implements AfterEventHandler{

    private Integer old;
    private Integer newV;

    private EventHandler handler;

    //入参 null 表示 any
    public ChangeEventHandler(Integer old, Integer newV, EventHandler handler) {
        this.old = old;
        this.newV = newV;
        this.handler = handler;
    }

    //入参 null 表示 any
    public ChangeEventHandler(Boolean old, Boolean newV, EventHandler handler) {
        this.old = old ? 1 : 0;
        this.newV = newV ? 1 : 0;
        this.handler = handler;
    }

    @Override
    public void process(int oldValue, int newValue) {
        if ( (old == null || old.equals(oldValue))
                && (newV == null || newV.equals(newValue))){
            handler.process();
        }
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }
}
