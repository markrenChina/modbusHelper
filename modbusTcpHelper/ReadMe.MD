# Modbus tcp Helper
modbus tcp java realize 

new version rc2

### this project in the experiment.The api is maybe to change.

# General usage
```java
import com.zhipuchina.model.Buffer;

class APP{
    public static void main(String[]args){
        //Inject the logging tool
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
There are two kinds of event abstractions:
```java
com.zhipuchina.event.AfterEventHandler
com.zhipuchina.event.BeforeEventHandler
```
The EventManager is responsible for registering and managing events.
BeforeEventHandler is synchronous, and time-consuming monitoring should not be used.(Provide asynchronous mode)
AfterEventHandler supports multiple listening to the same memory.
ChangeEventHandler is a built-in AfterEventHandler implementation, and null parameter means any

Inject the logging tool
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
Remember to malloc memory
```java
ModbusTcpServer server = new ModbusTcpServer(InetAddress.getByName("127.0.0.1"), 8888);
server.start();
```

# Client
Allow a scheduled task
```java
ModbusSyncTimer task = new ModbusSyncTimer(3, 1000);
ModbusTcpClient client = new ModbusTcpClient(new InetSocketAddress("127.0.0.1", 8888), 1, task, null);
```

