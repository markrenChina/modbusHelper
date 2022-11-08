package com.zhipuchina.function;

import com.zhipuchina.model.MemoryTypes;

public class TwoServerFunctionReadHandler extends OneServerFunctionReadHandler {
    public TwoServerFunctionReadHandler() {
        super();
        this.type = MemoryTypes.InputCoil;
    }
}
