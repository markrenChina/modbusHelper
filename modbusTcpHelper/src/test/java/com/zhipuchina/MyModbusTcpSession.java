package com.zhipuchina;

import com.zhipuchina.handler.ModbusTcpBasicSession;

import java.net.Socket;

public class MyModbusTcpSession extends ModbusTcpBasicSession {
    public MyModbusTcpSession(Socket socket) {
        super(socket);
    }

    @Override
    protected void testHook() {
        System.err.println("test Hook");
    }
}
