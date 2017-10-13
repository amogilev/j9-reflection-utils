package com.gilecode.reflection;

import com.gilecode.reflection.impl.PreJava9ReflectionAccessor;
import com.gilecode.reflection.impl.UnsafeReflectionAccessor;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ReflectionAccessUtilsTest {

    @Test
    public void testGetReflectionAccessor() throws Exception {
        assertNotNull(ReflectionAccessUtils.getReflectionAccessor());
    }

    @Test
    public void testCreateReflectionAccessorPreJ9() throws Exception {
        System.setProperty("java.version", "1.8.0_100");
        assertTrue(ReflectionAccessUtils.createReflectionAccessor() instanceof PreJava9ReflectionAccessor);
    }

    @Test
    public void testGetReflectionAccessorJ9() throws Exception {
        System.setProperty("java.version", "9");
        assertTrue(ReflectionAccessUtils.createReflectionAccessor() instanceof UnsafeReflectionAccessor);
    }

    @Test
    public void testGetMajorJavaVersion() throws Exception {
        assertTrue(ReflectionAccessUtils.getMajorJavaVersion() > 0);

        System.setProperty("java.version", "9");
        assertEquals(9, ReflectionAccessUtils.getMajorJavaVersion());

        System.setProperty("java.version", "1.8.0_100");
        assertEquals(8, ReflectionAccessUtils.getMajorJavaVersion());
    }

    @Test
    public void testSuppressWarning() throws Exception {
        try {
            // set our local stream for use by IllegalAccessLogger to catch possible warnings
            ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field streamField = loggerClass.getDeclaredField("warningStream");
            Field instanceField = loggerClass.getDeclaredField("logger");
            ReflectionAccessUtils.getReflectionAccessor().makeAccessible(streamField);
            ReflectionAccessUtils.getReflectionAccessor().makeAccessible(instanceField);
            Object loggerInstance = instanceField.get(null);
            streamField.set(loggerInstance, new PrintStream(baos));

            // if warnings are not suppressed, setAccessible() below is actually warned
            ReflectionAccessUtils.suppressIllegalReflectiveAccessWarnings();
            ArrayList.class.getDeclaredField("size").setAccessible(true);

            // no warnings are expected
            assertEquals(0, baos.size());
        } catch (ClassNotFoundException e) {
            // skip test if not in Java 9 or later
        }
    }
}