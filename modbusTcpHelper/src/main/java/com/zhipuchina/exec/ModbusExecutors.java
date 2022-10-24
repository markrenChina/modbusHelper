package com.zhipuchina.exec;

import java.util.List;
import java.util.concurrent.*;

public class ModbusExecutors {

    private static ExecutorService exec = new ThreadPoolExecutor(1, 64,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    public static void changeExecutor(ExecutorService exec){
        ModbusExecutors.exec.shutdown();
        ModbusExecutors.exec = exec;
    }

    public static void exec(Runnable job){
        ModbusExecutors.exec.execute(job);
    }
}
