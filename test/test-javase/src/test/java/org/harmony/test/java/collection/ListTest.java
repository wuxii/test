package org.harmony.test.java.collection;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Test;

public class ListTest {

    @Test
    public void testArrayList() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 11; i++) {
            list.add(null);
        }
        Field elemDate = list.getClass().getDeclaredField("elementData");
        elemDate.setAccessible(true);
        Object[] objs = (Object[]) elemDate.get(list);
        assertNotEquals(11, objs.length);
        assertEquals(11, list.size());
    }
    
}
