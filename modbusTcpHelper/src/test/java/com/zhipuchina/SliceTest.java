package com.zhipuchina;

import com.zhipuchina.model.Register;
import com.zhipuchina.model.Slice;
import junit.framework.TestCase;

import java.util.Iterator;

public class SliceTest extends TestCase {

    public void testMalloc(){
        Register register = new Register(null);
        register.malloc(100,100);
        Slice head = register.findSlice(100);

        Iterator<Slice> iterator = head.iterator();
        assertTrue(iterator.hasNext());

        Slice next = iterator.next();
        assertEquals(next, head);

        Slice nullNext = iterator.next();
        assertNull(nullNext);

        //第二次分配
        register.malloc(500,100);
        iterator = head.iterator();
        next = iterator.next();
        assertNotNull(next);
        assertEquals(next, head);
        Slice notNullNext = iterator.next();
        assertNotNull(notNullNext);


        register.malloc(150,400);
//        register.malloc(150,500);
        iterator = head.iterator();
        next = iterator.next(); //head
        assertNotNull(next);
        nullNext = iterator.next();
        assertNull(nullNext);
    }
}
