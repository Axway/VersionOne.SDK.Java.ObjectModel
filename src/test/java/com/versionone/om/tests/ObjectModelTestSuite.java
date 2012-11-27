/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTestSuite.class,
        FilterTestSuite.class,
        CustomAttributeTester.class,
        ListTypeTester.class,
        OperationTester.class,
        ProjectTester.class,
        EntityCachingTester.class,
        LinkTester.class,
        NoteTester.class,
        BuildProjectTester.class,
        BuildRunTester.class,
        ChangeSetTester.class,
        IterationTester.class,
        MemberTester.class,
        AttachmentTester.class,
        TeamTester.class,
        GoalTester.class,
        IssueTester.class,
        RequestTester.class,
        ThemeTester.class,
        TestSuiteTester.class,
        RetrospectiveTester.class,
        DefectTester.class,
        StoryTester.class,
        TaskTester.class,
        ApiClientInternalsTester.class,
        /*
        TotalTester.class,
*/
        EntityCollectionTester.class,
        EpicTester.class,
        EffortTester.class,
        EntityValidatorTester.class,
        AssetsStateTester.class,
        ConversationTester.class,
        RegressionPlanTester.class,
        EnvironmentTester.class,
        RegressionSuiteTester.class,
        RegressionTestTester.class,
        RegressionSuiteTester.class
        })
public class ObjectModelTestSuite {
}
