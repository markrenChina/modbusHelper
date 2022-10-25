package com.zhipuchina.client;

import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.handler.*;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 0~ 55534 由定时器使用
 * 55535 ~ 65535 主动发送
 */
public class ModbusTcpClient extends TcpClient{

    //private final ModbusTcpBasicSession basicSession;
    private OutputStream out;
    private InputStream in;
    private final int slaveId;
    private ModbusTcpBasicSession session;

    private final LinkedList<Pair<Integer,List<Object>>> list = new LinkedList<>();

    public ModbusTcpClient(
            SocketAddress remoteAddress,
            Integer slaveId,
            ModbusSyncTimer task,
            SocketAddress localAddress
    ) {
        super(remoteAddress, localAddress);
        this.session = new DefaultClientSessionFactoryImp().accept(socket);
        this.slaveId = Objects.requireNonNullElse(slaveId, 0);
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (task!=null){
            task.setOutputStream(out);
            ModbusExecutors.exec(task);
        }
        start();
    }

    public Object read(MemoryTypes type ,int offset){
        return readV(type, offset, 1).get(0);
    }

    //todo
    public boolean write(MemoryTypes type,int offset,Object val){
        byte[] ADU = new byte[10];
        send(ADU);
        return true;
    }

    public List<Object> readV(MemoryTypes type,int startAddress ,int count){
        CountDownLatch latch = new CountDownLatch(1);
        byte[] ADU = new byte[5];
        ADU[0] = (byte) MemoryTypes.type2functionCode(type);
        ADU[1] = BitUtil.getInt8To16(startAddress);
        ADU[2] = BitUtil.getInt0To8(startAddress);
        ADU[3] = BitUtil.getInt8To16(count);
        ADU[4] = BitUtil.getInt0To8(count);
        //send 异步
        int id = send(ADU);
        //latch.countDown();
        try {
            latch.await();
            //await 异步回调
            List<Object> res = null;
            Iterator<Pair<Integer,List<Object>>> it = list.iterator();
            while (it.hasNext()){
                Pair<Integer, List<Object>> next = it.next();
                if (next.getFirst() == id){
                    res = next.getSecond();
                    it.remove();
                    break;
                }
            }
            return res;
        } catch (InterruptedException e) {
            GlobalLogger.logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    //todo
    public boolean writeV(MemoryTypes type, int starAddress, List<Object> data){
        byte[] ADU = new byte[10];
        send(ADU);
        return true;
    }

    private int send(byte[] ADU){
        byte[] cmd = new byte[7 + ADU.length];
        int id = ThreadLocalRandom.current().nextInt(10000) + 55535;
        cmd[0] = BitUtil.getInt8To16(id);
        cmd[1] = BitUtil.getInt0To8(id);
        cmd[2] = 0;
        cmd[3] = 0;
        cmd[4] = BitUtil.getInt8To16(ADU.length +1);
        cmd[5] = BitUtil.getInt0To8(ADU.length +1);
        cmd[6] = BitUtil.getInt0To8(slaveId);
        System.arraycopy(ADU,0,cmd,6,ADU.length);
        try {
            out.write(cmd);
            out.flush();
            list.add(new Pair<>(id,new ArrayList<>()));
            return id;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //主动的 out => in
    @Override
    public void start() {
        //判断 send 出去 的id 有回复相同的id 再解析 再countDown
        session.run();
    }
}
