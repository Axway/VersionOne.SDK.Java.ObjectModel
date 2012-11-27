/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.Duration;
import com.versionone.om.TransformIterable.ITransformer;
import com.versionone.om.tests.TestTransform.NoopTransformer;
import com.versionone.om.Project;
import com.versionone.util.V1Util;
import junit.framework.AssertionFailedError;
import junit.framework.ComparisonCompactor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListAssert {

    private final static ITransformer defaultConverter = new NoopTransformer();

    private ListAssert() {
    }

    public static void contains(Object expectedItem, Collection actualList,
                                ITransformer transformer, String message) {
        new ContainsAsserter(true, expectedItem, null, actualList, transformer,
                message).test();
    }

    public static void contains(Object expectedItem, Collection actualList,
                                String message) {
        contains(expectedItem, actualList, defaultConverter, message);
    }

    public static void contains(Object expectedItem, Collection<Project> actualList,
                                ITransformer transformer) {
        contains(expectedItem, actualList, transformer, null);
    }

    public static <T> void contains(T expectedItem, Collection<T> actualList) {
        contains(expectedItem, actualList, defaultConverter, null);
    }

    public static <S,T> void notcontains(T expectedItem, Collection<S> actualList,
                                   ITransformer<S,T> transformer, String message) {
        new ContainsAsserter<S,T>(false, expectedItem, null, actualList,
                transformer, message).test();
    }

    public static <T> void notcontains(T expectedItem, Collection<T> actualList, String message) {
        notcontains(expectedItem, actualList, defaultConverter, message);
    }

    public static void notcontains(Object expectedItem, Collection actualList,
                                   ITransformer transformer) {
        notcontains(expectedItem, actualList, transformer, null);
    }

    public static void notcontains(Object expectedItem, Collection actualList) {
        notcontains(expectedItem, actualList, defaultConverter, null);
    }

    public static void areEqualIgnoringOrder(List<Object> expectedItems, Collection actualList,
                                             ITransformer transformer, String message) {
        new AreEqualIgnoringOrderAsserter(expectedItems, null, actualList,
                transformer, message).test();
    }

    public static void areEqualIgnoringOrder(List<Object> expectedItems,
                                             Collection actualList, String message) {
        areEqualIgnoringOrder(expectedItems, actualList, defaultConverter, message);
    }

    public static void areEqualIgnoringOrder(List<Object> expectedItems,
                                             Collection actualList, ITransformer transformer) {
        areEqualIgnoringOrder(expectedItems, actualList, transformer, null);
    }

    public static void areEqualIgnoringOrder(Object[] expectedItems,
                                             Collection actualList, ITransformer transformer) {
        areEqualIgnoringOrder(Arrays.asList(expectedItems), actualList, transformer, null);
    }

    public static void areEqualIgnoringOrder(List<Object> expectedItems,
                                             Collection actualList) {
        areEqualIgnoringOrder(expectedItems, actualList, defaultConverter, null);
    }

    public static void areEqualIgnoringOrder(Collection actualList, Object... expectedItems) {
        areEqualIgnoringOrder(expectedItems, actualList, defaultConverter);
    }

    public static <S, T> void areWithinIgnoringOrder(Object epsilon,
                                                     List<T> expectedItems, Collection<S> actualList,
                                                     ITransformer<S, T> transformer, String message) {
        new AreEqualIgnoringOrderAsserter<S, T>(expectedItems, epsilon, actualList,
                transformer, message).test();
    }

    public static void areWithinIgnoringOrder(Object epsilon, List expectedItems,
                                              Collection actualList, String message) {
        areWithinIgnoringOrder(epsilon, expectedItems, actualList,
                defaultConverter, message);
    }

    public static void areWithinIgnoringOrder(Object epsilon, List expectedItems,
                                              Collection actualList, ITransformer transformer) {
        areWithinIgnoringOrder(epsilon, expectedItems, actualList, transformer, null);
    }

    public static void areWithinIgnoringOrder(Object epsilon, List expectedItems, Collection actualList) {
        areWithinIgnoringOrder(epsilon, expectedItems, actualList, defaultConverter, null);
    }

    public static void areEqual(List expectedItems, Collection actualList,
                                ITransformer transformer, String message) {
        new AreEqualAsserter(expectedItems, null, actualList, transformer, message).test();
    }

    public static void areEqual(List expectedItems, Collection actualList,
                                String message, Object... args) {
        areEqual(expectedItems, actualList, defaultConverter, message);
    }

    public static void areEqual(List expectedItems, Collection actualList,
                                ITransformer transformer) {
        areEqual(expectedItems, actualList, transformer, null);
    }

    public static void areEqual(Object[] expectedItems, Collection actualList,
                                ITransformer transformer) {
        areEqual(Arrays.asList(expectedItems), actualList, transformer, null);
    }

    public static void areEqual(List expectedItems, Collection actualList) {
        areEqual(expectedItems, actualList, defaultConverter, null);
    }

    public static void areEqual(Object[] expectedItems, Collection actualList) {
        areEqual(Arrays.asList(expectedItems), actualList, defaultConverter, null);
    }

    public static void areWithin(Object epsilon, List expectedItems,
                                 Collection actualList, ITransformer transformer, String message) {
        new AreEqualAsserter(expectedItems, epsilon, actualList, transformer, message).test();
    }

    public static void areWithin(Object epsilon, List expectedItems,
                                 Collection actualList, String message) {
        areWithin(epsilon, expectedItems, actualList, defaultConverter,
                message);
    }

    public static void areWithin(Object epsilon, List expectedItems,
                                 Collection actualList, ITransformer transformer) {
        areWithin(epsilon, expectedItems, actualList, transformer, null);
    }

    public static void areWithin(Object epsilon, List expectedItems, Collection actualList) {
        areWithin(epsilon, expectedItems, actualList, defaultConverter, null);
    }

    public static void allMatch(Object expectedItem, Collection actualList,
                                ITransformer transformer, String message) {
        new AllMatchAsserter(expectedItem, actualList, transformer, message).test();
    }

    public static void allMatch(Object expectedItem, Collection actualList,
                                String message) {
        new AllMatchAsserter(expectedItem, actualList, defaultConverter, message).test();
    }

    public static void allMatch(Object expectedItem, Collection actualList,
                                ITransformer transformer) {
        new AllMatchAsserter(expectedItem, actualList, transformer, null).test();
    }

    public static void allMatch(Object expectedItem, Collection actualList) {
        new AllMatchAsserter(expectedItem, actualList, defaultConverter, null).test();
    }

    private static abstract class ListAsserterAbstract<S, T> {
        protected Collection<S> list;
        protected ITransformer<S, T> converter;
        protected Object epsilon;
        protected String message;

        public ListAsserterAbstract(Object epsilon, Collection<S> list,
                                    ITransformer<S, T> transformer, String message) {
            this.list = list;
            converter = transformer;
            this.epsilon = epsilon;
            this.message = message;
        }

        protected boolean compareEqual(Object expected, Object actual) {

            if (epsilon != null) {

                if ((epsilon instanceof Duration)
                        && (expected instanceof DateTime)
                        && (actual instanceof DateTime)) {
                    return ((DateTime) expected).subtract((DateTime) actual)
                            .getDays() <= ((Duration) epsilon).getDays();
                } else if ((epsilon instanceof Double)
                        && (expected instanceof Double)
                        && (actual instanceof Double)) {
                    return Math.abs((Double) expected - (Double) actual) <= (Double) epsilon;
                } else if ((epsilon instanceof Float)
                        && (expected instanceof Float)
                        && (actual instanceof Float)) {
                    return Math.abs((Float) expected - (Float) actual) <= (Float) epsilon;
                }
            }
            return V1Util.equals(expected, actual);
        }

        public String getMessage() {
            return (message == null) ? "" : message + " ";
        }

        public abstract boolean test();
    }

    private static class ContainsAsserter<S, T> extends ListAsserterAbstract<S, T> {
        private boolean polarity;
        private T item;

        public ContainsAsserter(boolean polarity, T item, Object epsilon,
                                Collection<S> list, ITransformer<S, T> transformer, String message) {
            super(epsilon, list, transformer, message);
            this.polarity = polarity;
            this.item = item;
        }

        @Override
        public boolean test() {
            FailureMessage msg = new FailureMessage(message);

            for (S value : list) {
                if (compareEqual(item, converter.transform(value))) {
                    if (polarity) {
                        return true;
                    }
                    msg.displayExtra(item);
                    throw new AssertionFailedError(msg.toString());
                }
            }

            if (!polarity) {
                return true;
            }
            msg.displayMissing(item);
            throw new AssertionFailedError(msg.toString());
        }
    }

    private static class AreEqualIgnoringOrderAsserter<S, T> extends ListAsserterAbstract<S, T> {
        private List<T> items;

        public AreEqualIgnoringOrderAsserter(List<T> items, Object epsilon, Collection<S> list,
                                             ITransformer<S, T> transformer, String message) {
            super(epsilon, list, transformer, message);
            this.items = items;
        }

        @Override
        public boolean test() {
            FailureMessage msg = new FailureMessage(getMessage());
            boolean isfail = false;
            boolean[] matched = new boolean[items.size()];
            int count = 0;

            for (S value : list) {
                count++;
                Object nvalue = converter.transform(value);
                int foundat = find(items, nvalue, 0);

                while ((foundat < items.size()) && matched[foundat]) {
                    foundat = find(items, nvalue, foundat + 1);
                }

                if (foundat >= items.size()) {
                    msg.displayExtra(nvalue);
                    isfail = true;
                } else {
                    matched[foundat] = true;
                }
            }

            for (int i = 0; i < matched.length; ++i) {
                if (!matched[i]) {
                    msg.displayMissing(items.get(i));
                }
            }

            if (count != items.size()) {
                isfail = true;
                msg.displayLengthsDiff(items.size(), count);
            }

            if (isfail) {
                throw new AssertionFailedError(msg.toString());
            }
            return true;
        }

        private int find(List<T> list, Object value, int index) {
            while (index < list.size()) {
                if (compareEqual(value, list.get(index))) {
                    break;
                }
                ++index;
            }
            return index;
        }
    }

    private static class AreEqualAsserter<S, T> extends ListAsserterAbstract<S, T> {
        private List<T> items;

        public AreEqualAsserter(List<T> items, Object epsilon, Collection<S> list,
                                ITransformer<S, T> transformer, String message) {
            super(epsilon, list, transformer, message);
            this.items = items;
        }

        @Override
        public boolean test() {
            FailureMessage msg = new FailureMessage(getMessage());
            boolean isfail = false;
            int count = 0;

            for (S value : list) {
                Object nvalue = converter.transform(value);

                if (!isfail && (count < items.size())
                        && !compareEqual(items.get(count), nvalue)) {
                    msg.displayMismatch(items.get(count), nvalue, count);
                    isfail = true;
                }
                ++count;
            }
            if (count != items.size()) {
                isfail = true;
                msg.displayLengthsDiff(items.size(), count);

                if (count < items.size()) {
                    for (int i = count; i < items.size(); i++) {
                        msg.displayMissing(items.get(count));
                    }
                } else if (count > items.size()) {
                    for (int i = items.size(); i < list.size(); i++) {
                        msg.displayExtra(items.get(count));
                    }
                }
            }

            if (isfail) {
                throw new AssertionFailedError(msg.toString());
            }
            return true;
        }
    }

    private static class AllMatchAsserter<S, T> extends ListAsserterAbstract<S, T> {
        private T item;

        public AllMatchAsserter(T item, Collection<S> list,
                                ITransformer<S, T> transformer, String message) {
            super(null, list, transformer, message);
            this.item = item;
        }

        @Override
        public boolean test() {
            FailureMessage msg = new FailureMessage(getMessage());
            boolean passed = true;

            for (S value : list) {
                Object nvalue = converter.transform(value);
                boolean test = compareEqual(item, nvalue);

                if (!test) {
                    msg.displayExtra(nvalue);
                    passed = false;
                }
            }

            if (!passed) {
                throw new AssertionFailedError(msg.toString());
            }
            return true;
        }
    }

    private static class FailureMessage {
        private String message;
        private static final int MAX_CONTEXT_LENGTH = 20;

        public FailureMessage(String message) {
            this.message = message == null ? "" : message;
        }

        public void displayMismatch(Object expected, Object actual, int index) {
            final ComparisonCompactor compactor = new ComparisonCompactor(MAX_CONTEXT_LENGTH,
                    String.valueOf(expected), String.valueOf(actual));
            System.out.println();
            System.out.println(compactor.compact(message + " Mismatch at index: " + index));
        }

        public void displayMissing(Object value) {
            System.out.println();
            System.out.println(message + " Expected: " + value);
        }

        public void displayExtra(Object value) {
            System.out.println();
            System.out.println(message + " Unexpected: " + value);
        }

        public void displayLengthsDiff(int expected, int actual) {
            final ComparisonCompactor compactor = new ComparisonCompactor(MAX_CONTEXT_LENGTH,
                    String.valueOf(expected), String.valueOf(actual));
            System.out.println();
            System.out.println(compactor.compact(message + " Lengths are different:"));
        }
    }
}