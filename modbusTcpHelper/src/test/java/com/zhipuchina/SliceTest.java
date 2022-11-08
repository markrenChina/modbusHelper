package com.zhipuchina;

import com.zhipuchina.exception.ModbusException;
import com.zhipuchina.model.Coil;
import com.zhipuchina.model.Register;
import com.zhipuchina.model.Slice;
import junit.framework.TestCase;

import java.util.Iterator;

public class SliceTest extends TestCase {

    public void testMallocRegister(){
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

    public void testMallocCoil() throws ModbusException {
        Coil coil = new Coil(null);
        coil.malloc(0,10);
        Slice head = coil.findSlice(0);
        Iterator<Slice> iterator = head.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(10, head.count());
        coil.setValue(8,new byte[]{0x01});

        assertEquals(coil.getValue(8,1)[0],0x01);

        coil.malloc(16,9);
        iterator.next();
        assertTrue(iterator.hasNext());
        Slice next = iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(9, next.count());

        coil.malloc(5,12);
        head = coil.findSlice(17);
        assertEquals(head.count(),25);
        iterator = head.iterator();
        iterator.next();
        assertFalse(iterator.hasNext());
        assertEquals(coil.getValue(8)[0],0x01);

    }
}
