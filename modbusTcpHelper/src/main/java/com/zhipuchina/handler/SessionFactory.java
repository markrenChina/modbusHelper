package com.zhipuchina.handler;

import java.net.Socket;

public abstract class SessionFactory {
    Integer slaveId;

    /**
     *
     * @param slaveId null 忽略slaveId
     */
    public SessionFactory(Integer slaveId) {
        this.slaveId = slaveId;
    }

    public abstract ModbusTcpBasicSession accept(Socket socket);
}
