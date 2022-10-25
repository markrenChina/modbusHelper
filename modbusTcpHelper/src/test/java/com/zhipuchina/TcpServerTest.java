package com.zhipuchina;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServerTest {

    protected ServerSocket socket;

    public TcpServerTest(InetAddress address, int port) {
        try {
            socket = new ServerSocket(port,10,address);
        } catch (IOException e) {
            System.exit(-1);
        }
    }

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            for (; ; ) {
                try {
                    Socket sock = socket.accept();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getProcessID());
        TcpServerTest server = new TcpServerTest(InetAddress.getByName("127.0.0.1"), 8885);
        server.start();
    }

    public static final int getProcessID() {

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        System.out.println(runtimeMXBean.getName());

        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);

    }

}
