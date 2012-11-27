/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.apiclient.*;
import com.versionone.om.filters.*;
import com.versionone.om.listvalue.ListValue;

import java.util.Collection;

/**
 * Methods to get things
 */
public class V1InstanceGetter {
    private final V1Instance instance;

    V1InstanceGetter(V1Instance instance) {
        this.instance = instance;
    }

    private <T extends Entity> IAssetType resolveAssetType(Class<T> type) {
        return instance.getMetaModel().getAssetType(
                V1Instance.getAssetTypeToken(type));
    }

    <T extends Entity> Collection<T> get(Class<T> clazz, EntityFilter filter) {
        // The returned entity type is determined by
        // 1) the filter passed in or 2) the type of T if there is no filter.
        Class<T> targetEntityType = (filter == null) ? clazz : (Class<T>) filter
                .getEntityType();
        IAssetType type = resolveAssetType(targetEntityType);
        Query query = new Query(type);

        if (filter != null) {
            String defaultToken = V1Instance
                    .getDefaultOrderByToken(targetEntityType);
            IAttributeDefinition defaultOrderBy = null;

            if (!defaultToken.equals("")) {
                defaultOrderBy = instance.getMetaModel()
                        .getAttributeDefinition(defaultToken);
            }

            query.setFilter(filter.buildFilter(type, instance));
            query.setFind(filter.buildFind(type));
            query.setOrderBy(filter.buildOrderBy(type, defaultOrderBy));
        }
        return instance.queryToEntityEnum(clazz, query);
    }

    /**
     * Get attachments filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Attachment> attachments(AttachmentFilter filter) {
        return get(Attachment.class, (filter != null) ? filter : new AttachmentFilter());
    }

    /**
     * Get notes filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Note> notes(NoteFilter filter) {
        return get(Note.class, (filter != null) ? filter : new NoteFilter());
    }

    /**
     * Get links filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Link> links(LinkFilter filter) {
        return get(Link.class, (filter != null) ? filter : new LinkFilter());
    }

    /**
     * Get effort records filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Effort> effortRecords(EffortFilter filter) {
        return get(Effort.class, (filter != null) ? filter : new EffortFilter());
    }

    /**
     * Get assets filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<BaseAsset> baseAssets(BaseAssetFilter filter) {
        return get(BaseAsset.class, (filter != null) ? filter : new BaseAssetFilter());
    }

    /**
     * Get stories filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Story> story(StoryFilter filter) {
        return get(Story.class, (filter != null) ? filter : new StoryFilter());
    }

    /**
     * Get Epics filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Epic> epics(EpicFilter filter) {
        return get(Epic.class, (filter != null) ? filter : new EpicFilter());
    }

    /**
     * Get tracked Epics for enlisted Projects.
     */
    public Collection<Epic> trackedEpics(Collection<Project> projects) {
        return get(Epic.class, new TrackedEpicFilter(projects));
    }

