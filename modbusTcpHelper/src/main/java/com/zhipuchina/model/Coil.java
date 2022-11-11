package com.zhipuchina.model;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exception.ModbusExceptionFactory;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.ConvertTo;

import java.util.ArrayList;
import java.util.List;

public class Coil extends Memory {

    public Coil(Slice head) {
        super(head);
    }

    @Override
    public byte[] getValue(int pos) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            int index = (pos - slice.start) / 8;
            int count = (pos - slice.start) % 8;
            byte data = slice.data[index];
            if (((data >> (7 - count)) & 0x01) == 0x01){
                return new byte[]{ 0x01 };
            }else {
                return new byte[]{ 0x00 };
            }
        } else {
            return null;
        }
    }

    public boolean getValue(Slice slice,int pos){
        if (slice != null && slice.isInThisSlice(pos)){
            int index = (pos - slice.start) / 8;
            int count = (pos - slice.start) % 8;
            byte data = slice.data[index];
            return ((data >> (7 - count)) & 0x01) == 0x01;
        }else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    //todo 优化为System.arrayCopy
    @Override
    public byte[] getValue(int start, int count) throws ModbusException {
        Slice slice = findSlice(start);
        if (slice == null || (slice.end + 1) < (start + count)){
            throw ModbusExceptionFactory.create(2);
        }
        byte[] res = new byte[count/8+1];
        for (int i = 0; i < count; i++) {
            boolean value = getValue(slice,start + i);
            setValue(res,i,value);
        }
        return res;
    }

    @Override
    public List<Integer> getValueAsInt(int start, int count) throws ModbusException {
        Slice slice = findSlice(start);
        if (slice == null || (slice.end + 1) < (start + count)){
            throw ModbusExceptionFactory.create(2);
        }
        List<Integer> res = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            boolean value = getValue(slice,start + i);
            res.add(i, value ? 1 : 0);
        }
        return res;
    }

    //单个值设置的方法
    @Override
    public void setValue(int pos,byte[] val) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            int index = (pos - slice.start) / 8;
            int count = (pos - slice.start) % 8;
            if (count == 0){
                slice.data[index] = BitUtil.setBit(slice.data[index],1,val[0]);
            }else {
                slice.data[index] = BitUtil.setBit(slice.data[index],count+1,val[0]);
            }
            return ;
        }
        throw new RuntimeException("illegal address");
    }

    public void setValue(byte[] dst,int offset,boolean val){
        int index = offset / 8 ;
        int bitCount = offset % 8;
        int tmp = val ? 1 : 0;
        if (bitCount == 0){
            dst[index] = BitUtil.setBit(dst[index],1,tmp);
        }else {
            dst[index] = BitUtil.setBit(dst[index],bitCount+1,tmp);
        }
    }

    /**
     * val 二进制从左到右排列，与TCP层传输byte从右到左不同
     * 可以使用 工具翻转单个byte
     * @see BitUtil#reversal(byte)
     * @param start 起始地址
     * @param count 数量
     * @param val 值
     */
    @Override
    public void setValue(int start, int count, byte[] val) {
        Slice slice = findSlice(start);
        if (slice == null || slice.end < (start + count)){
            return;
        }
        for (int i = 0; i < count; i++) {
            int pos = start + i;
            int index = i / 8;
            int bitCount = i % 8;
            byte data = slice.data[index];
            if (((data >> (7 - bitCount)) & 0x01) == 0x01){
                setValue(pos ,new byte[]{ 0x01 } );
            }else {
                setValue(pos ,new byte[]{ 0x00 } );
            }
        }
    }

    @Override
    public void malloc(int start, int size) {
        int end = start + size -1;  //包含的
        int byteSize = (size / 8) + 1;
        if (head == null) {
            head = new Slice(start, end, new byte[byteSize], null);
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
            byteSize = (totalSize/8) + 1;
            byte[] data = new byte[byteSize];
            for (int i = start; i <= end ; i++) {
                Slice tmp = findSlice(i);
                if (tmp != null){
//                    System.arraycopy(tmp.data,0,data,i-start,tmp.count() * 2);
//                    byte[] value = getValue(tmp.start, tmp.count());
                    for (int j = i; j <= tmp.end; j++) {
                        setValue(data, j - start, getValue(tmp,j));
                    }
                    i = tmp.end;
                }else {
                    tmp = findAfterSlice(i);
                    if (tmp == null){
                        break;
                    }else if(tmp.start <= end) {
//                        System.arraycopy(tmp.data,0,data,tmp.start - start -1,tmp.count() * 2);
                        for (int j = tmp.start; j <= tmp.end; j++) {
                            setValue(data, j - start, getValue(tmp,j));
                        }
                        i = tmp.end;
                    }
                }
            }
            Slice pre = findPreSlice(start);
            Slice aft = findAfterSlice(end);
            Slice slice = new Slice(start, end, data,aft);
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
