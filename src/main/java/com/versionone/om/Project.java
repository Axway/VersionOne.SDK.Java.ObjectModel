/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.DB.DateTime;
import com.versionone.om.filters.*;
import com.versionone.om.listvalue.ProjectStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Represents a project or release in the VersionOne system.
 */
@MetaDataAttribute("Scope")
public class Project extends BaseAsset {
    private static final String PARENT = "Parent";
    private static final String BEGIN_DATE = "BeginDate";
    private static final String END_DATE = "EndDate";
    private static final String OWNER = "Owner";
    private static final String TEST_SUITE = "TestSuite";
    private static final String SCHEDULE = "Schedule";

    /**
     * Constructor used to represent an Project entity that DOES exist in the
     * VersionOne System.
     *
     * @param id       Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Project(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Project entity that does NOT yet exist
     * in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Project(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Parent project this project belongs to.
     */
    @MetaRenamedAttribute(PARENT)
    public Project getParentProject() {
        return getRelation(Project.class, PARENT);
    }

    /**
     * @param parentProject Parent project this project belongs to.
     */
    public void setParentProject(Project parentProject) {
        setRelation(PARENT, parentProject);
    }

    /**
     * @return Date this project begins.
     */
    public DateTime getBeginDate() {
        final Object date = get(BEGIN_DATE);
        return (date == null) ? null : new DateTime(date);
    }

    /**
     * @param beginDate Date this project begins.
     */
    public void setBeginDate(DateTime beginDate) {
        set(BEGIN_DATE, beginDate.getDate());
    }

    /**
     * @return Date this project ends.
     */
    public DateTime getEndDate() {
        final Object date = get(END_DATE);
        return (date == null) ? null : new DateTime(date);
    }

    /**
     * @param value Date this project ends.
     */
    public void setEndDate(DateTime value) {
        set(END_DATE, value.getDate());
    }

    /**
     * @return Schedule that defines how this project's iterations are spaced.
     */
    public Schedule getSchedule() {
        return getRelation(Schedule.class, SCHEDULE);
    }

    /**
     * @param schedule Schedule that defines how this project's iterations are
     *                 spaced.
     */
    public void setSchedule(Schedule schedule) {
        setRelation(SCHEDULE, schedule);
    }

    /**
     * @return The Member who owns this Project.
     */
    public Member getOwner() {
        return getRelation(Member.class, OWNER);
    }

    /**
     * @param owner The Member who owns this Project.
     */
    public void setOwner(Member owner) {
        setRelation(OWNER, owner);
    }

    /**
     * @return TestSuite assigned to this Project.
     */
    public TestSuite getTestSuite() {
        return getRelation(TestSuite.class, TEST_SUITE);
    }

    /**
     * @param testSuite TestSuite assigned to this Project.
     */
    public void setTestSuite(TestSuite testSuite) {
        setRelation(TEST_SUITE, testSuite);
    }

    /**
     * @return This Project's Status.
     */
    public IListValueProperty getStatus() {
        return getListValue(ProjectStatus.class, "Status");
    }

    /**
     * @return Build Projects associated with this Project.
     */
    public Collection<BuildProject> getBuildProjects() {
        return getMultiRelation("BuildProjects");
    }

    /**
     * Create a sub project under this project with a name, begin date, and
     * optional schedule.
     *
     * @param name      Name of the new project.
     * @param beginDate Date the schedule will begin.
     * @param schedule  The new schedule. If null, the project will inherit the
     *                  parent project's schedule.
     * @return The newly created project.
     */
    public Project createSubProject(String name, DateTime beginDate, Schedule schedule) {
        return getInstance().create().project(name, this, beginDate, schedule);
    }

    public Project createSubProject(String name, DateTime beginDate, Schedule schedule,
                                    Map<String, Object> attributes) {
        return getInstance().create().project(name, this, beginDate, schedule, attributes);
    }

    /**
     * Create a sub project under this project with a name and begin date.
     *
     * @param name      Name of the new project.
     * @param beginDate Date the schedule will begin.
     * @return The newly created project.
     */
    public Project createSubProject(String name, DateTime beginDate) {
        return createSubProject(name, beginDate, null);
    }