    /**
     * Get defects filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Defect> defects(DefectFilter filter) {
        return get(Defect.class, (filter != null) ? filter : new DefectFilter());
    }

    /**
     * Get primary workitems (stories and defects) filtered by the criteria
     * specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<PrimaryWorkitem> primaryWorkitems(
            PrimaryWorkitemFilter filter) {
        return get(PrimaryWorkitem.class, (filter != null) ? filter : new PrimaryWorkitemFilter());
    }

    /**
     * Get workitems (stories, defects, tasks, and tests) filtered by the
     * criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Workitem> workitems(WorkitemFilter filter) {
        return get(Workitem.class, (filter != null) ? filter : new WorkitemFilter());
    }

    /**
     * Get secondary workitems (tasks and tests) filtered by the criteria
     * specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<SecondaryWorkitem> secondaryWorkitems(
            SecondaryWorkitemFilter filter) {
        return get(SecondaryWorkitem.class, (filter != null) ? filter : new SecondaryWorkitemFilter());
    }

    /**
     * Get tasks filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Task> tasks(TaskFilter filter) {
        return get(Task.class, (filter != null) ? filter : new TaskFilter());
    }

    /**
     * Get Tests filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Test> tests(TestFilter filter) {
        return get(Test.class, (filter != null) ? filter : new TestFilter());
    }

    /**
     * Get iterations filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Iteration> iterations(IterationFilter filter) {
        return get(Iteration.class, (filter != null) ? filter : new IterationFilter());
    }

    /**
     * Get projects filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Project> projects(ProjectFilter filter) {
        return get(Project.class, (filter != null) ? filter : new ProjectFilter());
    }

    /**
     * Get teams filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Team> teams(TeamFilter filter) {
        return get(Team.class, (filter != null) ? filter : new TeamFilter());
    }

    /**
     * Get themes filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Theme> themes(ThemeFilter filter) {
        return get(Theme.class, (filter != null) ? filter : new ThemeFilter());
    }

    /**
     * Get Members filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Member> members(MemberFilter filter) {
        return get(Member.class, (filter != null) ? filter : new MemberFilter());
    }

    /**
     * Get Requests filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Request> requests(RequestFilter filter) {
        return get(Request.class, (filter != null) ? filter : new RequestFilter());
    }

    /**
     * Get Goals filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Goal> goals(GoalFilter filter) {
        return get(Goal.class, (filter != null) ? filter : new GoalFilter());
    }

    /**
     * Get Issues filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Issue> issues(IssueFilter filter) {
        return get(Issue.class, (filter != null) ? filter : new IssueFilter());
    }

    /**
     * Get Retrospective filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Retrospective> retrospectives(
            RetrospectiveFilter filter) {
        return get(Retrospective.class, (filter != null) ? filter : new RetrospectiveFilter());
    }

    /**
     * Get Build Runs filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<BuildRun> buildRuns(BuildRunFilter filter) {
        return get(BuildRun.class, (filter != null) ? filter : new BuildRunFilter());
    }

    /**
     * Get Build Projects filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<BuildProject> buildProjects(BuildProjectFilter filter) {
        return get(BuildProject.class, (filter != null) ? filter : new BuildProjectFilter());
    }

    /**
     * Get ChangeSets filtered by the criteria specified in the passed in
     * filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<ChangeSet> changeSets(ChangeSetFilter filter) {
        return get(ChangeSet.class, (filter != null) ? filter : new ChangeSetFilter());
    }

    /**
     * Get schedules filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return ICollection of items as specified in the filter.
     */
    public Collection<Schedule> schedules(ScheduleFilter filter) {
        return get(Schedule.class, (filter != null) ? filter : new ScheduleFilter());
    }

    /**
     * Get Messages filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Message> messages(MessageFilter filter) {
        return get(Message.class, (filter != null) ? filter : new MessageFilter());
    }

    /**
     * Get Conversation/Expression filtered by the criteria specified in the passed filter.
     *
     * @param filter Limit the items returned. If null, then all items returned.
     * @return ICollection of the items as specified in the filter.
     */
    public Collection<Conversation> conversations(ConversationFilter filter) {
        return get(Conversation.class, (filter != null) ? filter : new ConversationFilter());
    }

    Collection<MessageReceipt> messageReceipts(MessageReceiptFilter filter) {
        return get(MessageReceipt.class, (filter != null) ? filter : new MessageReceiptFilter());
    }

