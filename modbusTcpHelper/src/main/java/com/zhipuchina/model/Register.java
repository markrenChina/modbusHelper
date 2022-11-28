package com.zhipuchina.model;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exception.ModbusExceptionFactory;
import com.zhipuchina.utils.BitUtil;
import com.zhipuchina.utils.ConvertTo;

public class Register extends Memory {

    public Register(Slice head) {
        super(head);
    }

    @Override
    public int getValue(int pos) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            byte[] data = ((ByteArray)slice.data).data;
            int index = pos - slice.start;
            return ConvertTo.getInteger(data[index * 2],data[index * 2 + 1]);
        } else {
            throw new RuntimeException("IllegalAddressException");
        }
    }

    @Override
    public int[] getValue(int start, int count) throws ModbusException {
        Slice slice = findSlice(start, count);
        if (slice != null) {
            int index = start - slice.start;
            int[] res = new int[count];
            for (int i = 0; i < count; i++) {
                res[i] =  ConvertTo.getInteger(((ByteArray)slice.data).data[(i+index) * 2 ],((ByteArray)slice.data).data[(i+index) *2 + 1]);
            }
//            System.arraycopy(slice.data, index * 2, res, 0, count * 2);
            return res;
        } else {
            throw ModbusExceptionFactory.create(2);
        }
    }




    @Override
    protected void setValue(Slice slice, int pos, int val) {
        int count = pos - slice.start;
        ((ByteArray)slice.data).data[count * 2] = BitUtil.getInt8To16(val);
        ((ByteArray)slice.data).data[count * 2 + 1] = BitUtil.getInt0To8(val);
    }

    @Override
    public void malloc(int start, int size) {
        int end = start + size -1;  //包含的
        if (head == null) {

            head = new Slice(start, end, new ByteArray(size * 2), null);
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
            byte[] data = new byte[totalSize * 2];
            for (int i = start; i <= end ; i++) {
                Slice tmp = findSlice(i);
                if (tmp != null){
                    System.arraycopy(((ByteArray)tmp.data).data,0,data,i-start,tmp.count() * 2);
                    i = tmp.end;
                }else {
                    tmp = findAfterSlice(i);
                    if (tmp == null){
                        break;
                    }else if(tmp.start <= end) {
                        System.arraycopy(((ByteArray)tmp.data).data,0,data,tmp.start - start -1,tmp.count() * 2);
                        i = tmp.end;
                    }
                }
            }
            Slice pre = findPreSlice(start);
            Slice aft = findAfterSlice(end);
            ByteArray ba = new ByteArray(data);
            Slice slice = new Slice(start, end, ba,aft);
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
