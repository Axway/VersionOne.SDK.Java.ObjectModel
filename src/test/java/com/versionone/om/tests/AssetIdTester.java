/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Test;

import com.versionone.om.AssetID;

public class AssetIdTester {

    @Test
    public void testConstructor() {
        AssetID id = new AssetID("GoodToken:1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail1() {
        AssetID id = new AssetID("WrongToken1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail2() {
        AssetID id = new AssetID("1234:WrongToken");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail3() {
        AssetID id = new AssetID("Wrong:Token:1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail4() {
        AssetID id = new AssetID("WrongToken.1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail5() {
        AssetID id = new AssetID("WrongToken:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail6() {
        AssetID id = new AssetID(":1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail8() {
        AssetID id = new AssetID("Wrong:Token");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail9() {
        AssetID id = new AssetID("Wrong$:123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail10() {
        AssetID id = new AssetID("Wrong:123$");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail11() {
        AssetID id = new AssetID("Wr-ong:123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail12() {
        AssetID id = new AssetID("Wr-ong:12-3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail13() {
        AssetID id = new AssetID("$Wr-ong:12-3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail14() {
        AssetID id = new AssetID("Wr-ong:$123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFail15() {
        AssetID id = new AssetID("Wrong\\:123");
    }
}
