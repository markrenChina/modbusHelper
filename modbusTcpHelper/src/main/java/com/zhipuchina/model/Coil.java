package com.zhipuchina.model;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exception.ModbusExceptionFactory;

public class Coil extends Memory {

    public Coil(Slice head) {
        super(head);
    }

    @Override
    public int getValue(int pos) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            boolean data =((BooleanArray)slice.data).data[pos - slice.start];
            if (data){
                return 1;
            }else {
                return 0;
            }
        } else {
            throw new RuntimeException("IllegalAddressException");
        }
    }

    public int getValue(Slice slice,int pos){
        if (slice != null && slice.isInThisSlice(pos)){
            return (((BooleanArray)slice.data).data[pos - slice.start]) ? 1 : 0;
        }else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    //todo 优化为System.arrayCopy
    @Override
    public int[] getValue(int start, int count) throws ModbusException {
        Slice slice = findSlice(start);
        if (slice == null || (slice.end + 1) < (start + count)){
            throw ModbusExceptionFactory.create(2);
        }
        int[] res = new int[count];
        for (int i = 0; i < count; i++) {
            res[i] =  getValue(slice,start + i);
        }
        return res;
    }

    //单个值设置的方法

//    public void setValue(byte[] dst,int offset,boolean val){
//        int index = offset / 8 ;
//        int bitCount = offset % 8;
//        int tmp = val ? 1 : 0;
//        if (bitCount == 0){
//            dst[index] = BitUtil.setBit(dst[index],1,tmp);
//        }else {
//            dst[index] = BitUtil.setBit(dst[index],bitCount+1,tmp);
//        }
//    }


    @Override
    protected void setValue(Slice slice, int pos, int val) {
        ((BooleanArray)slice.data).data[pos - slice.start] =(val == 0x01);
    }

    @Override
    public void malloc(int start, int size) {
        int end = start + size -1;
        if (head == null) {
            head =  new Slice(start, end,new BooleanArray(size),null);
        }else {
            Slice startSlice = findSlice(start);
            Slice endSlice = findSlice(end);
            if (startSlice != null){
                start = startSlice.start;
            }
            if (endSlice != null){
                end = endSlice.end;
            }
            int totalSize = Buffer.count(end,start);
            boolean[] data = new boolean[totalSize];
            for (int i = start; i <= end ; i++) {
                Slice tmp = findSlice(i);
                if (tmp != null){
                    System.arraycopy(((BooleanArray)tmp.data).data,0,data,i,tmp.count());
//                    byte[] value = getValue(tmp.start, tmp.count());
                    i = tmp.end;
                }else {
                    tmp = findAfterSlice(i);
                    if (tmp == null){
                        break;
                    }else if(tmp.start <= end) {
                        System.arraycopy(((BooleanArray)tmp.data).data,0,data,tmp.start - start -1,tmp.count());
                        i = tmp.end;
                    }
                }
            }
            Slice pre = findPreSlice(start);
            Slice aft = findAfterSlice(end);
            BooleanArray boola = new BooleanArray(data);
            Slice slice = new Slice(start, end, boola,aft);
            Slice tmp = (pre == null) ? head : pre;
            while (tmp != aft){
                Slice tmp2 = tmp;
                tmp = tmp.next;
                tmp2.next = null;
            }
            if (pre == null){
                head = slice;
            }else {
                pre.next = slice;
            }
        }
    }
}
