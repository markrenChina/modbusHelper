package com.zhipuchina.model;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.exception.ModbusExceptionFactory;
import com.zhipuchina.utils.Buffer;

public class Register extends Memory {

    public Register(Slice head) {
        super(head);
    }

    @Override
    public byte[] getValue(int pos) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            byte[] data = slice.data;
            int index = pos - slice.start;
            byte[] res = new byte[2];
            res[0] = data[index * 2];
            res[1] = data[index * 2 + 1];
            return res;
        } else {
            return null;
        }
    }

    @Override
    public byte[] getValue(int start, int count) throws ModbusException {
        Slice slice = findSlice(start, count);
        if (slice != null) {
            int index = start - slice.start;
            byte[] res = new byte[2 * count];
            System.arraycopy(slice.data, index * 2, res, 0, count * 2);
            return res;
        } else {
            throw ModbusExceptionFactory.create(2);
        }
    }

    @Override
    public void setValue(int pos, byte[] val) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            int count = pos - slice.start;
            if (val.length == 2) {
                slice.data[count * 2] = val[0];
                slice.data[count * 2 + 1] = val[1];
                return;
            }
        }
        throw new RuntimeException("illegal address");
    }

    @Override
    public void setValue(int start, int count, byte[] val) {
        if (val.length == count * 2) {
            Slice slice = findSlice(start, count);
            if (slice != null) {
                int index = start - slice.start;
                System.arraycopy(val, 0, slice.data, index * 2, val.length);
                return;
            }
            throw new RuntimeException("illegal address");
        }
        throw new RuntimeException("illegal data");
    }

    @Override
    public void malloc(int start, int size) {
        int end = start + size -1;  //包含的
        if (head == null) {
            head = new Slice(start, end, new byte[size * 2], null);
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
                    System.arraycopy(tmp.data,0,data,i-start,tmp.count() * 2);
                    i = tmp.end;
                }else {
                    tmp = findAfterSlice(i);
                    if (tmp == null){
                        break;
                    }else if(tmp.start <= end) {
                        System.arraycopy(tmp.data,0,data,tmp.start - start -1,tmp.count() * 2);
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


//    @Override
//    public void malloc(int start, int size) {
//        int end = start + size -1;  //包含的
//        if (head == null) {
//            Slice slice = new Slice(start, end, new byte[size * 2], null);
//            head = slice;
//        } else {
//            Slice startSlice = findSlice(start);
//            Slice endSlice = findSlice(end);
//            if (startSlice == endSlice && startSlice == null) {
//                Slice slice = new Slice(start, end, new byte[size * 2], null);
//                insert(slice);
//            } else if (startSlice == null) {
//                //end 已经被分配
//                int endSize = endSlice.count();
//                int totalSize = endSlice.end - start + 1;
//                byte[] data = new byte[totalSize * 2];
//                System.arraycopy(endSlice.data, 0, data, (totalSize - endSize - 1) * 2, endSize * 2);
//                Slice slice = new Slice(start, endSlice.end, data, endSlice.next);
//                Slice pre = findPreSlice(slice);
//                if (pre == null) {
//                    head = slice;
//                } else {
//                    pre.next = slice;
//                }
//            } else if (endSlice == null){
//                //start Size 的后面所有的next 都要合并
//                int totalSize = start + size - startSlice.start ;
//                byte[] data = new byte[totalSize * 2];
//                Iterator<Slice> iterator = startSlice.iterator();
//                while (iterator.hasNext()) {
//                    Slice slice = iterator.next();
//                    if (slice.start >= start && slice.end < (start + size)) {
//                        //切片整个copy
//                        int copySize = slice.end - slice.start + 1;
//                        System.arraycopy(slice.data, 0, data, (slice.start - start - 1) * 2, copySize * 2);
//                    }else if (slice.start >=start && slice.end >= (start + size) && slice.start < (start +size)){
//                        int copySize = (start + size -1) - slice.start + 1;
//                    }
////                    int startSize = startSlice.end - startSlice.start + 1;
////                    System.arraycopy(startSlice.data,0,data,0,startSize*2);
//                }
//                Slice slice = new Slice(startSlice.start, start + size - 1, data, startSlice.next);
//                Slice pre = findPreSlice(startSlice);
//                if (pre == null) {
//                    head = slice;
//                } else {
//                    pre.next = slice;
//                }
//            } else {
//                //startSize  endSlice 不是同一个切片 合并中间所有的切片
//            }
//        }
//    }
}
