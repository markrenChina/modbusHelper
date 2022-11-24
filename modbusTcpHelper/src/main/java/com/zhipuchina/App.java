package com.zhipuchina;

import com.zhipuchina.event.ChangeEventHandler;
import com.zhipuchina.event.EventManager;
import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.model.Buffer;
import com.zhipuchina.model.MemoryTypes;
import com.zhipuchina.server.ModbusTcpServer;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */
public class App {

    public static void testServer() throws UnknownHostException {
        Buffer.malloc(MemoryTypes.InputCoil, 0, 1000);
        Buffer.malloc(MemoryTypes.OutputCoil, 0, 1000);
        Buffer.malloc(MemoryTypes.HoldingRegister, 0, 50);
        Buffer.malloc(MemoryTypes.InputRegister, 0, 200);
        System.out.println(MemoryTypes.code2MemoryTypes(4));
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(0, 1, () -> System.err.println("0 改成了 1")));
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(1, 2, () -> System.err.println("1 改成了 2")));
        EventManager.register(MemoryTypes.OutputCoil, 0, new ChangeEventHandler(false, true, () -> System.out.println("开")));
        EventManager.register(MemoryTypes.OutputCoil, 0, new ChangeEventHandler(true, false, () -> System.out.println("关")));
        //Buffer.setValue(OutputCoil.class,0,true);
        ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888);
        server.start();
        //System.out.println("Tcp Server 已经启动 pid = " + getProcessID());

        //        Buffer.setValue(MemoryTypes.OutputCoil, 8, true);
//        System.out.println(Buffer.getValue(MemoryTypes.OutputCoil, 8)[0]);
//        EventManager.register(MemoryTypes.HoldingRegister, 1, new BeforeEventHandler() {
//            @Override
//            public boolean isNeedAsync() {
//                return true;
//            }
//
//            @Override
//            public void process(Object oldValue, Object newValue) {
//
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    Buffer.setValue(MemoryTypes.HoldingRegister, 2, ConvertTo.primitive(100));
//                } catch (ModbusException e) {
//                    //throw new RuntimeException(e);
//                }
//            }
//        });
    }


    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMXBean.getName());
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);

    }


    public static void main(String[] args) throws UnknownHostException, ModbusException, InterruptedException {
        testServer();
    }
}
