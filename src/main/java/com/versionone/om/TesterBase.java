/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public abstract class TesterBase {
    /**
     * Asserts that two sets of objects are equal. If they are not, test will be
     * failed with the given message. If {@code expecteds} and
     * {@code actuals} are {@code null}, they are considered
     * equal.
     *
     * @param message the identifying message or {@code null} for the
     *                {@link AssertionError}
     * @param expecteds Collection of expected values.
     * @param actuals Collection actual values
     */
    protected static void assertEqualsAsSet(String message,
            Collection<?> expecteds, Collection<?> actuals) {
        String header = (message == null) ? "" : message + ": ";
        if (expecteds == null) {
            fail(header + "expected set was null");
        }
        assert expecteds != null;
        if (actuals == null) {
            fail(header + "actual set was null");
        }
        assert actuals != null;
        int actualsLength = actuals.size();
        int expectedsLength = expecteds.size();
        if (actualsLength != expectedsLength) {
            fail(header + "set lengths differed expected.length="
                    + expectedsLength + " actual.length=" + actualsLength);
        }

        for (Object expected : expecteds) {
            if (!actuals.contains(expected)) {
                fail(header + "expected object not found in actuals: "
                        + expected);
            }
        }
    }

    /**
     * Asserts that two sets of objects are equal. Converts parameters and calls
     * {@link #assertEqualsAsSet(String, java.util.Collection, java.util.Collection)}
     *
     * @param message the identifying message or {@code null} for the
     *                {@link AssertionError}
     * @param expecteds Object array with expected values.
     * @param actuals Object array with actual values
     */
    protected static void assertEqualsAsSet(String message, Object[] expecteds,
            Object[] actuals) {
        if (expecteds == actuals) {
            return;
        }
        assertEqualsAsSet(message, Arrays.asList(expecteds), Arrays
                .asList(actuals));
    }

    protected static Object newPrivateInstance(String className,
            Class<?>[] argTypes, Object... initArgs) throws Exception {
        Class clazz = Class.forName(className);
        Constructor constr = clazz.getDeclaredConstructor(argTypes);
        constr.setAccessible(true);
        return constr.newInstance(initArgs);
    }

    protected static Object invokePrivateMethod(Class clazz, Object instance,
            String methodName, Class<?>[] argTypes, Object... args)
            throws Exception {
        Method method = ((Class) clazz).getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(instance, args);
    }

    protected static Object invokePrivateMethod(Object instanceOrClass,
            String methodName, Class<?>[] argTypes, Object... args)
            throws Exception {
        Object clazz = instanceOrClass;
        if (!(clazz instanceof Class)) {
            clazz = clazz.getClass();
        }
        return invokePrivateMethod((Class) clazz, instanceOrClass, methodName,
                argTypes, args);
    }
}
