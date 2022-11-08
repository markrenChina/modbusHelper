package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;

public class FourServerFunctionReadHandler extends ThreeServerFunctionReadHandler {
    public FourServerFunctionReadHandler() {
        super();
        this.type = MemoryTypes.InputRegister;
    }
}
