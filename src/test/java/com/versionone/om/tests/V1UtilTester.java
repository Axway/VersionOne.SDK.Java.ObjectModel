/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import com.versionone.om.StreamComparer;
import com.versionone.util.V1Util;

/**
 * A test case for V1Util class.
 */
public class V1UtilTester {

    @Test
    public void testEqualsObjectObject() {
        String s1 = new String("asd");
        String s2 = new String("asd");
        String s3 = "dsa";
        String s4 = "dsa";
        Integer i1 = 5;
        Integer i2 = 5;
        Integer i3 = 6;

        // "Equals" tests
        assertTrue(V1Util.equals(i1, i2));
        assertTrue(V1Util.equals(i1, 5));
        assertTrue(V1Util.equals(s1, s2)); // equals by value
        assertTrue(V1Util.equals(s3, s4)); // equals by pointer
        assertTrue(V1Util.equals(s1, "asd"));
        assertTrue(V1Util.equals(null, null));

        // "Not equals" tests
        assertFalse(V1Util.equals(i1, i3));
        assertFalse(V1Util.equals(i1, 4));
        assertFalse(V1Util.equals(null, i1));
        assertFalse(V1Util.equals(i1, null));
        assertFalse(V1Util.equals(s1, s3));
        assertFalse(V1Util.equals(s1, "asd3"));
    }

    @Test
    public void testCopyStreamReaderWriterInt() {
        final int BUFFER_SIZE = 8192;
        final int[] COPY_BUFFER_SIZES = { 1, 512, 8192, 16384 };
        char[] inputBufferArray = new char[BUFFER_SIZE];
        CharArrayReader is = new CharArrayReader(inputBufferArray);
        CharArrayWriter os = new CharArrayWriter(BUFFER_SIZE);
        Random rand = new Random((new Date()).getTime());

        // Copy data to the output stream using all defined copy buffer sizes
        try {
            for (int i = 0; i < COPY_BUFFER_SIZES.length; i++) {
                // Fill input buffer array by random data
                // Note: need for each iteration because next "os.reset()"
                // operation don't clear stream's buffer but just reset
                // stream's pointer to zero.
                for (int j = 0; j < BUFFER_SIZE; j++) {
                    inputBufferArray[j] = (char) rand.nextInt();
                }

                // Reset streams
                os.reset();
                is.reset();

                // Copy
                V1Util.copyStream(is, os, COPY_BUFFER_SIZES[i]);

                char[] resultArray = os.toCharArray();

                // Compare test array and result array sizes
                assertTrue("Result char array is " + resultArray.length
                        + " size not " + BUFFER_SIZE + ". Copy buffer size is "
                        + COPY_BUFFER_SIZES[i] + ".",
                        resultArray.length == BUFFER_SIZE);

                // Compare test array and result array data
                for (int j = 0; j < BUFFER_SIZE; j++) {
                    assertTrue(resultArray[j] == inputBufferArray[j]);
                }
            }
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testCopyStreamInputStreamOutputStreamInt() {
        final int BUFFER_SIZE = 8192;
        final int[] COPY_BUFFER_SIZES = { 1, 512, 8192, 16384 };
        byte[] inputBufferArray = new byte[BUFFER_SIZE];
        ByteArrayInputStream is = new ByteArrayInputStream(inputBufferArray);
        ByteArrayOutputStream os = new ByteArrayOutputStream(BUFFER_SIZE);
        Random rand = new Random((new Date()).getTime());

        // Copy data to the output stream using all defined copy buffer sizes
        try {
            for (int i = 0; i < COPY_BUFFER_SIZES.length; i++) {
                // Fill input buffer array by random data
                // Note: need for each iteration because next "os.reset()"
                // operation don't clear stream's buffer but just reset
                // stream's pointer to zero.
                rand.nextBytes(inputBufferArray);

                // Reset streams
                os.reset();
                is.reset();

                // Copy
                V1Util.copyStream(is, os, COPY_BUFFER_SIZES[i]);

                // Compare streams
                assertTrue(StreamComparer.compareStream(is,
                                new ByteArrayInputStream(os.toByteArray())));
            }
        } catch (IllegalArgumentException e) {
            fail();
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testConvertListToArray() {
        final int ARRAY_SIZE = 1000;
        List<Integer> testList = new ArrayList<Integer>(ARRAY_SIZE);
        Random rand = new Random((new Date()).getTime());

        // Fill list with random values
        for (int i = 0; i < ARRAY_SIZE; i++) {
            // append new random value to the end of the list
            testList.add(rand.nextInt());
        }
        // Get the result
        Integer[] resultArray = V1Util.convertListToArray(testList, Integer.class);

        // Compare result array with test list
        ListAssert.areEqual(resultArray, testList);
    }

    @Test
    public void testIsNullOrEmpty() {
        assertFalse(V1Util.isNullOrEmpty("asd"));
        assertTrue(V1Util.isNullOrEmpty(""));
        assertTrue(V1Util.isNullOrEmpty(null));
    }
}
