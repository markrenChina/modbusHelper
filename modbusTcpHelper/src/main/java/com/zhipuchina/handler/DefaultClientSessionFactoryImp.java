package com.zhipuchina.handler;

import com.zhipuchina.function.FunctionController;
import com.zhipuchina.utils.FunctionControllerUtil;

import java.net.Socket;

public class DefaultClientSessionFactoryImp extends SessionFactory{

    private static final FunctionController[] functionControllers = new FunctionController[17];
    static {
        for (int i = 0; i < 17; i++) {
            functionControllers[i] = FunctionControllerUtil.createDefaultClientFunctionController(i);
        }
    }

    public DefaultClientSessionFactoryImp() {
        this(null);
    }

    public DefaultClientSessionFactoryImp(Integer slaveId) {
        super(slaveId);
    }

    @Override
    public ModbusTcpBasicSession accept(Socket socket) {
        return new ModbusTcpBasicSession(socket,slaveId,functionControllers);
    }
}
