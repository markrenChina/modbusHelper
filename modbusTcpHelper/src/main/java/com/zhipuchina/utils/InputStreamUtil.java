package com.zhipuchina.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class InputStreamUtil {

    static public int readNBytes(InputStream inputStream, byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);

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
