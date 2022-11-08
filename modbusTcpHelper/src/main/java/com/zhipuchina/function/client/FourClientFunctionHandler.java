package com.zhipuchina.function.client;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.ConvertTo;

public class FourClientFunctionHandler extends ClientFunctionTemplate {

    @Override
    public void recv(byte[] header, byte[] ADU, Exchange exchange) throws ModbusException {
        int count =( ADU.length / 2) - 1;
        byte[] value = new byte[count * 2];
        System.arraycopy(ADU, 2, value, 0, value.length);
        //Buffer.setValue(MemoryTypes.InputRegister, exchange.getAddress(), count, value);
        exchange.getResult().add(ConvertTo.getShort(value));
    }
}
