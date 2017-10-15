package com.gilecode.reflection;

import com.gilecode.reflection.impl.PreJava9ReflectionAccessor;
import com.gilecode.reflection.impl.UnsafeReflectionAccessor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Provides solutions to reflective access issues appeared in Java 9:
 * <ul>
 *     <li>If annoyed by warnings like
 *     <pre>
 *     WARNING: An illegal reflective access operation has occurred
 *     WARNING: Illegal reflective access by ...
 *     </pre>- just call {@link ReflectionAccessUtils#suppressIllegalReflectiveAccessWarnings()} to suppress them.
 *     </li>
 *     <br>
 *     <li>
 *     If there is a {@link java.lang.reflect.InaccessibleObjectException} thrown in your code,
 *     obtain a {@link ReflectionAccessor} with {@link #getReflectionAccessor()} and use its
 *     {@link ReflectionAccessor#makeAccessible(AccessibleObject)}
 *     </li>
 * </ul>
 *
 * @author Andrey Mogilev
 */
public class ReflectionAccessUtils {

    /**
     * Obtains a {@link ReflectionAccessor} instance suitable for the current Java version.
     * <p>
     * You may need one a reflective operation in your code throws {@link java.lang.reflect.InaccessibleObjectException}.
     * In such a case, use {@link ReflectionAccessor#makeAccessible(AccessibleObject)} on a field, method or constructor
     * (instead of basic {@link AccessibleObject#setAccessible(boolean)}).
     */
    public static ReflectionAccessor getReflectionAccessor() {
        return ReflectionAccessorHolder.instance;
    }

    /**
     * Suppresses "illegal reflective access operation" warnings issued by Java 9 in case of inter-modules
     * reflective access operations.
     */
    public static void suppressIllegalReflectiveAccessWarnings() {
        if (getMajorJavaVersion() >= 9) {
            try {
                Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
                Field loggerField = loggerClass.getDeclaredField("logger");
                getReflectionAccessor().makeAccessible(loggerField);
                loggerField.set(null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // singleton holder
    private static class ReflectionAccessorHolder {
        static final ReflectionAccessor instance = createReflectionAccessor();
    }

    static int getMajorJavaVersion() {
        String[] parts = System.getProperty("java.version").split("[._]");
        int firstVer = Integer.parseInt(parts[0]);
        if (firstVer == 1 && parts.length > 1) {
            return Integer.parseInt(parts[1]);
        } else {
            return firstVer;
        }
    }

    static ReflectionAccessor createReflectionAccessor() {
        if (getMajorJavaVersion() < 9) {
            return new PreJava9ReflectionAccessor();
        } else {
            return new UnsafeReflectionAccessor();
        }
    }

}
