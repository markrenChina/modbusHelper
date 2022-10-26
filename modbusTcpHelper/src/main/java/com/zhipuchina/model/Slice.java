package com.zhipuchina.model;

import java.util.Iterator;
import java.util.Objects;

public class Slice {
    int start;  //包含 绝对地址
    int end;    //包含 绝对地址
    byte[] data;
    Slice next;

    public Slice(int start, int end, byte[] data, Slice next) {
        this.start = start;
        this.end = end;
        this.data = data;
        this.next = next;
    }

    public int count(){
        return (end - start + 1);
    }

    public boolean isInThisSlice(int pos){
        return pos >= start && pos <= end;
    }

    public boolean isInThisSlice(int pos,int count){
        return pos >= start && pos +count <= end;
    }


    public Iterator<Slice> iterator(){
        return new Iterator<>() {
            private Slice current = new Slice(-1,-1,null,Slice.this);

            @Override
            public boolean hasNext() {
                return current.next != null;
            }

            @Override
            public Slice next() {
                current = current.next;
                return current;
            }
        };
    }


    /**
     * 不比对data数据内容
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slice slice = (Slice) o;
        return start == slice.start && end == slice.end  && Objects.equals(next, slice.next);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(start, end, next);
        result = 31 * result;
        return result;
    }

    @Override
    public String toString() {
        return "Slice{" +
                "start=" + start +
                ", end=" + end +
                ", next=" + next +
                '}';
    }
}
