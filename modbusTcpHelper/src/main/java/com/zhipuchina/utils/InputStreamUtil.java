package com.zhipuchina.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class InputStreamUtil {

    static public int readNBytes(InputStream inputStream, byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        if (off < 0 || len < 0 || off + len >  b.length) {
            throw new IndexOutOfBoundsException(String.format(
                    "Array index out of range: off=%d, len=%d, array.length=%d",
                    off, len,  b.length));
        }
        int n = 0;
        while (n < len) {
            int count = inputStream.read(b, off + n, len - n);
            if (count < 0) {
                break;
            }
            n += count;
        }
        return n;
    }
}
