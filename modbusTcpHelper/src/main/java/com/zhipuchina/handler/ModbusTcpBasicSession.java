package com.zhipuchina.handler;

import com.zhipuchina.client.SyncOutputStream;
import com.zhipuchina.event.ReadCallback;
import com.zhipuchina.exception.IllegalFunctionException;
import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.function.FinallyInterceptor;
import com.zhipuchina.function.FunctionController;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.ByteArrays;
import com.zhipuchina.utils.ConvertTo;
import com.zhipuchina.utils.InputStreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

//todo 超时
public class ModbusTcpBasicSession implements Runnable {
    private final Socket socket;
    private InputStream in = null;
    private SyncOutputStream out = null;

    private int slaveId;
    private boolean isIgnoreSlaveId = false;

    private final FunctionController[] functionControllers;
    private FinallyInterceptor finallyInterceptor = null;

    private final ConcurrentMap<Integer, Exchange> concurrentMap = new ConcurrentHashMap<>();

    public ModbusTcpBasicSession(Socket socket, Integer slaveId, FunctionController[] functionControllers) {
        this.socket = socket;
        this.slaveId = slaveId==null ? 1 : slaveId;
        this.isIgnoreSlaveId = (slaveId == null);
        this.functionControllers = functionControllers;
        try {
            in = socket.getInputStream();
            out = new SyncOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    public ConcurrentMap<Integer, Exchange> getConcurrentMap() {
        return concurrentMap;
    }

    //todo 切换functionController
    public void changeFunctionController(int functionCode, FunctionController function) {
        functionControllers[functionCode] = function;
    }

    public void setFinallyInterceptor(FinallyInterceptor finallyInterceptor) {
        this.finallyInterceptor = finallyInterceptor;
    }

    protected void serveLastHook(byte[] header, byte[] ADU) {

    }


    /**
     * @param ADU   功能码为0角标
     * @param latch
     * @return
     */
    public int send(byte[] ADU, int count, CountDownLatch latch, ReadCallback callback) {
        byte[] cmd = new byte[7 + ADU.length];
        int id = ThreadLocalRandom.current().nextInt(10000) + 55535;
        cmd[0] = BitUtil.getInt8To16(id);
        cmd[1] = BitUtil.getInt0To8(id);
        cmd[2] = 0;
        cmd[3] = 0;
        cmd[4] = BitUtil.getInt8To16(ADU.length + 1);
        cmd[5] = BitUtil.getInt0To8(ADU.length + 1);
        cmd[6] = BitUtil.getInt0To8(slaveId);
        System.arraycopy(ADU, 0, cmd, 7, ADU.length);
        writeAndFlush(cmd,count,latch,callback);
        return id;
    }

    public int send(byte[] ADU, int count) {
        return send(ADU,count,null,null);
    }

    public int send(byte[] ADU, int count, CountDownLatch latch) {
        return send(ADU,count,latch,null);
    }

    public int send(byte[] ADU, int count, ReadCallback callback) {
        return send(ADU,count,null,callback);
    }

    //todo 发送的功能
    public void writeAndFlush(byte[] cmd, int count, CountDownLatch latch, ReadCallback callback)  {
        try {
            concurrentMap.put(
                    ConvertTo.getInteger(cmd[0], cmd[1]),
                    new Exchange(
                            cmd[7] & 0xFF,
                            ConvertTo.getInteger(cmd[8], cmd[9]),
                            count, latch,callback));
            out.writeAndFlush(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //被动  in -> out
    @Override
    public void run() {
        byte[] header = new byte[7];
        byte[] ADU;
        try {
            while (!socket.isClosed()) {
                int readLen = InputStreamUtil.readNBytes(in, header, 0, 7);
                if (readLen != 7) {
                    close();
                    break;
                }
                int transActionId = ((header[0] & 0xFF) << 8) | (header[1] & 0xFF);
                GlobalLogger.logger.debug("transActionId " + transActionId);
                int protocal = ((header[2] & 0xFF) << 8) | header[3];
                GlobalLogger.logger.debug("protocal " + protocal);
                if (protocal != 0) {
                    GlobalLogger.logger.error("protocal != 0 close accept");
                    close();
                    break;
                }
                int frameLen = ((header[4] & 0xFF) << 8) | (header[5] & 0xFF);
                GlobalLogger.logger.debug("frameLen " + frameLen);
                ADU = new byte[frameLen - 1];
                int len = InputStreamUtil.readNBytes(in,ADU, 0, frameLen - 1);
                if (len != frameLen - 1) {
                    GlobalLogger.logger.error("len != frameLen close accept ");
                    close();
                    break;
                }
                int slaveId0 = header[6] & 0xFF;
                if (!isIgnoreSlaveId && slaveId != slaveId0) {
                    GlobalLogger.logger.debug("slaveId " + slaveId0 + " not allow");
                    close();
                }
                GlobalLogger.logger.debug("slaveId " + slaveId);
                int functionCode = ADU[0] & 0xFF;
                GlobalLogger.logger.debug("functionCode " + functionCode);
                try {
                    if (functionCode < functionControllers.length && functionControllers[functionCode] != null) {
                        byte[] outValue = functionControllers[functionCode].serve(header, ADU, this);
                        if (outValue != null) {
                            //写单元标识符
                            outValue[6] = BitUtil.getInt0To8(slaveId);
                            //outValue[4] outValue[5] 写长度
                            int outLen = outValue.length - 6;
                            outValue[5] = BitUtil.getInt0To8(outLen);
                            outValue[4] = BitUtil.getInt8To16(outLen);
                            System.arraycopy(header, 0, outValue, 0, 4);
                            out.writeAndFlush(outValue);
                        }
                    } else if (functionCode >= 80 && functionCode <= (80+16)) {
                        //只要客户端才会进入这里
                        GlobalLogger.logger.error(ByteArrays.toString(ADU));
                        FunctionController controller = functionControllers[functionCode - 80];
                        if (controller == null){
                            //throw new IllegalFunctionException();
                        }else {
                            controller.serve(header, ADU, this);
                        }
                    } else {
                        throw new IllegalFunctionException();
                    }
                }catch (ModbusException e){
                    //id,pi,len 3,sid,80 + function,01
                    byte[] outs = new byte[9];
                    outs[4] = (byte)0x00;
                    outs[5] = (byte)0x03;
                    outs[6] = BitUtil.getInt0To8(slaveId);
                    outs[7] = (byte)(80+functionCode);
                    outs[8] = (byte)e.getCode();
                    System.arraycopy(header, 0, outs, 0, 4);
                    out.writeAndFlush(outs);
                    GlobalLogger.logger.debug("error :" + e);
                }
                serveLastHook(header, ADU);
                if (finallyInterceptor != null) {
                    finallyInterceptor.serve(header, ADU,this);
                }
                ADU = null;
            }
        } catch (Exception e) {
            close();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close() {
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