    /**
     * Create a new Epic in this Project.
     *
     * @param name The initial name of the Epic.
     * @return A new Epic.
     */
    public Epic createEpic(String name) {
        return getInstance().create().epic(name, this);
    }

    /**
     * Create a new Epic in this Project.
     *
     * @param name       The initial name of the Epic.
     * @param attributes additional attributes for the Epic.
     * @return A new Epic.
     */
    public Epic createEpic(String name, Map<String, Object> attributes) {
        return getInstance().create().epic(name, this, attributes);
    }

    /**
     * Create a new Story in this Project.
     *
     * @param name The initial name of the Story.
     * @return A new Story.
     */
    public Story createStory(String name) {
        return getInstance().create().story(name, this);
    }

    public Story createStory(String name, Map<String, Object> attributes) {
        return getInstance().create().story(name, this, attributes);
    }

    /**
     * Create a new Defect in this Project.
     *
     * @param name The initial name of the Defect.
     * @return A new Defect.
     */
    public Defect createDefect(String name) {
        return getInstance().create().defect(name, this);
    }

    /**
     * Create a new Defect in this Project.
     *
     * @param name       The initial name of the Defect.
     * @param attributes additional attributes for defect
     * @return A new Defect.
     */
    public Defect createDefect(String name, Map<String, Object> attributes) {
        return getInstance().create().defect(name, this, attributes);
    }

    /**
     * Create a new Theme in this Project.
     *
     * @param name The initial name of the Theme.
     * @return A new Theme.
     */
    public Theme createTheme(String name) {
        return getInstance().create().theme(name, this);
    }

    /**
     * Create a new Theme in this Project.
     *
     * @param name       The initial name of the Theme.
     * @param attributes additional attributes for Theme.
     * @return A new Theme.
     */
    public Theme createTheme(String name, Map<String, Object> attributes) {
        return getInstance().create().theme(name, this, attributes);
    }

    /**
     * Create a new Goal in this Project.
     *
     * @param name The initial name of the Goal.
     * @return A new Goal.
     */
    public Goal createGoal(String name) {
        return getInstance().create().goal(name, this);
    }

    /**
     * Create a new Goal in this Project.
     *
     * @param name       The initial name of the Goal.
     * @param attributes additional attributes for the Goal.
     * @return A new Goal.
     */
    public Goal createGoal(String name, Map<String, Object> attributes) {
        return getInstance().create().goal(name, this, attributes);
    }

    /**
     * Create a new Request in this Project.
     *
     * @param name The initial name of the Request.
     * @return A new Request.
     */
    public Request createRequest(String name) {
        return getInstance().create().request(name, this);
    }

    /**
     * Create a new Request in this Project.
     *
     * @param name       The initial name of the Request.
     * @param attributes additional attributes for the Request.
     * @return A new Request.
     */
    public Request createRequest(String name, Map<String, Object> attributes) {
        return getInstance().create().request(name, this, attributes);
    }

    /**
     * Create a new Issue in this Project.
     *
     * @param name The initial name of the Issue.
     * @return A new Issue.
     */
    public Issue createIssue(String name) {
        return getInstance().create().issue(name, this);
    }

    /**
     * Create a new Issue in this Project.
     *
     * @param name       The initial name of the Issue.
     * @param attributes additional attributes for the Issue.
     * @return A new Issue.
     */
    public Issue createIssue(String name, Map<String, Object> attributes) {
        return getInstance().create().issue(name, this, attributes);
    }

    /**
     * Create a new Retrospective in this Project.
     *
     * @param name The initial name of the Retrospective.
     * @return A new Retrospective.
     */
    public Retrospective createRetrospective(String name) {
        return getInstance().create().retrospective(name, this);
    }

    /**
     * Create a new Retrospective in this Project.
     *
     * @param name       The initial name of the Retrospective.
     * @param attributes additional attributes for Retrospective.
     * @return A new Retrospective.
     */
    public Retrospective createRetrospective(String name, Map<String, Object> attributes) {
        return getInstance().create().retrospective(name, this, attributes);
    }

