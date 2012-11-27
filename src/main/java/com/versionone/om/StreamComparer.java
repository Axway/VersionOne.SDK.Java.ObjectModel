/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.io.IOException;
import java.io.InputStream;

/**
 * Comparator for streams.
 */
public class StreamComparer {

    /**
     * @param expected first stream.
     * @param actual second stream.
     * @return true if data in streams is equals, false - otherwise.
     * @throws IOException if occur any error with reading from any streams.
     */
    public static boolean compareStream(InputStream expected, InputStream actual)
            throws IOException {
        int i, j;

        expected.reset();
        actual.reset();

        do {
            i = expected.read();
            j = actual.read();
            if (i != j) {
                return false;
            }
        } while ((i != -1) && (j != -1));

        return true;
    }

}