    /**
     * Gets regression plans filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items are returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<RegressionPlan> regressionPlans(RegressionPlanFilter filter) {
        return get(RegressionPlan.class, (filter != null) ? filter : new RegressionPlanFilter());
    }

    /**
     * Get Regression Suite filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items are returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<RegressionSuite> regressionSuites(RegressionSuiteFilter filter) {
        return get(RegressionSuite.class, (filter != null) ? filter : new RegressionSuiteFilter());
    }

    /**
     * Get Test Sets filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items are returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<TestSet> testSets(TestSetFilter filter) {
        return get(TestSet.class, (filter != null) ? filter : new TestSetFilter());
    }

    /**
     * Get Environment filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items are returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<Environment> environments(EnvironmentFilter filter) {
        return get(Environment.class, (filter != null) ? filter : new EnvironmentFilter());
    }

    /**
     * Get Regression Suite filtered by the criteria specified in the passed in filter.
     *
     * @param filter Limit the items returned. If null, then all items are returned.
     * @return Collection of items as specified in the filter.
     */
    public Collection<RegressionTest> regressionTests(RegressionTestFilter filter) {
        return get(RegressionTest.class, (filter != null) ? filter : new RegressionTestFilter());
    }

    /**
     * id Returns a project with the given ID or null if the ID is invalid.
     *
     * @param id id of the project to retrieve.
     * @return an instance of a Project or null if ID is invalid.
     */
    public Project projectByID(AssetID id) {
        return byID(Project.class, id);
    }

    /**
     * id Returns a project with the given ID or null if the ID is invalid.
     *
     * @param sId id of the project to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a Project or null if ID is invalid.
     * @see AssetID#AssetID(String).
     */
    public Project projectByID(String sId) {
        return projectByID(AssetID.valueOf(sId));
    }

    /**
     * Returns an iteration with the given ID or null if the ID is invalid.
     *
     * @param id ID of the iteration to retrieve.
     * @return an instance of an Iteration or null if ID is invalid.
     */
    public Iteration iterationByID(AssetID id) {
        return byID(Iteration.class, id);
    }

    /**
     * Returns an iteration with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the iteration to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of an Iteration or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public Iteration iterationByID(String sId) {
        return iterationByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a retrospective with the given ID or null if the ID is invalid.
     *
     * @param id ID of the iteration to retrieve.
     * @return an instance of a retrospective or null if ID is invalid.
     */
    public Retrospective retrospectiveByID(AssetID id) {
        return byID(Retrospective.class, id);
    }

