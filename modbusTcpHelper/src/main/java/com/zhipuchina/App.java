package com.zhipuchina;

import com.zhipuchina.event.BeforeEventHandler;
import com.zhipuchina.event.ChangeEventHandler;
import com.zhipuchina.event.EventHandler;
import com.zhipuchina.event.EventManager;
import com.zhipuchina.handler.DefaultSessionFactoryImp;
import com.zhipuchina.model.*;
import com.zhipuchina.server.ModbusTcpServer;
import com.zhipuchina.utils.Buffer;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 */
public class App 
{
    public static void main( String[] args ) throws UnknownHostException {
        Buffer.malloc(MemoryTypes.InputCoil,1000);
        Buffer.malloc(MemoryTypes.OutputCoil,1000);
        Buffer.malloc(MemoryTypes.OutputRegister,50);
        Buffer.malloc(MemoryTypes.InputRegister,200);
        System.out.println(MemoryTypes.code2MemoryTypes(4));
        EventManager.register(MemoryTypes.OutputRegister, 0, new ChangeEventHandler(0,1,()->System.err.println("0 改成了 1")));
        EventManager.register(MemoryTypes.OutputRegister, 0, new ChangeEventHandler(1,2,()->System.err.println("1 改成了 2")));
        EventManager.register(MemoryTypes.OutputCoil,0,new ChangeEventHandler(false,true,() -> System.out.println("开")));
        EventManager.register(MemoryTypes.OutputCoil,0,new ChangeEventHandler(true,false,() -> System.out.println("关")));
//        Buffer.setValue(OutputCoil.class,0,true);
        ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888, new DefaultSessionFactoryImp());
        server.start();

        System.out.println("Tcp Server 已经启动");
    }
}
