package com.zhipuchina.utils;

public class Tuple<A,B,C> extends Pair<A,B>{

    private C third;

    public Tuple(A first, B second,C third) {
        super(first, second);
        this.third = third;
    }

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