    /**
     * Create a new Iteration in the Project where the schedule is defined. Use
     * the suggested system values for the new iteration.
     *
     * @return A new Iteration.
     */
    public Iteration createIteration() {
        return getInstance().create().iteration(this);
    }

    /**
     * Create a new Iteration in the Project where the schedule is defined. Use
     * the suggested system values for the new iteration.
     *
     * @param attributes additional attributes for the Iteration.
     * @return A new Iteration.
     */
    public Iteration createIteration(Map<String, Object> attributes) {
        return getInstance().create().iteration(this, attributes);
    }

    /**
     * Create a new Iteration in the Project where the schedule is defined.
     *
     * @param name      The initial name of the Iteration.
     * @param beginDate The begin date of the Iteration.
     * @param endDate   The end date of the Iteration.
     * @return A new Iteration.
     */
    public Iteration createIteration(String name, DateTime beginDate, DateTime endDate) {
        return getInstance().create().iteration(name, getSchedule(), beginDate, endDate);
    }

    /**
     * Create a new Iteration in the Project where the schedule is defined.
     *
     * @param name       The initial name of the Iteration.
     * @param beginDate  The begin date of the Iteration.
     * @param endDate    The end date of the Iteration.
     * @param attributes additional attributes for Iteration.
     * @return A new Iteration.
     */
    public Iteration createIteration(String name, DateTime beginDate, DateTime endDate,
                                     Map<String, Object> attributes) {
        return getInstance().create().iteration(name, getSchedule(), beginDate, endDate, attributes);
    }

    /**
     * Creates a new Regression Plan in the Project with additional attributes.
     *
     * @param name Regression Plan title.
     * @return A new Regression Plan
     */
    public RegressionPlan createRegressionPlan(String name) {
        return getInstance().create().regressionPlan(name, this);
    }

    /**
     * Creates a new Regression Plan in the Project with additional attributes.
     *
     * @param name       Regression Plan title.
     * @param attributes Additional attributes for initialization Regression Plan
     * @return A new Regression Plan
     */
    public RegressionPlan createRegressionPlan(String name, Map<String, Object> attributes) {
        return getInstance().create().regressionPlan(name, this, attributes);
    }

    /**
     * Create a new Test Set in the project.
     *
     * @param name       Test Set name
     * @param suite      Parent Regression suite
     * @param attributes Additional attributes
     * @return Newly created test set
     */
    public TestSet createTestSet(String name, RegressionSuite suite, Map<String, Object> attributes) {
        return getInstance().create().testSet(name, suite, this, attributes);
    }

    /**
     * Create a new Test Set in the project.
     *
     * @param name  Test Set name
     * @param suite Parent Regression suite
     * @return Newly created test set
     */
    public TestSet createTestSet(String name, RegressionSuite suite) {
        return getInstance().create().testSet(name, suite, this, null);
    }

    /**
     * Creates a new Environment in the Project.
     *
     * @param name Environment title.
     * @return A new Environment.
     */
    public Environment createEnvironment(String name) {
        return getInstance().create().environment(name, this);
    }

    /**
     * Creates a new Environment in the Project with additional attributes.
     *
     * @param name       Environment title.
     * @param attributes Additional attributes for initialization Environment.
     * @return A new Environment.
     */
    public Environment createEnvironment(String name, Map<String, Object> attributes) {
        return getInstance().create().environment(name, this, attributes);
    }

    /**
     * Creates a new Regression Test in the Project.
     *
     * @param name Name of Regression Test.
     * @return A new Regression Test.
     */
    public RegressionTest createRegressionTest(String name) {
        return getInstance().create().regressionTest(name, this);
    }

    /**
     * Creates a new Regression Test in the Project.
     *
     * @param name       Name of Regression Test.
     * @param attributes Additional attributes for initialization RegressionTest.
     * @return A new Regression Test.
     */
    public RegressionTest createRegressionTest(String name, Map<String, Object> attributes) {
        return getInstance().create().regressionTest(name, this, attributes);
    }

    /**
     * @return Members assigned to this project.
     */
    public Collection<Member> getAssignedMembers() {
        return getMultiRelation("Members");
    }

