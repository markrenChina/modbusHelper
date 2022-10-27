package com.zhipuchina.function.client;

import com.zhipuchina.function.FunctionController;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.ConvertTo;

public abstract class ClientFunctionTemplate implements FunctionController {

    public abstract void recv(byte[] header, byte[] ADU, Exchange exchange);

    @Override
    public byte[] serve(byte[] header, byte[] ADU, ModbusTcpBasicSession session) {
        int id = ConvertTo.getInteger(header[0], header[1]);
        var exchange = session.getConcurrentMap().get(id);
        if (exchange == null) {
            return null;
        }

        recv(header, ADU, exchange);

        if (exchange.getLatch() != null) {
            exchange.getLatch().countDown();
        }
        return null;
    }
}
