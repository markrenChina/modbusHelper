package com.zhipuchina;

import com.zhipuchina.handler.SessionFactory;
import com.zhipuchina.handler.ModbusTcpBasicSession;

import java.net.Socket;

public class MySessionFactory implements SessionFactory {

    @Override
    public ModbusTcpBasicSession accept(Socket socket) {
        return new MyModbusTcpSession(socket);
    }
}
