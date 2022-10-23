package com.zhipuchina.handler;

import java.net.Socket;

public interface SessionFactory {

    default public ModbusTcpBasicSession accept(Socket socket){
        return new ModbusTcpBasicSession(socket);
    }
}
