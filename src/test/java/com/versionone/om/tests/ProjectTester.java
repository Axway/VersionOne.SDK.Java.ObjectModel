/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.DB.DateTime;
import com.versionone.Duration;
import com.versionone.Oid;
import com.versionone.apiclient.OidException;
import com.versionone.om.*;
import com.versionone.om.TransformIterable.ITransformer;
import com.versionone.om.filters.ProjectFilter;
import com.versionone.om.filters.RegressionPlanFilter;
import com.versionone.om.filters.TestSetFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ProjectTester extends BaseSDKTester {

    private final String REGRESSION_PLAN_NAME = "Plan B";
    private final String TEST_SET_DESCRIPTION = "Plan B";

    @Test
    public void testInstanceEnumerable() {
        Collection<Project> projects = getInstance().getProjects();

        ListAssert.areEqual(new String[]{"System (All Projects)"}, projects,
                            new EntityToNameTransformer<Project>());
        ListAssert.areEqual(new String[]{"Scope:0"}, projects,
                            new EntityToAssetIDTransformer<Project>());
    }

    @Test
    public void testCreateProjectWithSpecialSymbols() {
        final String projectName = "1234 test & project !@#$%^&*()_+' ' \"";

        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        DateTime targetBeginDate = new DateTime("2009-01-01");
        Project subProject = project.createSubProject(projectName,
                                                      targetBeginDate);
        String subProjectID = subProject.getID().toString();

        resetInstance();

        ProjectFilter filter = new ProjectFilter();
        filter.name.add(projectName);
        Collection<Project> projectList = project.getChildProjects(filter);
        int numberOfProjects = projectList.size();

        Assert.assertNotSame(0, numberOfProjects);
        ITransformer<Project, String> nameTransformer = new ITransformer<Project, String>() {
            public String transform(Project input) {
                return input.getID().toString();
            }
        };

        ListAssert.contains(subProjectID, projectList, nameTransformer);

        subProject.delete();
    }

    @Test
    public void testSimpleProjectAttributes() {    	
    	
        Project project = getInstance().get().projectByID(SCOPE_ZERO);

        Assert.assertEquals(new DateTime("2007-09-08"), project.getBeginDate());
        Assert.assertNull(project.getEndDate());
        Assert.assertTrue(project.isActive());
        Assert.assertFalse(project.isClosed());
        Assert.assertNull(project.getParentProject());
        
        Assert.assertNull(project.getSchedule());  //test data does not have a schedule      
        
        Duration expected1 = new Duration("14 Days");
        Duration expected2 = new Duration("0 Days");
        
        Duration actual1 = project.getSchedule().getIterationLength();
        Duration actual2 = project.getSchedule().getIterationGap();
        
        Assert.assertEquals(expected1.getDays(), actual1.getDays());

        Assert.assertEquals(expected2.getDays(), actual2.getDays());
    }

    @Test
    public void testCreateProjectWithAttributes() {
        final String name = "TestProjectName";

        final String description = "Test for Project creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Project rootProject = getInstance().get().projectByID(SCOPE_ZERO);

        Project project = getInstance().create().project(name, rootProject, DateTime.now(), null, attributes);

        Assert.assertEquals(name, project.getName());
        Assert.assertEquals(description, project.getDescription());

        project.delete();
    }

    @Test
    public void testCreateSubProjectShareSchedule() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        DateTime targetBeginDate = new DateTime("2007-01-01");
        Project subProject = project.createSubProject("Sub Project A",
                                                      targetBeginDate);
        String subProjectID = subProject.getID().toString();

        resetInstance();

        Project cleanRootProject = getInstance().get().projectByID(
                SCOPE_ZERO);
        Project cleanSubProject = getInstance().get().projectByID(
                subProjectID);

        Assert.assertEquals(targetBeginDate, cleanSubProject.getBeginDate());
        Assert.assertNull(cleanSubProject.getEndDate());
        Assert.assertEquals(cleanRootProject, cleanSubProject
                .getParentProject());
        Assert.assertNull(cleanSubProject.getSchedule());
    }

    @Test
    public void testCreateSubProjectOwnSchedule() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        DateTime targetBeginDate = new DateTime("2007-01-01");
        Schedule ownSchedule = getInstance().create().schedule("Own Schedule", new Duration("7 Days"), new Duration("2 Days"));
        Project subProject = project.createSubProject("Sub Project B",
                                                      targetBeginDate, ownSchedule);
        String subProjectID = subProject.getID().toString();

        resetInstance();

        Project cleanRootProject = getInstance().get().projectByID(
                SCOPE_ZERO);
        Project cleanSubProject = getInstance().get().projectByID(
                subProjectID);

        Assert.assertEquals(targetBeginDate, cleanSubProject.getBeginDate());
        Assert.assertNull(cleanSubProject.getEndDate());
        Assert.assertEquals(cleanRootProject, cleanSubProject
                .getParentProject());
        Assert.assertNotNull(cleanSubProject.getSchedule());
        Assert.assertEquals(cleanSubProject.getSchedule().getIterationLength(),
                            new Duration("7 Days"));
        Assert.assertEquals(cleanSubProject.getSchedule().getIterationGap(),
                            new Duration("2 Days"));
    }

    @Test
    public void testAssignMember() {
        Project project = getInstance().get().projectByID(SCOPE_ZERO);
        Member member = getInstance().create().member("Duude", "dud",
                                                      Role.TEAM_MEMBER);
        Assert.assertFalse(project.getAssignedMembers().contains(member));
        project.getAssignedMembers().add(member);

        resetInstance();
        project = getInstance().get().projectByID(project.getID());
        member = getInstance().get().memberByID(member.getID());
        Assert.assertTrue(project.getAssignedMembers().contains(member));
    }

    private AssetID relationSandboxProjectID;

    private AssetID getRelationSandboxProjectID() {

        if (relationSandboxProjectID == null) {
            Project rootProject = getInstance().get().projectByID(
                    SCOPE_ZERO);
            Project sandbox = getInstance().create().project(
                    "SDK Project Relation Tests", rootProject, DateTime.now(),
                    null);
            relationSandboxProjectID = sandbox.getID();
        }
        return relationSandboxProjectID;
    }

    private Project getRelationSandbox() {
        return getInstance().get().projectByID(
                getRelationSandboxProjectID());
    }

    @Test
    public void testAddAndGetDefects() {
        Defect roach = getRelationSandbox().createDefect("Cockroach");
        resetInstance();

        roach = getInstance().get().defectByID(roach.getID());
        boolean found = findRelated(roach, getRelationSandbox()
                .getDefects(null));
        Assert.assertTrue("Expected to find defect '" + roach.getName()
                + "' in project '" + getRelationSandbox().getName() + "'.",
                          found);
    }

    @Test
    public void testAddAndGetStories() {
        Story peterPan = getRelationSandbox().createStory("Peter Pan");

        resetInstance();
        peterPan = getInstance().get().storyByID(peterPan.getID());
        boolean found = findRelated(peterPan, getRelationSandbox().getStories(
                null));
        Assert.assertTrue("Expected to find Story \"" + peterPan.getName()
                + "\" in Project \"" + getRelationSandbox().getName() + "\".",
                          found);
    }

    @Test
    public void testAddAndGetIssues() {
        Issue myMother = getRelationSandbox().createIssue("My Mother");

        resetInstance();
        myMother = getInstance().get().issueByID(myMother.getID());
        boolean found = findRelated(myMother, getRelationSandbox().getIssues(
                null));
        Assert.assertTrue("Expected to find Issue \"" + myMother.getName()
                + "\" in Project \"" + getRelationSandbox().getName() + "\".",
                          found);
    }

    @Test
    public void testAddAndGetRequests() {
        Request more = getRelationSandbox().createRequest(
                "Please Sir, I want some more.");

        resetInstance();
        more = getInstance().get().requestByID(more.getID());
        boolean found = findRelated(more, getRelationSandbox()
                .getRequests(null));
        Assert.assertTrue("Expected to find Request \"" + more.getName()
                + "\" in Project \"" + getRelationSandbox().getName() + "\".",
                          found);
    }

    @Test
    public void testAddAndGetGoals() {
        Goal win = getRelationSandbox().createGoal("Go.  Fight.  Win!");

        resetInstance();
        win = getInstance().get().goalByID(win.getID());
        boolean found = findRelated(win, getRelationSandbox().getGoals(null));
        Assert.assertTrue("Expected to find Goal \"" + win.getName()
                + "\" in Project \"" + getRelationSandbox().getName() + "\".",
                          found);
    }

    // #region Disabled Project Relations
    // [Test] public void GoalsInHierarchy()
    // {
    // Project parent = Instance.Create.Project("Goal Parent", RelationSandbox,
    // DateTime.Now, null);
    // Project child = Instance.Create.Project("Goal child", parent,
    // DateTime.Now, null);

    // Goal inherited = parent.CreateGoal("Inherited");
    // parent.GoalsTargetedDirectly.Add(inherited);

    // Goal direct = parent.CreateGoal("Direct");
    // child.GoalsTargetedDirectly.Add(direct);

    // ResetInstance();
    // parent = Instance.Get.ProjectByID(parent.ID);
    // child = Instance.Get.ProjectByID(child.ID);
    // inherited = Instance.Get.GoalByID(inherited.ID);
    // direct = Instance.Get.GoalByID(direct.ID);

    // Assert.IsTrue(parent.GoalsTargetedDirectly.Contains(inherited), "Expect
    // directly targeted goal");
    // Assert.IsTrue(child.GoalsTargetedDirectly.Contains(direct), "Expect
    // directly targeted goal");

    // Assert.IsTrue(FindRelated(inherited, child.GoalsTargetedViaParent),
    // "Expect goal targeted by parent.");

    // Assert.IsTrue(FindRelated(inherited, parent.GoalsAvailable), "Expect goal
    // in parent to be available to both parent and child.");

    // Assert.IsTrue(FindRelated(inherited, child.GoalsAvailable), "Expect goal
    // in parent to be available to both parent and child.");
    // Assert.IsTrue(FindRelated(direct, child.GoalsAvailable), "Expect goal in
    // child to be available to child.");

    // Assert.IsTrue(FindRelated(parent, inherited.TargetedBy), "Expect parent
    // to target \"inherited \"goal.");
    // Assert.IsTrue(FindRelated(child, direct.TargetedBy), "Expect parent to
    // target \"direct\"goal.");
    // }

    // [Test] public void EnumerateAvailableThemes()
    // {
    // IEnumerable<Theme> availableThemes =
    // Instance.Get.ProjectByID("Scope:1013").ThemesAvailable;
    // string[] expected = new string[] { "Theme:1055", "Theme:1056",
    // "Theme:1057","Theme:1058", "Theme:1059",
    // "Theme:1060","Theme:1061","Theme:1062" };
    // ListAssert.AreEqualIgnoringOrder(expected, availableThemes, new
    // EntityToAssetIDTransformer<Theme>());
    // }

    // [Test] public void EmptyEnumerateAvaiableThemes()
    // {
    // IEnumerable<Theme> availableThemes =
    // Instance.Get.ProjectByID("Scope:1011").ThemesAvailable;
    // ListAssert.AreEqual(new string[] {},availableThemes,new
    // EntityToAssetIDTransformer<Theme>());
    // }

    @Test
    public void testGetIterations() {
        String[] expected = new String[]{"Timebox:1021", "Timebox:1022",
                                         "Timebox:1023", "Timebox:1024", "Timebox:1025", "Timebox:1026",
                                         "Timebox:1027", "Timebox:1028"};
        Collection<Iteration> iterations = getInstance().get()
                .projectByID("Scope:1012").getIterations(null);

        ListAssert.areEqualIgnoringOrder(expected, iterations,
                                         new EntityToAssetIDTransformer<Iteration>());
    }

    @Test
    public void testGetIterationsEmpty() {
        Collection<Iteration> iterations = getInstance().get()
                .projectByID("Scope:1011").getIterations(null);

        ListAssert.areEqualIgnoringOrder(new Object[]{}, iterations,
                                         new EntityToAssetIDTransformer<Iteration>());
    }

    @Test
    public void testGetIterationsDefinedInParentProject() {
        //Children projects no longer inherit their parent's schedule, so the child project
        Project parentProject = getInstance().get().projectByID("Scope:1012");
        Project childProject = parentProject.createSubProject("Sub Project with No Schedule", DateTime.now());
        Collection<Iteration> iterations = childProject.getIterations(null);
        String[] expected = new String[]{};
        ListAssert.areEqualIgnoringOrder(expected, iterations,
                                         new EntityToAssetIDTransformer<Iteration>());
    }

    @Test
    public void testSubProjects() {
        Assert.assertTrue(getInstance().get().projectByID("Scope:0")
                .getThisAndAllChildProjects().size() > 1);
    }

    @Test
    public void testGetProjectByName() {
        String projectName = newGuid();
        Project mine = getInstance().create().project(projectName,
                                                      getSandboxProject().getID(), DateTime.now(), null);
        getEntityFactory().registerForDisposal(mine);

        resetInstance();
        mine = getInstance().get().projectByID(mine.getID());

        Assert.assertEquals(mine.getID(), getInstance().get()
                .projectByName(projectName).getID());
    }

    @Test
    public void testGetProjectByNameWithSpecialSymbols() {
        final String projectName = "test 123 @#$%^&*()_+!~ ' '  \" \"" + newGuid();
        final Project mainProject = getSandboxProject();

        Project mine = getInstance().create().project(projectName, mainProject.getID(), DateTime.now(), null);
        getEntityFactory().registerForDisposal(mine);

        resetInstance();
        mine = getInstance().get().projectByID(mine.getID());

        Assert.assertEquals(mine.getID(), getInstance().get().projectByName(projectName).getID());
    }

    @Test
    public void getRegressionPlanWithValuableFilterTest() throws OidException {
        Member owner = getEntityFactory().createMember("Paul");
        Member nobody = getEntityFactory().createMember("Mike");

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Owner", Oid.fromToken(owner.getID().toString(), getInstance().getApiClient().getMetaModel()));
        RegressionPlan plan = getEntityFactory().createRegressionPlan(REGRESSION_PLAN_NAME,
                                                                      getSandboxProject(), attributes);

        RegressionPlanFilter filter = new RegressionPlanFilter();
        filter.owners.add(owner);
        Collection<RegressionPlan> plans = getSandboxProject().getRegressionPlans(filter);
        Assert.assertEquals(1, plans.size());
        ListAssert.contains(plan, plans);

        filter = new RegressionPlanFilter();
        filter.project.add(getSandboxProject());
        filter.owners.add(nobody);
        plans = getSandboxProject().getRegressionPlans(filter);
        Assert.assertEquals(0, plans.size());
    }

    @Test
    public void testCreateTestSet() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(REGRESSION_PLAN_NAME, getSandboxProject());
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite("suite1", regressionPlan);

        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", TEST_SET_DESCRIPTION);
        TestSet testSet = getEntityFactory().createTestSet("my test set", regressionSuite, attributes);

        resetInstance();

        List<TestSet> testSets = new ArrayList<TestSet>(regressionSuite.getTestSets(new TestSetFilter()));
        ListAssert.contains(testSet, testSets);
        Assert.assertTrue(testSets.get(0).getProject().equals(getSandboxProject()));
        Assert.assertTrue(testSets.get(0).getDescription().equals(TEST_SET_DESCRIPTION));
    }

    @Test
    public void testRetrieveTestSet() {
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(REGRESSION_PLAN_NAME,
                                                                                getSandboxProject());
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite("suite1", regressionPlan);
        Collection<TestSet> beforeCreation = getSandboxProject().getTestSets(new TestSetFilter());
        TestSet testSet = getEntityFactory().createTestSet("my test set", regressionSuite);

        resetInstance();

        List<TestSet> afterCreation = new ArrayList<TestSet>(getSandboxProject().getTestSets(new TestSetFilter()));
        ListAssert.contains(testSet, afterCreation);
        ListAssert.notcontains(testSet, beforeCreation);
        Assert.assertTrue(afterCreation.size() - beforeCreation.size() == 1);
    }

    @Test
    public void testRetrieveTestSetFromChildProject() {
        Project subProject = getSandboxProject().createSubProject("subproject", DateTime.now());
        getEntityFactory().registerForDisposal(subProject);
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(REGRESSION_PLAN_NAME, subProject);
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite("suite1", regressionPlan);
        TestSet testSet = getInstance().create().testSet("my test set", regressionSuite, subProject);
        getEntityFactory().registerForDisposal(testSet);

        resetInstance();

        Collection<TestSet> testSetsFromSandboxProject = getSandboxProject().getTestSets(new TestSetFilter());
        Collection<TestSet> testSetsFromChildProject = subProject.getTestSets(new TestSetFilter());
        Collection<TestSet> testSetsFromSandboxProjectAndChildren =
                getSandboxProject().getTestSets(new TestSetFilter(), true);

        ListAssert.contains(testSet, testSetsFromSandboxProjectAndChildren);
        ListAssert.contains(testSet, testSetsFromChildProject);
        ListAssert.notcontains(testSet, testSetsFromSandboxProject);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateTestSetWithInvalidParameters() {
        Project subProject = getSandboxProject().createSubProject("subproject", DateTime.now());
        getEntityFactory().registerForDisposal(subProject);
        RegressionPlan regressionPlan = getEntityFactory().createRegressionPlan(REGRESSION_PLAN_NAME,
                                                                                getSandboxProject());
        RegressionSuite regressionSuite = getEntityFactory().createRegressionSuite("suite1", regressionPlan);
        getEntityFactory().registerForDisposal(getInstance().create().testSet("my test set",
                                                                              regressionSuite, subProject));
    }

    @Test
    public void createRegressionTestTest() {
        String regressionTestName = "Regression Test from Project";
        Project subProject = getSandboxProject().createSubProject("subProject", DateTime.now());
        getEntityFactory().registerForDisposal(subProject);
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName, subProject);

        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());

        Assert.assertEquals(regressionTestName, regressionTestNew.getName());
        Assert.assertEquals(subProject, regressionTestNew.getProject());
    }

    @Test
    public void createRegressionTestWithAttributesTest() {
        String regressionTestName = "Regression Test from Project";
        String regressionTestTags = "test tags regression";
        Project subProject = getSandboxProject().createSubProject("subProject", DateTime.now());
        getEntityFactory().registerForDisposal(subProject);
        Map<String, Object> attributes = createAttributesWithDescription(regressionTestName);
        attributes.put("Tags", regressionTestTags);
        RegressionTest regressionTest = getEntityFactory().createRegressionTest(regressionTestName,
                                                                                subProject, attributes);
        resetInstance();

        RegressionTest regressionTestNew = getInstance().get().regressionTestByID(regressionTest.getID());

        Assert.assertEquals(regressionTestName, regressionTestNew.getName());
        Assert.assertEquals(0, regressionTestNew.getOwners().size());
        Assert.assertEquals(regressionTestTags, regressionTestNew.getTags());
        Assert.assertEquals(attributes.get("Description"), regressionTestNew.getDescription());
    }
}
