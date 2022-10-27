package com.zhipuchina.function.client;

import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.Buffer;

public class ThreeClientFunctionHandler extends ClientFunctionTemplate {

    @Override
    public void recv(byte[] header, byte[] ADU, Exchange exchange) {
        int count =( ADU.length / 2) - 1;
        byte[] value = new byte[count * 2];
        System.arraycopy(ADU, 2, value, 0, value.length);
        Buffer.setValue(MemoryTypes.HoldingRegister, exchange.getAddress(), count, value);
    }
}