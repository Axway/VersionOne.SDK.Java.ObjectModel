package com.versionone.om.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.TransformIterable;
import com.versionone.om.TransformIterable.ITransformerGeneric;

public class TransformerTester {

    @Test
    public void testIterator() {
        String[] stringsData1 = new String[]{"test1", "test3", "test5"};
        String[] stringsData2 = new String[]{"test2", "test4", "test6"};
        String[] stringsData3 = new String[]{"test2", "test2", "test6"};
        final int length = stringsData1.length;

        List<TestData> data = new ArrayList<TestData>(length);
        for (int i = 0; i < stringsData1.length; i++) {
            data.add(new TestData(stringsData1[i], stringsData2[i]));
        }

        ITransformerGeneric<TestData, String> trans = new ITransformerGeneric<TestData, String>() {
            public String transform(TestData e, Class<String> dClass) {
                return e.getField2();
            }
        };

        TransformIterable<TestData, String> transformed =
                new TransformIterable<TestData, String>(data, trans, String.class);

        List<Object> resultList = new ArrayList<Object>(length);
        for (String aTransformed : transformed) {
            resultList.add(aTransformed);
        }

        Assert.assertTrue(isListEqualIgnoringOrder(stringsData2, resultList));
        Assert.assertFalse(isListEqualIgnoringOrder(stringsData3, resultList));
    }

    @Test
    public void testTransformer() {
        final String test1 = "test 1";
        TestData data = new TestData(test1, "test 2");

        ITransformerGeneric<TestData, String> trans = new ITransformerGeneric<TestData, String>() {
            public String transform(TestData e, Class<String> dClass) {
                return e.getField1();
            }
        };

        Assert.assertTrue(trans.transform(data, String.class).equals(test1));
    }

    class TestData {
        private String field1;
        private String field2;

        public TestData(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }
    }

    /**
     * Testing the list ignoring order
     */
    <T> boolean isListEqualIgnoringOrder(T[] expects, Collection<T> actuals) {
        if (actuals.size() != expects.length) {
            throw new AssertionFailedError("Lengths are different: expect=" +
                    expects.length + " but actual size is " + actuals.size());
        }

        for (T actual : actuals) {
            boolean status = false;
            for (int i = 0; i < expects.length; i++) {
                final T expect = expects[i];
                if (expect != null && actual.equals(expect)) {
                    status = true;
                    expects[i] = null;
                    break;
                }
            }

            if (!status) {
                return false;
            }
        }

        for (Object expect : expects) {
            if (expect != null) {
                return false;
            }
        }

        return true;
    }
}
