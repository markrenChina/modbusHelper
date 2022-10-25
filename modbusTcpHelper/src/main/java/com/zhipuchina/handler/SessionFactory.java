package com.zhipuchina.handler;

import com.zhipuchina.function.FunctionController;
import com.zhipuchina.utils.FunctionControllerUtil;

import java.net.Socket;

public interface SessionFactory {

    public ModbusTcpBasicSession accept(Socket socket);
}
