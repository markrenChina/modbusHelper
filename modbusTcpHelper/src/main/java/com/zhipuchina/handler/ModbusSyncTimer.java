package com.zhipuchina.handler;

import com.zhipuchina.function.FunctionController;
import com.zhipuchina.utils.FunctionControllerUtil;

import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ModbusSyncTimer implements Runnable {

    private final int functionCode;
    private final int scanRate;

    private OutputStream outputStream;

    private int slaveId;

    private final AtomicInteger transActionId = new AtomicInteger(0);

    private final byte[] cmd = new byte[12];

    private int startAddress;
    private int count;

    public ModbusSyncTimer(int functionCode, int scanRate) {
        this(functionCode,scanRate,0,10,0);
    }

    public ModbusSyncTimer(int functionCode, int scanRate, int startAddress, int count, int slaveId) {
        this.functionCode = functionCode;
        this.scanRate = scanRate;
        this.startAddress = startAddress;
        this.count = count;
        this.slaveId = slaveId;
        //协议标识符
        cmd[2] = 0;
        cmd[3] = 0;
        //长度
        cmd[4] = 0;
        cmd[5] = 6;
        //单元标识符
        cmd[6] = (byte) slaveId;
        cmd[7] = (byte) functionCode;
        cmd[8] = (byte) ((startAddress >> 8 )&0xFF);
        cmd[9] = (byte) (startAddress &0xFF);
        cmd[10] = (byte) ((count >> 8 )&0xFF);
        cmd[11] = (byte) (count &0xFF);
    }

    public void setReadAddress(int startAddress, int count){
        this.startAddress = startAddress;
        this.count = count;
        cmd[8] = (byte) ((startAddress >> 8 )&0xFF);
        cmd[9] = (byte) (startAddress &0xFF);
        cmd[10] = (byte) ((count >> 8 )&0xFF);
        cmd[11] = (byte) (count &0xFF);
    }

    public void setSlaveId(int slaveId){
        this.slaveId = slaveId;
        cmd[6] = (byte) slaveId;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try {
            while (outputStream!= null) {
                int id = transActionId.getAndIncrement();
                if (id == 55535) {
                    id = 0;
                    transActionId.set(1);
                }
                cmd[0] = (byte) ((id >> 8 )&0xFF);
                cmd[1] = (byte) (id & 0xFF);
                outputStream.write(cmd);
                outputStream.flush();
                TimeUnit.MILLISECONDS.sleep(scanRate);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
