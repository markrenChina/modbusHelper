package com.zhipuchina;

import com.zhipuchina.utils.BitUtil;
import junit.framework.TestCase;

import java.util.Arrays;

public class Funtest extends TestCase {

    public void testStream(){
        int[] value = {8995,8995,8995,8995};
        System.out.println(Arrays.stream(value)
                .mapToObj(i -> (char) BitUtil.getInt8To16(i) + "" + (char) BitUtil.getInt0To8(i))
                .reduce("", (p, e) -> p + e));

    }
}