    /**
     * Returns a retrospective with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the iteration to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a retrospective or null if ID is invalid.
     */
    public Retrospective retrospectiveByID(String sId) {
        return retrospectiveByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Member with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Member to retrieve.
     * @return an instance of a Member or null if ID is invalid.
     */
    public Member memberByID(AssetID id) {
        return byID(Member.class, id);
    }

    /**
     * Returns a Member with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Member to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a Member or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public Member memberByID(String sId) {
        return memberByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Team with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Team to retrieve.
     * @return an instance of a Team or null if ID is invalid.
     */
    public Team teamByID(AssetID id) {
        return byID(Team.class, id);
    }

    /**
     * Returns a Team with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Team to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a Team or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public Team teamByID(String sId) {
        return teamByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Story with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Story to retrieve.
     * @return an instance of a story or null if ID is invalid.
     */
    public Story storyByID(AssetID id) {
        return byID(Story.class, id);
    }

    /**
     * Returns a Story with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Story to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a story or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public Story storyByID(String sId) {
        return storyByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Defect with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Defect to retrieve.
     * @return an instance of a Defect or null if ID is invalid.
     */
    public Defect defectByID(AssetID id) {
        return byID(Defect.class, id);
    }

    /**
     * Returns a Defect with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Defect to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a Defect or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public Defect defectByID(String sId) {
        return defectByID(AssetID.valueOf(sId));
    }

    /**
     * Returns an Issue with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Issue to retrieve.
     * @return an instance of a Issue or null if ID is invalid.
     */
    public Issue issueByID(AssetID id) {
        return byID(Issue.class, id);
    }

    /**
     * Returns a Request with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Request to retrieve.
     * @return an instance of a Request or null if ID is invalid.
     */
    public Request requestByID(AssetID id) {
        return byID(Request.class, id);
    }

    /**
     * Returns a Theme with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Theme to retrieve.
     * @return an instance of a Theme or null if ID is invalid.
     */
    public Theme themeByID(AssetID id) {
        return byID(Theme.class, id);
    }

    /**
     * Returns a Theme with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Theme to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a Theme or null if ID is invalid.
     */
    public Theme themeByID(String sId) {
        return themeByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Goal with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Goal to retrieve.
     * @return an instance of a Goal or null if ID is invalid.
     */
    public Goal goalByID(AssetID id) {
        return byID(Goal.class, id);
    }

    /**
     * Returns a Goal with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Goal to retrieve.
     * @return an instance of a Goal or null if ID is invalid.
     */
    public Goal goalByID(String sId) {
        return goalByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Epic with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Epic to retrieve(string have to be token for AssetId
     *           class).
     * @return an instance of a Epic or null if ID is invalid.
     */
    public Epic epicByID(AssetID id) {
        return byID(Epic.class, id);
    }

    /**
     * Returns a Epic with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Epic to retrieve(string have to be token for AssetId
     *            class).
     * @return an instance of a Epic or null if ID is invalid.
     */
    public Epic epicByID(String sId) {
        return epicByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a StoryTemplate with the given ID or null if the ID is invalid.
     *
     * @param id ID of the StoryTemplate to retrieve.
     * @return an instance of a StoryTemplate or null if ID is invalid.
     */
    public StoryTemplate storyTemplateByID(AssetID id) {
        return byID(StoryTemplate.class, id);
    }

    /**
     * Returns a StoryTemplate with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the StoryTemplate to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a StoryTemplate or null if ID is invalid.
     */
    public StoryTemplate storyTemplateByID(String sId) {
        return storyTemplateByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a DefectTemplate with the given ID or null if the ID is invalid.
     *
     * @param id ID of the DefectTemplate to retrieve.
     * @return an instance of a DefectTemplate or null if ID is invalid.
     */
    public DefectTemplate defectTemplateByID(AssetID id) {
        return byID(DefectTemplate.class, id);
    }

    /**
     * Returns a DefectTemplate with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the DefectTemplate to retrieve(string have to be token
     *            for AssetId class).
     * @return an instance of a DefectTemplate or null if ID is invalid.
     */
    public DefectTemplate defectTemplateByID(String sId) {
        return defectTemplateByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Note with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Note to retrieve.
     * @return an instance of a Note or null if ID is invalid.
     */
    public Note noteByID(AssetID id) {
        return byID(Note.class, id);
    }

    /**
     * Returns a Note with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Note to retrieve(string have to be token for AssetId
     *            class).
     * @return an instance of a Note or null if ID is invalid.
     * @see AssetID#AssetID(String).
     */
    public Note noteByID(String sId) {
        return noteByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Link with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Link to retrieve.
     * @return an instance of a Link or null if ID is invalid.
     */
    public Link linkByID(AssetID id) {
        return byID(Link.class, id);
    }

    /**
     * Returns a Link with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Link to retrieve(string have to be token for AssetId
     *            class).
     * @return an instance of a Link or null if ID is invalid.
     * @see AssetID#AssetID(String).
     */
    public Link linkByID(String sId) {
        return linkByID(AssetID.valueOf(sId));
    }

    /**
     * Returns an Attachment with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Attachment to retrieve.
     * @return an instance of an Attachment or null if ID is invalid.
     */
    public Attachment attachmentByID(AssetID id) {
        return byID(Attachment.class, id);
    }

    /**
     * Returns an Attachment with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Attachment to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of an Attachment or null if ID is invalid.
     */
    public Attachment attachmentByID(String sId) {
        return attachmentByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a TestSuite with the given ID or null if the ID is invalid.
     *
     * @param id ID of the TestSuite to retrieve.
     * @return an instance of a TestSuite or null if ID is invalid.
     */
    public TestSuite testSuiteByID(AssetID id) {
        return byID(TestSuite.class, id);
    }

    /**
     * Returns a TestSuite with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the TestSuite to retrieve (string have to be token for
     *            AssetId class).
     * @return an instance of a TestSuite or null if ID is invalid.
     */
    public TestSuite testSuiteByID(String sId) {
        return testSuiteByID(AssetID.valueOf(sId));
    }

    /**
     * Returns an Effort record with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Effort record to retrieve.
     * @return an instance of an Effort record or null if ID is invalid.
     */
    public Effort effortByID(AssetID id) {
        return byID(Effort.class, id);
    }

    /**
     * Returns an Effort record with the given ID or null if the ID is invalid.
     *
     * @param assetId ID of the Effort record to retrieve. (string have to be
     *                token for AssetId class).
     * @return an instance of an Effort record or null if ID is invalid.
     */
    public Effort effortByID(String assetId) {
        return effortByID(AssetID.valueOf(assetId));
    }

    /**
     * Returns a Primary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param id ID of the Primary Workitem to retrieve.
     * @return an instance of a Primary Workitem or null if ID is invalid.
     */
    public PrimaryWorkitem primaryWorkitemByID(AssetID id) {
        return byID(PrimaryWorkitem.class, id);
    }

    /**
     * Returns a Primary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param sId ID of the Primary Workitem to retrieve(string have to be token
     *            for AssetId class).
     * @return an instance of a Primary Workitem or null if ID is invalid.
     */
    public PrimaryWorkitem primaryWorkitemByID(String sId) {
        return primaryWorkitemByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Secondary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param id ID of the Secondary Workitem to retrieve.
     * @return an instance of a Secondary Workitem or null if ID is invalid.
     */
    public SecondaryWorkitem secondaryWorkitemByID(AssetID id) {
        return byID(SecondaryWorkitem.class, id);
    }

    /**
     * Returns a Secondary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param sId ID of the Secondary Workitem to retrieve(string have to be
     *            token for AssetId class).
     * @return an instance of a Secondary Workitem or null if ID is invalid.
     */
    public SecondaryWorkitem secondaryWorkitemByID(String sId) {
        return secondaryWorkitemByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Workitem with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Workitem to retrieve.
     * @return an instance of a Workitem or null if ID is invalid.
     */
    public Workitem workitemByID(AssetID id) {
        return byID(Workitem.class, id);
    }

    /**
     * Returns a Workitem with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Workitem to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a Workitem or null if ID is invalid.
     */
    public Workitem workitemByID(String sId) {
        return workitemByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a BaseAsset with the given ID or null if the ID is invalid.
     *
     * @param id ID of the BaseAsset to retrieve.
     * @return an instance of a BaseAsset or null if ID is invalid.
     */
    public BaseAsset baseAssetByID(AssetID id) {
        return byID(BaseAsset.class, id);
    }

    /**
     * Returns a BaseAsset with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the BaseAsset to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a BaseAsset or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public BaseAsset baseAssetByID(String sId) {
        return baseAssetByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Build Run with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Build Run to retrieve.
     * @return an instance of a Build Run or null if ID is invalid.
     */
    public BuildRun buildRunByID(AssetID id) {
        return byID(BuildRun.class, id);
    }

    /**
     * Returns a Build Run with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Build Run to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a Build Run or null if ID is invalid
     * @see AssetID#AssetID(String).
     */
    public BuildRun buildRunByID(String sId) {
        return buildRunByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Build Project with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Build Project to retrieve.
     * @return an instance of a Build Project or null if ID is invalid.
     */
    public BuildProject buildProjectByID(AssetID id) {
        return byID(BuildProject.class, id);
    }

    /**
     * Returns a Build Project with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the Build Project to retrieve(string have to be token
     *            for AssetId class).
     * @return an instance of a Build Project or null if ID is invalid.
     * @see AssetID#AssetID(String).
     */
    public BuildProject buildProjectByID(String sId) {
        return buildProjectByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a ChangeSet with the given ID or null if the ID is invalid.
     *
     * @param id ID of the ChangeSet to retrieve.
     * @return an instance of a ChangeSet or null if ID is invalid.
     */
    public ChangeSet changeSetByID(AssetID id) {
        return byID(ChangeSet.class, id);
    }

    /**
     * Returns a ChangeSet with the given ID or null if the ID is invalid.
     *
     * @param sId ID of the ChangeSet to retrieve(string have to be token for
     *            AssetId class).
     * @return an instance of a ChangeSet or null if ID is invalid.
     * @see AssetID#AssetID(String).
     */
    public ChangeSet changeSetByID(String sId) {
        return changeSetByID(AssetID.valueOf(sId));
    }

    /**
     * Returns a Conversation with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Conversation to retrieve.
     * @return an instance of an Conversation or null if ID is invalid.
     */
    public Conversation conversationByID(AssetID id) {
        return byID(Conversation.class, id);
    }

    /**
     * Returns an Entity of Type T with the given ID or null if the ID is
     * invalid.
     *
     * @param <T>   Entity Type to retrieve.
     * @param clazz - T Class.
     * @param id    ID of the Entity to retrieve.
     * @return an instance of an Entity of Type T or null if ID is invalid.
     */
    public <T extends Entity> T byID(Class<T> clazz, AssetID id) {
        return instance.getWrapperManager().create(clazz, id, true);
    }

    /**
     * Returns an Entity of Type T with the given ID or null if the ID is
     * invalid.
     *
     * @param clazz     Entity Type to retrieve.
     * @param displayID DisplayID of the Entity to retrieve.
     * @return an instance of an Entity of Type T or null if ID is invalid.
     * @throws SDKException if there is a problem executing the query
     */
    public <T extends ProjectAsset> T byDisplayID(Class<T> clazz,
                                                  String displayID) throws SDKException {
        String assetTypeToken = V1Instance.getAssetTypeToken(clazz);
        IAssetType projectAssetType = instance.getMetaModel().getAssetType(
                assetTypeToken);
        IAttributeDefinition idDef = projectAssetType
                .getAttributeDefinition("Number");

        Query query = new Query(projectAssetType);
        FilterTerm idTerm = new FilterTerm(idDef);
        idTerm.equal(displayID);
        query.setFilter(idTerm);

        QueryResult result;
        try {
            result = instance.getServices().retrieve(query);
        } catch (V1Exception e) {
            throw new SDKException(e);
        }

        if (result.getAssets().length == 0) {
            return null;
        }
        Asset asset = result.getAssets()[0];

        return instance.getWrapperManager().create(clazz,
                                                   AssetID.valueOf(asset.getOid().getToken()), false);
    }

    /**
     * Returns a Story with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Story to retrieve.
     * @return an instance of a story or null if ID is invalid.
     */
    public Story storyByDisplayID(String displayID) {
        return byDisplayID(Story.class, displayID);
    }

    /**
     * Returns a Defect with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Defect to retrieve.
     * @return an instance of a Defect or null if ID is invalid.
     */
    public Defect defectByDisplayID(String displayID) {
        return byDisplayID(Defect.class, displayID);
    }

    /**
     * Returns an Issue with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Issue to retrieve.
     * @return an instance of a Issue or null if ID is invalid.
     */
    public Issue issueByDisplayID(String displayID) {
        return byDisplayID(Issue.class, displayID);
    }

    /**
     * Returns a Request with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Request to retrieve.
     * @return an instance of a Request or null if ID is invalid.
     */
    public Request requestByDisplayID(String displayID) {
        return byDisplayID(Request.class, displayID);
    }

    /**
     * Returns a Theme with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Theme to retrieve.
     * @return an instance of a Theme or null if ID is invalid.
     */
    public Theme themeByDisplayID(String displayID) {
        return byDisplayID(Theme.class, displayID);
    }

    /**
     * Returns a Goal with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Goal to retrieve.
     * @return an instance of a Goal or null if ID is invalid.
     */
    public Goal goalByDisplayID(String displayID) {
        return byDisplayID(Goal.class, displayID);
    }

    /**
     * Returns a Epic with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Epic to retrieve.
     * @return an instance of a Epic or null if ID is invalid.
     */
    public Epic epicByDisplayID(String displayID) {
        return byDisplayID(Epic.class, displayID);
    }

    /**
     * Returns a Primary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param displayID DisplayID of the Primary Workitem to retrieve.
     * @return an instance of a Primary Workitem or null if ID is invalid.
     */
    public PrimaryWorkitem primaryWorkitemByDisplayID(String displayID) {
        return byDisplayID(PrimaryWorkitem.class, displayID);
    }

    /**
     * Returns a Secondary Workitem with the given ID or null if the ID is
     * invalid.
     *
     * @param displayID DisplayID of the Secondary Workitem to retrieve.
     * @return an instance of a Secondary Workitem or null if ID is invalid.
     */
    public SecondaryWorkitem secondaryWorkitemByDisplayID(String displayID) {
        return byDisplayID(SecondaryWorkitem.class, displayID);
    }

    /**
     * Returns a Workitem with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the Workitem to retrieve.
     * @return an instance of a Workitem or null if ID is invalid.
     */
    public Workitem workitemByDisplayID(String displayID) {
        return byDisplayID(Workitem.class, displayID);
    }

    /**
     * Returns a RegressionPlan with the given ID or null if the ID is invalid.
     *
     * @param id ID of the RegressionPlan to retrieve.
     * @return an instance of a RegressionPlan or null if ID is invalid.
     */
    public RegressionPlan regressionPlanByID(AssetID id) {
        return byID(RegressionPlan.class, id);
    }

    /**
     * Returns a RegressionPlan with the given ID or null if the ID is invalid
     *
     * @param displayID DisplayID of the RegressionPlan to retrieve
     * @return an instance of a RegressionPlan or null if ID is invalid
     */
    public RegressionPlan regressionPlanByDisplayID(String displayID) {
        return byDisplayID(RegressionPlan.class, displayID);
    }

    /**
     * Returns a Regression Suite with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Regression Suite to retrieve.
     * @return an instance of a Regression Suite or null if ID is invalid.
     */
    public RegressionSuite regressionSuiteByID(AssetID id) {
        return byID(RegressionSuite.class, id);
    }

    /**
     * Returns a Test Set with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Test Set to retrieve.
     * @return an instance of a Test Set or null if ID is invalid.
     */
    public TestSet testSetByID(AssetID id) {
        return byID(TestSet.class, id);
    }

    /**
     * Returns a TestSet with the given ID or null if the ID is invalid.
     *
     * @param displayID DisplayID of the TestSet to retrieve.
     * @return an instance of a TestSet or null if ID is invalid.
     */
    public TestSet testSetByDisplayID(String displayID) {
        return byDisplayID(TestSet.class, displayID);
    }

    /**
     * Returns a Environment with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Environment to retrieve.
     * @return an instance of a Environment or null if ID is invalid.
     */
    public Environment environmentByID(AssetID id) {
        return byID(Environment.class, id);
    }

    /**
     * Returns a Regression Test with the given ID or null if the ID is invalid.
     *
     * @param id ID of the Regression Test to retrieve.
     * @return an instance of a Regression Test or null if ID is invalid.
     */
    public RegressionTest regressionTestByID(AssetID id) {
        return byID(RegressionTest.class, id);
    }

    /**
     * Retrieves the first project with the given name or null.
     *
     * @param name name of the project to retrieve.
     * @return the first instance of a Project that matches name or null.
     * @throws SDKException if there is a problem executing the query
     */
    public Project projectByName(String name) throws SDKException {
        IAssetType projectAssetType = instance.getMetaModel().getAssetType(
                "Scope");
        IAttributeDefinition nameDef = projectAssetType
                .getAttributeDefinition("Name");
        Query query = new Query(projectAssetType);
        FilterTerm nameTerm = new FilterTerm(nameDef);

        nameTerm.equal(name);
        query.setFilter(nameTerm);
        query.getOrderBy().majorSort(nameDef, OrderBy.Order.Ascending);

        QueryResult result;
        try {
            result = instance.getServices().retrieve(query);
        } catch (V1Exception e) {
            throw new SDKException(e);
        }

        if (result.getAssets().length == 0) {
            return null;
        }
        Asset asset = result.getAssets()[0];
        return new Project(new AssetID(asset.getOid().getToken()), instance);
    }

    /**
     * Retrieves the first Member with the given user name.
     *
     * @param userName The user name the user or member uses to login to the
     *                 VersionOne system.
     * @return The first Member with the given user name, or null if none found.
     * @throws SDKException if there is a problem executing the query
     */
    public Member memberByUserName(String userName) throws SDKException {
        IAssetType memberAssetType = instance.getMetaModel().getAssetType(
                "Member");
        IAttributeDefinition nameDef = memberAssetType
                .getAttributeDefinition("Username");
        Query query = new Query(memberAssetType);
        FilterTerm usernameTerm = new FilterTerm(nameDef);

        usernameTerm.equal(userName);
        query.setFilter(usernameTerm);
        query.getOrderBy().majorSort(nameDef, OrderBy.Order.Ascending);

        QueryResult result;
        try {
            result = instance.getServices().retrieve(query);
        } catch (V1Exception e) {
            throw new SDKException(e);
        }

        if (result.getAssets().length == 0) {
            return null;
        }
        Asset asset = result.getAssets()[0];
        return new Member(new AssetID(asset.getOid().getToken()), instance);
    }

    /**
     * Gets the active values of a standard list type.
     *
     * @param <T>         The type of Entity that represents the V1 List Type.
     * @param valuesClass T Class.
     * @return A list of active values for this list type.
     */
    public <T extends ListValue> Collection<T> listTypeValues(
            Class<T> valuesClass) {
        IAssetType typeToGet = instance.getMetaModel().getAssetType(
                V1Instance.getAssetTypeToken(valuesClass));
        Query query = new Query(typeToGet);
        FilterTerm assetStateTerm = new FilterTerm(typeToGet
                .getAttributeDefinition("AssetState"));

        assetStateTerm.notEqual(AssetState.Closed);
        query.setFilter(new AndFilterTerm(
                new IFilterTerm[]{assetStateTerm}));
        return instance.queryToEntityEnum(valuesClass, query);
    }

    /**
     * Returns a schedule with the given ID or null if the ID is invalid
     *
     * @param id ID of the schedule to retrieve
     * @return an instance of a Schedule or null if ID is invalid
     */
    public Schedule scheduleByID(AssetID id) {
        return byID(Schedule.class, id);
    }

    /**
     * Retrieves the first schedule with the given name or null
     *
     * @param name name of the schedule to retrieve
     * @return the first instance of a Schedule that matches name or null
     */
    public Schedule scheduleByName(String name) {
        IAssetType scheduleAssetType = instance.getMetaModel().getAssetType("Schedule");
        IAttributeDefinition nameDef = scheduleAssetType.getAttributeDefinition("Name");

        Query query = new Query(scheduleAssetType);
        FilterTerm nameTerm = new FilterTerm(nameDef);
        nameTerm.equal(name);
        query.setFilter(nameTerm);
        query.getOrderBy().majorSort(nameDef, OrderBy.Order.Ascending);

        QueryResult result;
        try {
            result = instance.getServices().retrieve(query);
        } catch (Exception e) {
            throw new SDKException(e);
        }
        if (result.getAssets().length == 0)
            return null;
        Asset asset = result.getAssets()[0];
        return new Schedule(new AssetID(asset.getOid().getToken()), instance);
    }
}
