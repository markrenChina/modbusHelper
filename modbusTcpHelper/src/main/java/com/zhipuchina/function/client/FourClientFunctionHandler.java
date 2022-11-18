package com.zhipuchina.function.client;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.ConvertTo;

import java.util.Arrays;

public class FourClientFunctionHandler extends ClientFunctionTemplate {

    @Override
    public void recv(byte[] header, byte[] ADU, Exchange exchange) throws ModbusException {
        int count =( ADU.length / 2) - 1;
        Integer[] value = new Integer[count];
        for (int i = 0; i < count; i++) {
            value[i] = ConvertTo.getInteger(ADU[2+i*2],ADU[3+i*2]);
        }
//        System.arraycopy(ADU, 2, value, 0, value.length);
        //Buffer.setValue(MemoryTypes.InputRegister, exchange.getAddress(), count, value);
        exchange.getResult().addAll(Arrays.asList(value));
    }
}
