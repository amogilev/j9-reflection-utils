package com.gilecode.reflection;

import java.lang.reflect.AccessibleObject;

/**
 * Provides a replacement for {@link AccessibleObject#setAccessible(boolean)}, useful when that basic operation is
 * prohibited, e.g. throws {@link java.lang.reflect.InaccessibleObjectException} in Java 9.
 *
 * @author Andrey Mogilev
 */
public interface ReflectionAccessor {

    /**
     * Does the same as {@code ao.setAccessible(true)}, but never throws
     * {@link java.lang.reflect.InaccessibleObjectException}
     */
    void makeAccessible(AccessibleObject ao);

}