    // commented in .NET version
    /* public Iterable<Theme> getThemesAvailable() {
    * // This wants to be Workitems:Theme, if it weren't for MS KB 932552. return
    *  getMultiRelation("ParentMeAndUp.Workitems:Theme");
    * }
    */

    /**
     * @return A read-only collection of StoryTemplates in this Project.
     */
    public Collection<StoryTemplate> getStoryTemplates() {
        // This wants to be Workitems:Story, if it weren't for MS KB 932552.
        EntityCollection<StoryTemplate> c = getMultiRelation("Workitems[AssetType='Story';AssetState='200']");
        return c.asReadOnly();
    }

    /**
     * @return A read-only collection of DefectTemplates in the Project.
     */
    public Collection<DefectTemplate> getDefectTemplates() {
        // This wants to be Workitems:Defect, if it weren't for MS KB 932552.
        final EntityCollection<DefectTemplate> c = getMultiRelation("Workitems[AssetType='Defect';AssetState='200']");
        return c.asReadOnly();
    }

    /**
     * Returns a flattened collection of this project and all Projects that
     * descend from this project.
     *
     * @return read-only collection.
     */
    public Collection<Project> getThisAndAllChildProjects() {
        EntityCollection<Project> x = getMultiRelation("ChildrenMeAndDown[AssetState!='Closed']");
        return x.asReadOnly();
    }

    /**
     * @param <T>                entity type.
     * @param filter             cannot be null.
     * @param includeSubprojects Specifies whether to include items from
     *                           sub-project or not. This only adds open subprojects.
     * @return requested filter.
     */
    private <T extends ProjectAssetFilter> T getFilter(T filter, boolean includeSubprojects) {
        // TODO in .NET version, filter is created if null has been passed to this method.
        assert filter != null;

        filter.project.clear();

        if (includeSubprojects) {
            filter.project.addAll(getThisAndAllChildProjects());
        } else {
            filter.project.add(this);
        }

        return filter;
    }

    /**
     * A collection of sub-projects that belong to this project.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all child projects in the project are returned.
     * @return A collection projects that belong to this project filtered by the
     *         passed in filter.
     */
    public Collection<Project> getChildProjects(ProjectFilter filter) {
        return getChildProjects(filter, false);
    }

    /**
     * A collection of sub-projects that belong to this project.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all child projects in the project are returned.
     * @param includeSubprojects Specifies whether to include items from
     *                           sub-project or not. This only adds open subprojects.
     * @return A collection projects that belong to this project filtered by the
     *         passed in filter.
     */
    public Collection<Project> getChildProjects(ProjectFilter filter, boolean includeSubprojects) {

        if (filter == null) {
            filter = new ProjectFilter();
        }
        filter.parent.clear();

        if (includeSubprojects) {
            filter.parent.addAll(getThisAndAllChildProjects());
        } else {
            filter.parent.add(this);
        }
        return getInstance().get().projects(filter);
    }

    /**
     * A collection of Effort records that belong to this project.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all effort records in the project are returned.
     * @return A collection efforts that belong to this project filtered by the
     *         passed in filter.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter) {
        return getEffortRecords(filter, false);
    }

    /**
     * A collection of Effort records that belong to this project.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all effort records in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return A collection efforts that belong to this project filtered by the
     *         passed in filter.
     */
    public Collection<Effort> getEffortRecords(EffortFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new EffortFilter();

        filter.project.clear();

        if (includeSubprojects) {

            for (Project p : getThisAndAllChildProjects()) {
                filter.project.add(p);
            }
        } else {
            filter.project.add(this);
        }
        return getInstance().get().effortRecords(filter);
    }

    /**
     * Get Epics in this Project filtered as specified in the passed in filter.
     * Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all epics in the project are returned.
     * @return A read only Collection of Epic.
     */
    public Collection<Epic> getEpics(EpicFilter filter) {
        return getEpics(filter, false);
    }

