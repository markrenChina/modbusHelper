package com.zhipuchina.server;

import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.handler.SessionFactory;

import java.net.InetAddress;
import java.net.Socket;

public class ModbusTcpServer extends TcpServer{
    private SessionFactory factory;

    public ModbusTcpServer(InetAddress address, int port, SessionFactory factory) {
        super(address, port);
        this.factory = factory;
    }

    @Override
    public void start() {
        for (;;){
            try {
                Socket sock = socket.accept();
                GlobalLogger.logger.debug("Clint connect");
//                new ModbusTcpBasicHandler(sock).start();
                factory.accept(sock).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
