package com.gilecode.reflection.impl;

import com.gilecode.reflection.ReflectionAccessor;
import sun.misc.Unsafe;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * An implementation of {@link ReflectionAccessor} based on {@link Unsafe}.
 * <p/>
 * NOTE: This implementation is designed for Java 9. Although it should work with earlier Java releases, it is better to
 * use {@link PreJava9ReflectionAccessor} for them.
 *
 * @author Andrey Mogilev
 */
public class UnsafeReflectionAccessor implements ReflectionAccessor {

    private static final Unsafe theUnsafe = getUnsafeInstance();
    private static final Field overrideField = getOverrideField();

    /**
     * {@inheritDoc}
     */
    public void makeAccessible(AccessibleObject ao) {
        if (theUnsafe != null && overrideField != null) {
            long overrideOffset = theUnsafe.objectFieldOffset(overrideField);
            theUnsafe.putBoolean(ao, overrideOffset, true);
        }
    }

    private static Unsafe getUnsafeInstance() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            return (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException|NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Field getOverrideField() {
        try {
            return AccessibleObject.class.getDeclaredField("override");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
