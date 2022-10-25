package com.zhipuchina.handler;

import com.zhipuchina.function.*;
import com.zhipuchina.utils.FunctionControllerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ModbusTcpBasicSession implements Runnable {
    private final Socket socket;
    private InputStream in = null;
    private OutputStream out = null;

    private final FunctionController[] functionControllers;

    public ModbusTcpBasicSession(Socket socket,FunctionController[] functionControllers) {
        this.socket = socket;
        this.functionControllers = functionControllers;
    }


    //todo 切换functionController
    public void changeFunctionController(int functionCode, FunctionController function) {
        functionControllers[functionCode] = function;
    }

    protected void testHook() {
    };

    //被动  in -> out
    @Override
    public void run() {
        byte[] header = new byte[7];
        byte[] ADU;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            while (!socket.isClosed()) {
                int readLen = in.readNBytes(header, 0, 7);
                if (readLen != 7) {
                    close();
                    break;
                }
                int transActionId = ((header[0] & 0xFF) << 8) | (header[1] & 0xFF);
                GlobalLogger.logger.debug("transActionId " + transActionId);
                int protocal = ((header[2] & 0xFF) << 8) | header[3];
                GlobalLogger.logger.debug("protocal " + protocal);
                if (protocal != 0) {
                    System.err.println("protocal != 0 close accept");
                    close();
                    break;
                }
                int frameLen = ((header[4] & 0xFF) << 8) | header[5];
                GlobalLogger.logger.debug("frameLen " + frameLen);
                ADU = new byte[frameLen - 1];
                int len = in.readNBytes(ADU, 0, frameLen - 1);
                if (len != frameLen - 1) {
                    System.err.println("len != frameLen close accept ");
                    close();
                    break;
                }
                int slaveId = header[6];
                GlobalLogger.logger.debug("slaveId " + slaveId);
                int functionCode = ADU[0];
                GlobalLogger.logger.debug("functionCode " + functionCode);

                if (functionControllers[functionCode] != null){
                    byte[] outValue = functionControllers[functionCode].serve(header,ADU);
                    if (outValue != null){
                        //写单元标识符
                        outValue[6] = (byte) slaveId;
                        //outValue[4] outValue[5] 写长度
                        int outLen = outValue.length - 6;
                        outValue[5] = (byte) (outLen & 0xFF);
                        outValue[4] = (byte) ((outLen >> 8) & 0xFF);
                        System.arraycopy(header, 0, outValue, 0, 4);
                        out.write(outValue);
                    }
                }else {
                    GlobalLogger.logger.debug("UnsupportedOperationException functionCode " + functionCode);
                }
                ADU = null;
            }
        } catch (Exception e) {
            close();
            throw new RuntimeException(e);
        }
    }

    private void close() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
