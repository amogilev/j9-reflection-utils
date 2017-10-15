package com.gilecode.reflection.impl;

import com.gilecode.reflection.ReflectionAccessor;

import java.lang.reflect.AccessibleObject;

/**
 * A basic implementation of {@link ReflectionAccessor} which is suitable for Java 8 and below.
 * <p>
 * This implementation just calls {@link AccessibleObject#setAccessible(boolean) setAccessible(true)}, which worked
 * fine before Java 9.
 *
 * @author Andrey Mogilev
 */
public class PreJava9ReflectionAccessor implements ReflectionAccessor {

    /**
     * {@inheritDoc}
     */
    public void makeAccessible(AccessibleObject ao) {
        ao.setAccessible(true);
    }
}
