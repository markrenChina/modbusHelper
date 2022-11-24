# Modbus tcp Helper
modbus tcp java 实现

最新版本 rc2

### API可能还会发生改变

# 通用的使用方式
```java
import com.zhipuchina.model.Buffer;

class APP{
    public static void main(String[]args){
        //malloc memory
        Buffer.malloc(MemoryTypes.InputCoil, 0, 1000);
        Buffer.malloc(MemoryTypes.OutputCoil, 0, 1000);
        Buffer.malloc(MemoryTypes.HoldingRegister, 0, 50);
        Buffer.malloc(MemoryTypes.InputRegister, 0, 200);
        //Register listening
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(0, 1, () -> System.err.println("0 to 1")));
        EventManager.register(MemoryTypes.HoldingRegister, 0, new ChangeEventHandler(1, 2, () -> System.err.println("1 to 2")));
        EventManager.register(MemoryTypes.OutputCoil, 0, new ChangeEventHandler(false, true, () -> System.out.println("open")));
        EventManager.register(MemoryTypes.OutputCoil, 0, new ChangeEventHandler(true, false, () -> System.out.println("close")));
    }
}
```
2种事件抽象
```java
com.zhipuchina.event.AfterEventHandler
com.zhipuchina.event.BeforeEventHandler
```
EventManager 是事件管理类。
BeforeEventHandler 是同步的，不要使用耗时操作。(有提供异步的方式)
AfterEventHandler 支持同一个地址多种监听
ChangeEventHandler 是内建的AfterEventHandler，入参null表示任意。

替换日志工具
```java
GlobalLogger.logger = new ModBusLogAdapter() {
            @Override
            public void info(Object obj) {
                //todo use custom Logger
            }

            @Override
            public void debug(Object obj) {
            }

            @Override
            public void warn(Object obj) {
            }

            @Override
            public void error(Object obj) {
            }
        };
```

# Server
记得要开辟内存
```java
ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888);
server.start();
```

# Client
允许自带一个定时任务
```java
ModbusSyncTimer task = new ModbusSyncTimer(3, 1000);
ModbusTcpClient client = new ModbusTcpClient(new InetSocketAddress("127.0.0.1", 8888), 1, task, null);
```

