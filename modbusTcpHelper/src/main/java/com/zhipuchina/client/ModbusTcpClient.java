package com.zhipuchina.client;

import com.zhipuchina.annotation.WorkThread;
import com.zhipuchina.event.ReadCallback;
import com.zhipuchina.exec.ModbusExecutors;
import com.zhipuchina.handler.*;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.pojo.Exchange;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.ConvertTo;

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 0~ 55534 由定时器使用
 * 55535 ~ 65535 主动发送
 */
public class ModbusTcpClient extends TcpClient {

    //private final ModbusTcpBasicSession basicSession;

    private final ModbusTcpBasicSession session;
    private ModbusSyncTimer task;

    public ModbusTcpClient(
            SocketAddress remoteAddress,
            ModbusSyncTimer task,
            SocketAddress localAddress,
            SessionFactory factory
    ){
        super(remoteAddress, localAddress);
        this.session = factory.accept(socket);
        if (task != null) {
            this.task = task;
            task.setSession(session);
            ModbusExecutors.exec(task);
        }
        start();
    }

    public ModbusTcpClient(
            SocketAddress remoteAddress,
            Integer slaveId,
            ModbusSyncTimer task,
            SocketAddress localAddress
    ) {
        this(remoteAddress,task,localAddress,new DefaultClientSessionFactoryImp(slaveId));
    }

    //同步
    public Object readSync(MemoryTypes type, int offset) {
        return readVSync(type, offset, 1).get(0);
    }

    //异步
    public void readAsync(MemoryTypes type, int offset, ReadCallback callback) {
        readVAsync(type, offset, 1,callback);
    }

        //todo
    @WorkThread
    public boolean write(MemoryTypes type, int offset, Object val) {
        int functionCode = MemoryTypes.type2writeFunctionCode(type);
        byte[] value = ConvertTo.primitive(val);
        if (value.length == 1){
            value = new byte[]{ 0x00,value[0] };
        }
        byte[] ADU = new byte[]{
                    BitUtil.getInt0To8(functionCode),
                    BitUtil.getInt8To16(offset),
                    BitUtil.getInt0To8(offset),
                    value[0],
                    value[1]
            };
        CountDownLatch latch = new CountDownLatch(1);
        int id = session.send(ADU,1, latch);
        GlobalLogger.logger.debug("send finish");
        //去判断有回复再返回true 没回复 返回false
        try {
            latch.await();
            return (boolean) getRes(id).get(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object> readVSync(MemoryTypes type, int startAddress, int count) {
        CountDownLatch latch = new CountDownLatch(1);
        byte[] ADU = new byte[5];
        ADU[0] = (byte) MemoryTypes.type2readFunctionCode(type);
        ADU[1] = BitUtil.getInt8To16(startAddress);
        ADU[2] = BitUtil.getInt0To8(startAddress);
        ADU[3] = BitUtil.getInt8To16(count);
        ADU[4] = BitUtil.getInt0To8(count);
        //send 异步
        int id = session.send(ADU,count,latch);
        //latch.countDown();
        try {
            latch.await();
            //await 异步回调
            return getRes(id);
        } catch (InterruptedException e) {
            GlobalLogger.logger.error(e.getLocalizedMessage());
            return null;
        }
    }

    public void readVAsync(MemoryTypes type, int startAddress, int count, ReadCallback callback) {
        byte[] ADU = new byte[5];
        ADU[0] = (byte) MemoryTypes.type2readFunctionCode(type);
        ADU[1] = BitUtil.getInt8To16(startAddress);
        ADU[2] = BitUtil.getInt0To8(startAddress);
        ADU[3] = BitUtil.getInt8To16(count);
        ADU[4] = BitUtil.getInt0To8(count);
        session.send(ADU,count,callback);
    }

    /**
     * 读取结果会移除
     *
     * @param id
     * @return
     */
    private List<Object> getRes(int id) {
        Exchange countDownLatchListPair = session.getConcurrentMap().get(id);
        session.getConcurrentMap().remove(id);
        return countDownLatchListPair.getResult();
    }


    //todo
    @WorkThread
    public boolean writeV(MemoryTypes type, int starAddress, List<Object> data) {
        int functionCode = MemoryTypes.type2writeVFunctionCode(type);
        int bitCount = 0;
        byte[] value = null;
        if (functionCode == 15) {
            bitCount = data.size() / 8 + (data.size() % 8 == 0 ? 0 : 1);
            value = new byte[bitCount];
            //todo 线圈设置值 有错误
            for (int i = 0; i < bitCount; i++) {
//                value[i/8 + 1] = BitUtil.setBit(value[i/8 + 1],0,(ConvertTo.primitive(data.get(i))[0] & 0xFF));
                int end = 8;
                if (i == bitCount-1 && data.size()%8 !=0){
                    end = data.size()%8;
                }
                for (int j = 0; j < end; j++) {
                    value[i] = BitUtil.setBit(value[i],8-j,(ConvertTo.primitive(data.get(i*8+j))[0] & 0xFF));
                    //value[i] = (byte) (value[i] << 1);
                }
//                if (end == 0){
//                    continue;
//                }
//                value[i] = BitUtil.setBit(value[i],8,(ConvertTo.primitive(data.get(i*8+ end -1))[0] & 0xFF));

            }
        } else {
            bitCount = data.size() * 2;
            value = new byte[bitCount];
            for (int i = 0; i < data.size(); i++) {
                byte[] s = ConvertTo.primitive(data.get(i));
                value[i * 2] = s[0];
                value[i * 2 + 1] = s[1];
            }
        }
        byte[] ADU = new byte[6 + bitCount];
        ADU[0] = BitUtil.getInt0To8(functionCode);
        ADU[1] = BitUtil.getInt8To16(starAddress);
        ADU[2] = BitUtil.getInt0To8(starAddress);
        ADU[3] = BitUtil.getInt8To16(data.size());
        ADU[4] = BitUtil.getInt0To8(data.size());
        ADU[5] = BitUtil.getInt0To8(bitCount);
        //copy
        System.arraycopy(value, 0, ADU, 6, value.length);
        CountDownLatch latch = new CountDownLatch(1);
        int id = session.send(ADU, data.size(),latch);
        try {
            latch.await();
            return (boolean) getRes(id).get(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    //主动的 out => in
    @Override
    public void start() {
        //判断 send 出去 的id 有回复相同的id 再解析 再countDown
        ModbusExecutors.exec(session);
    }

    public void close(){
        if (task != null){
            task.close();
        }
        session.close();
    }
}
