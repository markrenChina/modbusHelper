package com.zhipuchina.client;

import java.io.IOException;
import java.io.OutputStream;

public class SyncOutputStream  {

    OutputStream outputStream;

    public SyncOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public synchronized void writeAndFlush(byte[] b) throws IOException {
        outputStream.write(b);
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
