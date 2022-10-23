package com.zhipuchina.server;

import com.zhipuchina.handler.GlobalLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

abstract class TcpServer {

    protected ServerSocket socket;

    public TcpServer(InetAddress address, int port) {
        try {
            socket = new ServerSocket(port,10,address);
        } catch (IOException e) {
            GlobalLogger.logger.info("program exit");
            System.exit(-1);
        }
    }

    public abstract void start();
}
