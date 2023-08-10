package com.zhipuchina.function.client;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.function.FunctionController;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.ConvertTo;

public abstract class ClientFunctionTemplate implements FunctionController {

    public abstract void recv(byte[] header, byte[] ADU, Exchange exchange) throws ModbusException;

    @Override
    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) throws ModbusException {
        int id = ConvertTo.getInteger(header[0], header[1]);
        Exchange exchange = session.getConcurrentMap().get(id);
        if (exchange == null) {
            return null;
        }
        int funCode = (ADU[0] & 0xFF);
        if (funCode >= 85){
            exchange.getResult().add(0);
        }

        recv(header, ADU, exchange);
        if (exchange.getLatch() != null) {
            exchange.getLatch().countDown();
        }
        if (exchange.getCallback() != null){
            ModbusExecutors.exec(() -> exchange.getCallback().process(exchange.getResult()));
        }
        return null;
    }
}
