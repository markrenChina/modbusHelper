package com.zhipuchina.model;

import com.zhipuchina.exception.ModbusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Memory{
    protected Slice head;

    public Memory(Slice head) {
        this.head = head;
    }

    public abstract int getValue(int pos);
    public abstract int[] getValue(int start,int count) throws ModbusException;

    public void setValue(int pos,int val){
        Slice slice = findSlice(pos);
        if (slice != null) {
            setValue(slice, pos, val);
        }else {
            throw new RuntimeException("illegal address");
        }

    }
    protected abstract void setValue(Slice slice,int pos,int val);

    public  void setValue(int start,int count,int[] val){
        if (val.length == count) {
            Slice slice = findSlice(start, count);
            if (slice != null) {
                for (int i = 0; i < count; i++) {
                    setValue(slice,start+i,val[i]);
                }
                return;
            }
            throw new RuntimeException("illegal address");
        }
        throw new RuntimeException("illegal data");
    }


    public abstract void malloc(int start,int size);



    public  List<Integer> getValueAsInt(int start, int count) throws ModbusException{
//        return Arrays.stream(getValue(start,count)).boxed().collect(Collectors.toList());
        List<Integer> res = new ArrayList<>(count);
        for (int i : getValue(start, count)) {
            res.add(i);
        }
        return res;
    }

    /**
     *
     * @param pos
     * @return if not find return null
     */
    public Slice findSlice(int pos){
        Iterator<Slice> iterator = head.iterator();
        Slice slice = null;

        while (iterator.hasNext()){
            slice = iterator.next();
            if (slice.isInThisSlice(pos)){
                return slice;
            }
        }
        return null;
    }

    public Slice findSlice(int pos,int count){
        Iterator<Slice> iterator = head.iterator();
        Slice slice = null;
        while (iterator.hasNext()){
            slice = iterator.next();
            if (slice.isInThisSlice(pos,count)){
                break;
            }
        }
        return slice;
    }

    public Slice findPreSlice(Slice slice){
        Iterator<Slice> iterator = head.iterator();
        Slice pre = null;
        Slice tmp = null;
        while (iterator.hasNext()){
            pre = tmp;
            tmp = iterator.next();
            if (tmp == slice){
                return pre;
            }
        }
        return null;
    }

    public Slice findPreSlice(int pos){
        Iterator<Slice> iterator = head.iterator();
        Slice pre = null;
        Slice tmp = null;
        if (head.end >= pos) {
            return null;
        }
        while (iterator.hasNext()){
            pre = tmp;
            tmp = iterator.next();
            if (tmp.start > pos && (pre == null || pre.end < pos)){
                return pre;
            }
        }
        return tmp;
    }

    public Slice findAfterSlice(int pos){
        Iterator<Slice> iterator = head.iterator();
        Slice pre = null;
        Slice tmp = null;
        while (iterator.hasNext()){
            pre = tmp;
            tmp = iterator.next();
            if (tmp.start > pos && (pre == null || pre.end < pos)){
                return tmp;
            }
        }
        return null;
    }

    public void insert(Slice slice){
        Iterator<Slice> iterator = head.iterator();
        Slice preSlice = null;
        Slice nowSlice = null;
        while (iterator.hasNext()){
            preSlice = nowSlice;
            nowSlice = iterator.next();
            if (nowSlice.start > slice.end){
                if (preSlice == null){
                    head = slice;
                }else {
                    preSlice.next = slice;
                }
                slice.next = nowSlice;
                return;
            }
        }
        assert nowSlice != null;
        nowSlice.next = slice;
    }
}
