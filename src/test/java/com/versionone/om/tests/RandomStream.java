/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

class RandomStream extends InputStream {

    private static final char MIN = 'a';
    private static final char MAX = 'z';

    final Random random = new Random();
    final int size;
    int pos = 0;
    int mark = 0;

    /**
     * Create RandomStream of specified size.
     *
     * @param size of stream in bytes.
     */
    RandomStream(int size) {
        this.size = size;
    }

    public int read() throws IOException {
        return (pos++ < size) ? getRandomInt(MIN, MAX) : -1;
    }

    private int getRandomInt(int from, int to) {
        assert to > from;
        return random.nextInt(to - from + 1) + from;
    }

    public long skip(long n) throws IOException {
        long res = Math.min(n, size - pos);
        pos += res;
        return res;
    }

    public boolean markSupported() {
        return true;
    }

    public void mark(int readAheadLimit) {
        mark = pos;
    }

    public synchronized void reset() {
        pos = mark;
    }
}