package com.zhipuchina.model;

import java.util.Iterator;

public abstract class Memory{
    protected Slice head;

    public Memory(Slice head) {
        this.head = head;
    }

    public abstract byte[] getValue(int pos);
    public abstract byte[] getValue(int start,int count);

    public abstract void setValue(int pos,byte[] val);
    public abstract void setValue(int start,int count,byte[] val);

    public abstract void malloc(int start,int size);

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
