package com.zhipuchina.model;

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
            data = (byte) ((data >> (7 - count)) & 0x01);
            byte[] res = new byte[1];
            res[0] = data;
            return res;
        } else {
            return null;
        }
    }

    @Override
    public byte[] getValue(int start, int count) {
        return new byte[0];
    }

    @Override
    public void setValue(int pos,byte[] val) {
        Slice slice = findSlice(pos);
        if (slice != null) {
            int index = (pos - slice.start) / 8;
            int count = (pos - slice.start) % 8;
            int data = (val[0] & 0x01) << (7 - count);
            slice.data[index] |= data;
            return ;
        }
        throw new RuntimeException("illegal address");
    }

    @Override
    public void setValue(int start, int count, byte[] val) {

    }

    @Override
    public void malloc(int start, int size) {

    }
}
