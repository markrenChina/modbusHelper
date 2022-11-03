package com.zhipuchina;

import com.zhipuchina.client.ModbusTcpClient;
import com.zhipuchina.event.ChangeEventHandler;
import com.zhipuchina.event.EventManager;
import com.zhipuchina.handler.DefaultServerSessionFactoryImp;
import com.zhipuchina.handler.ModbusSyncTimer;
import com.zhipuchina.handler.ModbusTcpBasicSession;
import com.zhipuchina.handler.SessionFactory;
import com.zhipuchina.model.*;
import com.zhipuchina.server.ModbusTcpServer;
import com.zhipuchina.utils.Buffer;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class App 
{

    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMXBean.getName());
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);

    }

    public static void testServer() throws UnknownHostException {
        Buffer.malloc(MemoryTypes.InputCoil,0,1000);
        Buffer.malloc(MemoryTypes.OutputCoil,0,1000);
        Buffer.malloc(MemoryTypes.HoldingRegister,0,50);
        Buffer.malloc(MemoryTypes.InputRegister,0,200);
        System.out.println(MemoryTypes.code2MemoryTypes(4));
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(0,1,()->System.err.println("0 改成了 1")));
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(1,2,()->System.err.println("1 改成了 2")));
        EventManager.register(MemoryTypes.OutputCoil,0,new ChangeEventHandler(false,true,() -> System.out.println("开")));
        EventManager.register(MemoryTypes.OutputCoil,0,new ChangeEventHandler(true,false,() -> System.out.println("关")));
        //Buffer.setValue(OutputCoil.class,0,true);
        ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888);
        server.start();
        System.out.println("Tcp Server 已经启动 pid = " + getProcessID());
    }

    public static void testClient() throws UnknownHostException, InterruptedException {
        Buffer.malloc(MemoryTypes.HoldingRegister,0,10);
        ModbusSyncTimer task = new ModbusSyncTimer(3,500);
        ModbusTcpClient client =
                new ModbusTcpClient(new InetSocketAddress("127.0.0.1", 8888), 1, task, null
                );
//        ModbusTcpClient client = new ModbusTcpClient(new InetSocketAddress("127.0.0.1",8888),1,null,null);
//        for (int i = 0; i < 100; i++) {
//            TimeUnit.SECONDS.sleep(1);
//            client.writeV(MemoryTypes.OutputRegister,1,null);
//        }
        while (true){
            TimeUnit.SECONDS.sleep(1);
            byte[] value = Buffer.getValue(MemoryTypes.HoldingRegister, 0, 10);
            System.out.println(Arrays.toString(value));
        }

        //boolean b = client.writeV(MemoryTypes.HoldingRegister, 1, Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        //System.err.println(b);
//        client.read()
    }


    public static void main( String[] args ) throws UnknownHostException, InterruptedException {
       testServer();
       Buffer.setValue(MemoryTypes.OutputCoil,8,true);
       System.out.println(Buffer.getValue(MemoryTypes.OutputCoil,8)[0]);
       //testClient();
    }
}
