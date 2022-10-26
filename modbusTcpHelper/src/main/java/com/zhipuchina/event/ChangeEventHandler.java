package com.zhipuchina.event;

import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.ConvertTo;

import java.util.Arrays;

/**
 * 监听old到newV的变化，默认AfterEventHandler
 */
public class ChangeEventHandler implements AfterEventHandler{

    private byte[] old;
    private byte[] newV;

    private EventHandler handler;

    public ChangeEventHandler(Integer old, Integer newV, EventHandler handler) {
        this.old = new byte[]{BitUtil.getInt8To16(old),BitUtil.getInt0To8(old) };
        this.newV = new byte[]{BitUtil.getInt8To16(newV),BitUtil.getInt0To8(newV) };
        this.handler = handler;
    }

    public ChangeEventHandler(Boolean old, Boolean newV, EventHandler handler) {
        this.old = ConvertTo.primitive(old);
        this.newV = ConvertTo.primitive(newV);
        this.handler = handler;
    }

    @Override
    public void process(Object oldValue, Object newValue) {
        if (Arrays.equals(old,ConvertTo.primitive(oldValue.toString()))
                && Arrays.equals(newV,ConvertTo.primitive(newValue.toString()))){
            handler.process();
        }
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }
}