    /**
     * Get Epics in this Project filtered as specified in the passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all epics in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return A read only Collection of Epic.
     */
    public Collection<Epic> getEpics(EpicFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new EpicFilter();

        return getInstance().get().epics(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get tracked Epics that belong to current Project.
     */
    public Collection<Epic> GetTrackedEpics() {
        Collection<Project> projects = new ArrayList<Project>();
        projects.add(this);
        return getInstance().get().trackedEpics(projects);
    }

    /**
     * Get stories in this Project filtered as specified in the passed in
     * filter. Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all stories in the project are returned.
     * @return An Collection of Story.
     */
    public Collection<Story> getStories(StoryFilter filter) {
        return getStories(filter, false);
    }

    /**
     * Get stories in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all stories in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return An Collection of Story.
     */
    public Collection<Story> getStories(StoryFilter filter, boolean includeSubprojects) {

        filter = (filter != null) ? filter : new StoryFilter();
        return getInstance().get().story(
                getFilter(filter, includeSubprojects));
    }

    /**
     * filter Get Defects in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all defects in the project are returned.
     * @return An Collection of Defect.
     */
    public Collection<Defect> getDefects(DefectFilter filter) {
        return getDefects(filter, false);
    }

    /**
     * Get Defects in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all defects in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return An Collection of Defect.
     */
    public Collection<Defect> getDefects(DefectFilter filter, boolean includeSubprojects) {

        if (filter == null)
            filter = new DefectFilter();
        return getInstance().get().defects(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get test sets in this Project filtered as specified in the passed in filter. Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically. If null, all test sets in the project are returned.
     * @returns Collection of Test Sets
     */
    public Collection<TestSet> getTestSets(TestSetFilter filter) {
        return getTestSets(filter, false);
    }

    /**
     * Get test sets in this Project filtered as specified in the passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically. If null, all test sets in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub project or not. This only adds open subprojects.
     * @return Collection of Test Sets
     */
    public Collection<TestSet> getTestSets(TestSetFilter filter, boolean includeSubprojects) {
        return getInstance().get().testSets(getFilter(filter, includeSubprojects));
    }

    /**
     * Get PrimaryWorkitems in this Project filtered as specified in the passed
     * in filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all stories and defects in the project are
     *               returned.
     * @return An Collection of PrimaryWorkitem.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        return getPrimaryWorkitems(filter, false);
    }

    /**
     * Get PrimaryWorkitems in this Project filtered as specified in the passed
     * in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all stories and defects in the project are
     *                           returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return An Collection of PrimaryWorkitem.
     */
    public Collection<PrimaryWorkitem> getPrimaryWorkitems(PrimaryWorkitemFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        return getInstance().get().primaryWorkitems(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get Iterations in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all iterations in the project are returned.
     * @return A read only Collection of Iteration.
     */
    public Collection<Iteration> getIterations(IterationFilter filter) {
        return getIterations(filter, false);
    }

    /**
     * Get Iterations in this Project's schedule filtered as specified in the passed in
     * filter. This returns iterations even if the schedule is defined in a
     * parent project.
     *
     * @param filter             Criteria to filter on. Schedule will be set automatically.
     *                           If null, all iterations in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return A read only Collection of Iteration.
     */
    public Collection<Iteration> getIterations(IterationFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new IterationFilter();

        filter.schedule.clear();

        if (includeSubprojects) {

            for (Project p : getThisAndAllChildProjects()) {
                filter.schedule.add(p.getSchedule());
            }
        } else {
            filter.schedule.add(getSchedule());
        }

        return getInstance().get().iterations(filter);
    }

    /**
     * Get Themes in this Project filtered as specified in the passed in filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all themes in the project are returned.
     * @return A read only Collection of Theme.
     */
    public Collection<Theme> getThemes(ThemeFilter filter) {
        return getThemes(filter, false);
    }

    /**
     * Get Themes in this Project filtered as specified in the passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all themes in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return A read only Collection of Theme.
     */
    public Collection<Theme> getThemes(ThemeFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new ThemeFilter();

        return getInstance().get().themes(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get SecondaryWorkitems in this Project filtered as specified in the
     * passed in filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all tasks and tests in the project are returned.
     * @return An Collection of SecondaryWorkitem.
     */
    public Collection<SecondaryWorkitem> getSecondaryWorkitems(SecondaryWorkitemFilter filter) {
        return getSecondaryWorkitems(filter, false);
    }

    /**
     * Get SecondaryWorkitems in this Project filtered as specified in the
     * passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all tasks and tests in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open subprojects.
     * @return An Collection of SecondaryWorkitem.
     */
    public Collection<SecondaryWorkitem> getSecondaryWorkitems(SecondaryWorkitemFilter filter,
                                                               boolean includeSubprojects) {
        filter = (filter != null) ? filter : new SecondaryWorkitemFilter();

        return getInstance().get().secondaryWorkitems(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get Tasks in this Project filtered as specified in the passed in filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all tasks in the project are returned.
     * @return An Collection of SecondaryWorkitem.
     */
    public Collection<Task> getTasks(TaskFilter filter) {
        filter = (filter != null) ? filter : new TaskFilter();

        return getInstance().get().tasks(getFilter(filter, false));
    }

    /**
     * Get Tests in this Project filtered as specified in the passed in filter.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all tests in the project are returned.
     * @return An Collection of Tests.
     */
    public Collection<Test> getTests(TestFilter filter) {
        filter = (filter != null) ? filter : new TestFilter();

        return getInstance().get().tests(getFilter(filter, false));
    }

    /**
     * Get Requests in this Project filtered as specified in the passed in
     * filter. Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all Requests in the project are returned.
     * @return An Collection of Requests.
     */
    public Collection<Request> getRequests(RequestFilter filter) {
        return getRequests(filter, false);
    }

    /**
     * Get Requests in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all Requests in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open sub projects.
     * @return A read only Collection of Request.
     */
    public Collection<Request> getRequests(RequestFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new RequestFilter();

        return getInstance().get().requests(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get Goals in this Project filtered as specified in the passed in filter.
     * Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all Goals in the project are returned.
     * @return An Collection of Goals.
     */
    public Collection<Goal> getGoals(GoalFilter filter) {
        return getGoals(filter, false);
    }

    /**
     * Get Goals in this Project filtered as specified in the passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all Goals in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open sub projects.
     * @return An Collection of Goals.
     */
    public Collection<Goal> getGoals(GoalFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new GoalFilter();

        return getInstance().get().goals(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get Retrospective in this Project filtered as specified in the passed in
     * filter. Does not include sub projects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all Retrospective in the project are returned.
     * @return A read only Collection of Retrospective.
     */
    public Collection<Retrospective> getRetrospectives(RetrospectiveFilter filter) {
        return getRetrospectives(filter, false);
    }

    /**
     * Get Retrospective in this Project filtered as specified in the passed in
     * filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all Retrospective in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open sub projects.
     * @return A read only Collection of Retrospective.
     */
    public Collection<Retrospective> getRetrospectives(RetrospectiveFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new RetrospectiveFilter();

        return getInstance().get().retrospectives(
                getFilter(filter, includeSubprojects));
    }

    /**
     * Get Issues in this Project filtered as specified in the passed in filter.
     * Does not include subprojects.
     *
     * @param filter Criteria to filter on. Project will be set automatically.
     *               If null, all Issues in the project are returned.
     * @return A read only Collection of Issues.
     */
    public Collection<Issue> getIssues(IssueFilter filter) {
        return getIssues(filter, false);
    }

    /**
     * Get Issues in this Project filtered as specified in the passed in filter.
     *
     * @param filter             Criteria to filter on. Project will be set automatically.
     *                           If null, all Issues in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub
     *                           project or not. This only adds open sub projects.
     * @return A read only Collection of Issues.
     */
    public Collection<Issue> getIssues(IssueFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new IssueFilter();

        return getInstance().get().issues(
                getFilter(filter, includeSubprojects));
    }

    /**
     * A collection of regression plans that belong to this project.
     *
     * @param filter             Criteria to filter on. Project(s) will be set automatically.
     *                           If null, all related regression items in the project are returned.
     * @param includeSubprojects Specifies whether to include items from sub-project or not.
     *                           This only adds open subprojects.
     * @return RegressionPlan.
     */
    public Collection<RegressionPlan> getRegressionPlans(RegressionPlanFilter filter, boolean includeSubprojects) {
        filter = (filter != null) ? filter : new RegressionPlanFilter();

        filter.project.clear();
        if (includeSubprojects) {
            for (Project project : getThisAndAllChildProjects()) {
                filter.project.add(project);
            }
        } else {
            filter.project.add(this);
        }
        return getInstance().get().regressionPlans(filter);
    }

    /**
     * A collection of regression plans that belong to this project.
     *
     * @param filter Criteria to filter on. Project(s) will be set automatically.
     *               If null, all related regression items in the project are returned.
     * @return RegressionPlan.
     */
    public Collection<RegressionPlan> getRegressionPlans(RegressionPlanFilter filter) {
        return getRegressionPlans(filter, false);
    }

    /**
     * Inactivates the Project.
     *
     * @throws UnsupportedOperationException The Project is an invalid state for
     *                                       the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Project.
     *
     * @throws UnsupportedOperationException The Project is an invalid state for
     *                                       the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }

    private Double getRollup(String multiRelation, String attribute,
                             EntityFilter filter, boolean includeChildProjects) {

        if (includeChildProjects) {
            multiRelation = "ChildrenMeAndDown[AssetState!='Closed']."
                    + multiRelation;
        }
        return getSum(multiRelation, filter, attribute);
    }

    /**
     * Retrieves the total estimate for all stories and defects in this project
     * optionally filtered.
     *
     * @param filter               Criteria to filter stories and defects on.
     * @param includeChildProjects If true, include open sub projects, otherwise
     *                             only include this project.
     * @return total estimate of selected Workitems.
     */
    public Double getTotalEstimate(PrimaryWorkitemFilter filter, boolean includeChildProjects) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        return getRollup("Workitems:PrimaryWorkitem", "Estimate", filter,
                         includeChildProjects);
    }

    /**
     * Retrieves the total estimate for all stories and defects in this project
     * optionally filtered.
     *
     * @param filter Criteria to filter stories and defects on.
     * @return total estimate of selected Workitems.
     */
    public Double getTotalEstimate(PrimaryWorkitemFilter filter) {
        return getTotalEstimate(filter, false);
    }

    /**
     * Count the total detail estimate for all workitems in this project
     * optionally filtered.
     *
     * @param filter               Criteria to filter workitems on.
     * @param includeChildProjects If true, include open sub projects, otherwise
     *                             only include this project.
     * @return total detail estimate for all workitems in this project
     *         optionally filtered.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter, boolean includeChildProjects) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getRollup("Workitems", "DetailEstimate", filter,
                         includeChildProjects);
    }

    /**
     * Count the total detail estimate for all workitems in this project
     * optionally filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total detail estimate for all workitems in this project
     *         optionally filtered.
     */
    public Double getTotalDetailEstimate(WorkitemFilter filter) {
        return getTotalDetailEstimate(filter, false);
    }

    /**
     * Retrieves the total to do for all workitems in this project optionally
     * filtered.
     *
     * @param filter               Criteria to filter workitems on.
     * @param includeChildProjects If true, include open sub projects, otherwise
     *                             only include this project.
     * @return total to do of selected Workitems.
     */
    public Double getTotalToDo(WorkitemFilter filter, boolean includeChildProjects) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getRollup("Workitems", "ToDo", filter, includeChildProjects);
    }

    /**
     * Retrieves the total to do for all workitems in this project optionally
     * filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total to do for selected workitems.
     */
    public Double getTotalToDo(WorkitemFilter filter) {
        return getTotalToDo(filter, false);
    }

    /**
     * Retrieves the total done for all workitems in this project optionally
     * filtered.
     *
     * @param filter               Criteria to filter workitems on.
     * @param includeChildProjects If true, include open sub projects, otherwise
     *                             only include this project.
     * @return total done for selected workitems.
     */
    public Double getTotalDone(WorkitemFilter filter, boolean includeChildProjects) {
        filter = (filter != null) ? filter : new WorkitemFilter();

        return getRollup("Workitems", "Actuals.Value", filter,
                         includeChildProjects);
    }

    /**
     * Retrieves the total done for all workitems in this project optionally
     * filtered.
     *
     * @param filter Criteria to filter workitems on.
     * @return total done for selected workitems.
     */
    public Double getTotalDone(WorkitemFilter filter) {
        return getTotalDone(filter, false);
    }
}