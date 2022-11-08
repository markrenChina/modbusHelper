package com.zhipuchina;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ErrorTest extends TestCase {

    public void testError() throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("127.0.0.1", 8888));
        socket.getOutputStream().write(new byte[]{0x00,0x00,0x00,0x00,0x00,0x06,0x00,0x09,0x00,0x01,0x00,0x01});
        socket.getOutputStream().flush();
        socket.close();
    }
}
