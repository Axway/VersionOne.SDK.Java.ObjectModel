package com.versionone.om.tests;

import com.versionone.om.*;
import com.versionone.om.listvalue.*;
import org.junit.Test;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class WrapperManagerTester extends TesterBase {

    /**
     * Test fillMap() method functionality.
     */
    @Test
    public void testFillMap() {
        Map<String, List<Class>> resultMap = new Hashtable<String, List<Class>>();
        Map<String, Class[]> expectedMap = new Hashtable<String, Class[]>();

        // Build expected result map
        expectedMap.put("ThemeSource", new Class[] { ThemeSource.class });
        expectedMap.put("Link", new Class[] { Link.class });
        expectedMap.put("StoryCategory", new Class[] { StoryType.class });
        expectedMap.put("Test", new Class[] { com.versionone.om.Test.class });
        expectedMap.put("BaseAsset", new Class[] { BaseAsset.class });
        expectedMap.put("Defect", new Class[] { DefectTemplate.class,
                Defect.class });
        expectedMap.put("TestSuite", new Class[] { TestSuite.class });
        expectedMap.put("TaskStatus", new Class[] { TaskStatus.class });
        expectedMap.put("RequestCategory", new Class[] { RequestType.class });
        expectedMap.put("WorkitemPriority",
                new Class[] { WorkitemPriority.class });
        expectedMap.put("ThemeStatus", new Class[] { ThemeStatus.class });
        expectedMap.put("ChangeSet", new Class[] { ChangeSet.class });
        expectedMap.put("ScopeStatus", new Class[] { ProjectStatus.class });
        expectedMap.put("Timebox", new Class[] { Iteration.class });
        expectedMap.put("PrimaryWorkitem",
                new Class[] { PrimaryWorkitem.class });
        expectedMap.put("Workitem", new Class[] { SecondaryWorkitem.class,
                Workitem.class });
        expectedMap.put("RegressionPlan", new Class[] { RegressionPlan.class });
        expectedMap.put("RegressionSuite", new Class[] { RegressionSuite.class });
        expectedMap.put("TestSet", new Class[] { TestSet.class });
        expectedMap.put("Environment", new Class[] { Environment.class });
        expectedMap.put("RegressionTest", new Class[] { RegressionTest.class });
        expectedMap.put("BuildProject", new Class[] { BuildProject.class });
        expectedMap.put("Note", new Class[] { Note.class });
        expectedMap.put("WorkitemRisk", new Class[] { WorkitemRisk.class });
        expectedMap.put("NoteCategory", new Class[] { NoteType.class });
        expectedMap.put("BuildRun", new Class[] { BuildRun.class });
        expectedMap.put("TestCategory", new Class[] { TestType.class });
        expectedMap.put("Member", new Class[] { Member.class });
        expectedMap.put("ThemeCategory", new Class[] { ThemeType.class });
        expectedMap.put("RequestPriority",
                new Class[] { RequestPriority.class });
        expectedMap.put("IssueCategory", new Class[] { IssueType.class });
        expectedMap.put("Theme", new Class[] { Theme.class });
        expectedMap.put("IssueResolution",
                new Class[] { IssueResolutionReason.class });
        expectedMap.put("DefectResolution",
                new Class[] { DefectResolutionReason.class });
        expectedMap.put("Scope", new Class[] { Project.class });
        expectedMap.put("BuildSource", new Class[] { BuildSource.class });
        expectedMap.put("Goal", new Class[] { Goal.class });
        expectedMap.put("Role", new Class[] { Role.class });
        expectedMap.put("GoalCategory", new Class[] { GoalType.class });
        expectedMap.put("IssuePriority", new Class[] { IssuePriority.class });
        expectedMap.put("Epic", new Class[] { Epic.class});
        expectedMap.put("Story", new Class[] { StoryTemplate.class,
                Story.class });
        expectedMap.put("DefectType", new Class[] { DefectType.class });
        expectedMap.put("AttachmentCategory",
                new Class[] { AttachmentType.class });
        expectedMap.put("Task", new Class[] { Task.class });
        expectedMap.put("BuildStatus", new Class[] { BuildStatus.class });
        expectedMap.put("Issue", new Class[] { Issue.class });
        expectedMap.put("RequestResolution",
                new Class[] { RequestResolution.class });
        expectedMap.put("Retrospective", new Class[] { Retrospective.class });
        expectedMap.put("Actual", new Class[] { Effort.class });
        expectedMap.put("TaskCategory", new Class[] { TaskType.class });
        expectedMap.put("State", new Class[] { State.class });
        expectedMap.put("GoalPriority", new Class[] { GoalPriority.class });
        expectedMap.put("StorySource", new Class[] { WorkitemSource.class });
        expectedMap.put("Attachment", new Class[] { Attachment.class });
        expectedMap.put("TestStatus", new Class[] { TestStatus.class });
        expectedMap.put("StoryStatus", new Class[] { WorkitemStatus.class });
        expectedMap.put("TaskSource", new Class[] { TaskSource.class });
        expectedMap.put("RequestStatus", new Class[] { RequestStatus.class });
        expectedMap.put("Team", new Class[] { Team.class });
        expectedMap.put("Request", new Class[] { Request.class });
        expectedMap.put("Schedule", new Class[] { Schedule.class });
        expectedMap.put("Message", new Class[] { Message.class });
        expectedMap.put("MessageReceipt", new Class[] { MessageReceipt.class });
        expectedMap.put("Expression", new Class[] { Conversation.class });

        // Invoke method
        try {
            invokePrivateMethod(WrapperManager.class, "fillMap",
                    new Class[] { Map.class }, resultMap);
        } catch (Exception e) {
            fail("Invocation of private method fillMap() failed.");
        }

        // Check result map
        assertFalse(resultMap.isEmpty());

        // Compare result map with expected map

        for (String key : resultMap.keySet()) {
            List<Class> resultValues = resultMap.get(key);
            Class[] expectedValues = expectedMap.get(key);

            assertTrue(resultValues != null);
            assertTrue(expectedValues != null);

            ListAssert.areEqual(expectedValues, resultValues);
        }
    }
}
