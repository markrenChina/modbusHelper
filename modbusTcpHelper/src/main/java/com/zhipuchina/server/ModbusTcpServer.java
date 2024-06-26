package com.zhipuchina.server;

import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.handler.DefaultServerSessionFactoryImp;
import com.zhipuchina.handler.GlobalLogger;
import com.zhipuchina.handler.SessionFactory;

import java.net.InetAddress;
import java.net.Socket;

public class ModbusTcpServer extends TcpServer {
    private final SessionFactory factory;

    public ModbusTcpServer(InetAddress address, int port) {
        this(address, port,null);
    }

    public ModbusTcpServer(InetAddress address, int port, SessionFactory factory) {
        super(address, port);
        this.factory = factory == null ? new DefaultServerSessionFactoryImp() : factory;
    }

    @Override
    public void start() {
        ModbusExecutors.exec(() -> {
            for (; ; ) {
                try {
                    Socket sock = socket.accept();
                    GlobalLogger.logger.debug("Clint connect");
//                new ModbusTcpBasicHandler(sock).start();
                    //todo session 放入容器
                    ModbusExecutors.exec(factory.accept(sock));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
