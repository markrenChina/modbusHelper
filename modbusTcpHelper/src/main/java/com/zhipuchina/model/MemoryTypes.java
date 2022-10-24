package com.zhipuchina.model;

public enum MemoryTypes {
    InputCoil(1),
    InputRegister(3),
    OutputCoil(0),
    OutputRegister(4);

    private int code;

    MemoryTypes(int i) {
        this.code = i;
    }

    public int getCode() {
        return code;
    }

    public static MemoryTypes code2MemoryTypes(int code){
        for (MemoryTypes value : MemoryTypes.values()) {
            if (value.code == code){
                return value;
            }
        }
        throw new RuntimeException("null MemoryType");
    }
}
