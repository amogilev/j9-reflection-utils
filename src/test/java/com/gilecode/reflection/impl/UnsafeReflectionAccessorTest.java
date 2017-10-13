package com.gilecode.reflection.impl;

import com.gilecode.reflection.ReflectionAccessor;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

public class UnsafeReflectionAccessorTest {

    private ReflectionAccessor uut = new UnsafeReflectionAccessor();

    @Test
    public void testMakeAccessible() throws Exception {
        try {
            Class c = Class.forName("jdk.internal.jline.TerminalFactory");
            Method getMethod = c.getDeclaredMethod("get");
            uut.makeAccessible(getMethod);

            Object terminal = getMethod.invoke(null);
            assertNotNull(terminal);
        } catch (ClassNotFoundException e) {
            // skip test if not in Java 9 or later
        }
    }

}