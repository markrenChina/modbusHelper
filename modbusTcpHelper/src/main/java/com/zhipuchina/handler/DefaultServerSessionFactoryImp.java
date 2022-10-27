package com.zhipuchina.handler;

import com.zhipuchina.function.FunctionController;
import com.zhipuchina.utils.FunctionControllerUtil;

import java.net.Socket;

public class DefaultServerSessionFactoryImp extends SessionFactory {
    private static final FunctionController[] functionControllers = new FunctionController[17];
    static {
        for (int i = 0; i < 17; i++) {
            functionControllers[i] = FunctionControllerUtil.createDefaultServerFunctionController(i);
        }
    }

    public DefaultServerSessionFactoryImp() {
        this(null);
    }

    public DefaultServerSessionFactoryImp(Integer slaveId) {
        super(slaveId);
    }

    @Override
    public ModbusTcpBasicSession accept(Socket socket) {
        return new ModbusTcpBasicSession(socket,slaveId, functionControllers);
    }
}
