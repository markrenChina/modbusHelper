package com.zhipuchina.function.client;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.pojo.Exchange;

public class TwoClientFunctionHandler extends ClientFunctionTemplate{
    @Override
    public void recv(byte[] header, byte[] ADU, Exchange exchange) throws ModbusException {
        int count = exchange.getCount();
        //[-124, -128]
        //Buffer.setValue(MemoryTypes.OutputCoil,exchange.getAddress(),count,value);
        for (int i = 0; i < (count/8 +1); i++) {
            int v = ADU[i+2] & 0xFF;
            for (int j = 0; j < ((i == count/8) ? count%8 : 8); j++) {
                boolean vv = ((v &0x01) == 0x01);
                //Buffer.setValue(MemoryTypes.OutputCoil,exchange.getAddress() + i*8 + j,vv);
                exchange.getResult().add(vv? 1 : 0);
                v = v >> 1;
            }
        }
    }
}
