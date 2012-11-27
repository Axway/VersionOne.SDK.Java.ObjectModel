/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AssetIdTester.class,
        V1InstanceTester.class,
        EntityFilterTester.class,
        TransformerTester.class,
        V1UtilTester.class,
        WrapperManagerTester.class,
        ComparisonSearcherTester.class
        })
public class UnitTestSuite {
}
