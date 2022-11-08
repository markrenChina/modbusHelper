package com.zhipuchina.model;

public enum MemoryTypes {
    InputCoil(1),
    InputRegister(3),
    OutputCoil(0),
    HoldingRegister(4);

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

    public static int type2readFunctionCode(MemoryTypes type) {
        int functionCode = 0;
        switch (type){
            case OutputCoil:
                functionCode = 1;
                break;
            case InputCoil:
                functionCode = 2;
                break;
            case HoldingRegister:
                functionCode = 3;
                break;
            case InputRegister:
                functionCode = 4;
                break;
        }
        return functionCode;
    }

    public static int type2writeFunctionCode(MemoryTypes type){
        switch (type){
            case OutputCoil: return 5;
            case HoldingRegister:return 6;
            default: throw new UnsupportedOperationException();
        }
    }

    public static int type2writeVFunctionCode(MemoryTypes type){
        switch (type){
            case OutputCoil: return  15;
            case HoldingRegister: return  16;
            default: throw new UnsupportedOperationException();
        }
    }

    public int getPos(int offset){
        return code * 10000 + offset;
    }
}
