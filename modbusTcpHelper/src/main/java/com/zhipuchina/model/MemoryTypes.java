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

    public static int type2functionCode(MemoryTypes type) {
        int functionCode = 0;
        switch (type){
            case OutputCoil:
                functionCode = 1;
                break;
            case InputCoil:
                functionCode = 2;
                break;
            case OutputRegister:
                functionCode = 4;
                break;
            case InputRegister:
                functionCode = 3;
                break;
        }
        return functionCode;
    }
}
