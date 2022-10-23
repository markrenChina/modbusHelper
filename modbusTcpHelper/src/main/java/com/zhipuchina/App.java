package com.zhipuchina;

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
        EventManager.register(MemoryTypes.OutputRegister, 0, new EventHandler() {
            @Override
            public void beforeProcess(Object oldValue, Object newValue) {
                System.err.println("EventHandler oldValue = "+ oldValue + " newValue" + newValue);
            }
        });
//        Buffer.setValue(OutputCoil.class,0,true);
        ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888, new DefaultSessionFactoryImp());
        server.start();
    }
}
