package com.zhipuchina.client;

import com.zhipuchina.handler.GlobalLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public abstract class TcpClient {

    protected Socket socket;

    public TcpClient(SocketAddress remoteAddress,SocketAddress localAddress) {
        try {
            socket = new Socket();
            if (localAddress != null){
                socket.bind(remoteAddress);
            }
            socket.connect(remoteAddress);
        } catch (IOException e) {
            e.printStackTrace();
            GlobalLogger.logger.info("TcpClient program exit " + e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    public abstract void start();
}
