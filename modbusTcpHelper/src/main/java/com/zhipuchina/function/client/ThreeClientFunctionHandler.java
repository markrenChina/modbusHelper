package com.zhipuchina.function.client;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.model.Buffer;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.ConvertTo;

//import java.util.Arrays;
//import java.util.stream.Collectors;


public class ThreeClientFunctionHandler extends ClientFunctionTemplate {

    @Override
    public void recv(byte[] header, byte[] ADU, Exchange exchange) throws ModbusException {
        int count =( ADU.length / 2) - 1;
        int[] value = new int[count];
//        System.arraycopy(ADU, 2, value, 0, value.length);
        for (int i = 0; i < count; i++) {
            value[i] = ConvertTo.getInteger(ADU[2+i*2],ADU[3+i*2]);
        }
        Buffer.setValue(MemoryTypes.HoldingRegister, exchange.getAddress(), count, value);
        //exchange.getResult().addAll(Arrays.stream(value).boxed().collect(Collectors.toList()));
        for (int obj : value) {
            exchange.getResult().add(obj);
        }
    }
}
