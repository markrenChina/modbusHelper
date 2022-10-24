package com.zhipuchina.server;

import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.handler.SessionFactory;

import java.net.InetAddress;
import java.net.Socket;

public class ModbusTcpServer extends TcpServer {
    private SessionFactory factory;

    public ModbusTcpServer(InetAddress address, int port, SessionFactory factory) {
        super(address, port);
        this.factory = factory;
    }

    @Override
    public void start() {
        ModbusExecutors.exec(() -> {
            for (; ; ) {
                try {
                    Socket sock = socket.accept();
                    GlobalLogger.logger.debug("Clint connect");
//                new ModbusTcpBasicHandler(sock).start();
                    ModbusExecutors.exec(factory.accept(sock));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
