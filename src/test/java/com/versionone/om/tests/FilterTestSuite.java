/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BaseAssetFilterTester.class,
        ChangeSetFilterTester.class,
        IterationFilterTester.class,
        DefectFilterTester.class,
        PrimaryWorkitemFilterTester.class,
        IssueFilterTester.class,
        GoalFilterTester.class,
        EpicFilterTester.class,
        MemberFilterTester.class,
        SecondaryWorkitemFilterTester.class,
        StoryFilterTester.class,
        TaskFilterTester.class,
        TestFilterTester.class,
        WorkitemFilterTester.class,
        ThemeFilterTester.class,
        RetrospectiveFilterTester.class,
        NoteFilterTester.class,
        AttachmentFilterTester.class,
        EnvironmentFilterTester.class,
        RegressionPlanFilterTester.class,
        RegressionSuiteFilterTester.class,
        RegressionTestFilterTester.class,
        TestSetFilterTester.class
        })
public class FilterTestSuite {
}
