/*(c) Copyright 2012, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.Duration;
import com.versionone.DB.DateTime;
import com.versionone.om.*;

import java.util.*;

/**
 * Entity creation factory that cleans up created objects when disposed.
 * If you create BaseAssets manually, do not forget to handle them on your own.
 */
class EntityFactory {
    interface IEntityCreator<T> {
        public T create();
    }

    private final Stack<BaseAsset> baseAssets = new Stack<BaseAsset>();
    private final Stack<Entity> entities = new Stack<Entity>();
    private final V1Instance instance;

    private final Random random = new Random();

    private V1Instance getInstance() {
        return instance;
    }

    EntityFactory(V1Instance instance) {
        this.instance = instance;
    }
    
    <T extends BaseAsset> T create(IEntityCreator<T> creator) {
        T entity =  creator.create();
        registerForDisposal(entity);

        return entity;
    }

    RegressionPlan createRegressionPlan(String name, Project project) {
        return createRegressionPlan(name, project, new HashMap<String, Object>());
    }

    RegressionPlan createRegressionPlan(String name, Project project, Map<String, Object> attributes) {
        RegressionPlan plan = getInstance().create().regressionPlan(name, project, attributes);
        registerForDisposal(plan);

        return plan;
    }

    RegressionSuite createRegressionSuite(String name, RegressionPlan regressionPlan) {
        return createRegressionSuite(name, regressionPlan, null);
    }

    RegressionSuite createRegressionSuite(String name, RegressionPlan regressionPlan, Map<String, Object> attributes) {
        RegressionSuite suite = getInstance().create().regressionSuite(name, regressionPlan, attributes);
        registerForDisposal(suite);

        return suite;
    }

    TestSet createTestSet(String name, RegressionSuite suite, Map<String, Object> attributes) {
        TestSet testSet = getInstance().create().testSet(name, suite, suite.getRegressionPlan().getProject(),
                                                         attributes);
        registerForDisposal(testSet);

        return testSet;
    }

    TestSet createTestSet(String name, RegressionSuite suite) {
        return createTestSet(name, suite, null);
    }

    Member createMember(String name) {
        String randomString = Integer.toString(random.nextInt());
        Member member = getInstance().create().member(name, randomString);
        registerForDisposal(member);

        member.save();
        return member;
    }

    RegressionTest createRegressionTest(String name, Project project) {
        return createRegressionTest(name, project, null);
    }

    RegressionTest createRegressionTest(String name, Project project, Map<String, Object> attributes) {
        RegressionTest regressionTest = getInstance().create().regressionTest(name, project, attributes);
        registerForDisposal(regressionTest);

        return regressionTest;
    }

    RegressionTest createRegressionTest(Test test) {
        RegressionTest regressionTest = getInstance().create().regressionTest(test);
        registerForDisposal(regressionTest);

        return regressionTest;
    }

    Test createTest(String name, Workitem workitem) {
        Test test = getInstance().create().test(name, workitem);
        registerForDisposal(test);

        return test;
    }

    Task createTask(String name, PrimaryWorkitem primaryWorkitem, Map<String, Object> attributes) {
        Task task = getInstance().create().task(name, primaryWorkitem, attributes);
        registerForDisposal(task);

        return task;
    }

    Story createStory(String name, Project scope) {
        Story story = getInstance().create().story(name, scope);
        registerForDisposal(story);

        return story;
    }

    Defect createDefect(String name, Project scope) {
        Defect defect = getInstance().create().defect(name, scope);
        registerForDisposal(defect);

        return defect;
    }

    Project createProject(String sandboxName, Project rootProject, Map<String, Object> mandatoryAttributes) {
        Project project = getInstance().create().project(sandboxName, rootProject, DateTime.now(), null, mandatoryAttributes);
        registerForDisposal(project);

        return project;
    }

    Project createProjectWithSchedule(String sandboxName, Project rootProject) {
        Schedule schedule = getInstance().create().schedule("Sandbox Schedule", new Duration(14, Duration.Unit.Days), new Duration(0, Duration.Unit.Days));
        registerForDisposal(schedule);
        Project project = getInstance().create().project(sandboxName, rootProject, DateTime.now(), schedule);
        registerForDisposal(project);

        return project;
    }

    Project createSubProject(String sandboxName, DateTime now, Schedule schedule, Project sandboxProject) {
        Project project = sandboxProject.createSubProject("Son of " + sandboxName, DateTime.now(), schedule);
        registerForDisposal(project);

        return project;
    }

    Epic createEpic(String name, Project sandboxProject) {
        Epic epic = sandboxProject.createEpic(name);
        registerForDisposal(epic);

        return epic;
    }

    Epic createEpic(String name, Project sandboxProject, Map<String, Object> mandatoryAttributes) {
        Epic epic = sandboxProject.createEpic(name, mandatoryAttributes);
        registerForDisposal(epic);

        return epic;
    }

    Epic createChildEpic(Epic epic) {
        Epic childEpic = epic.generateChildEpic();

        registerForDisposal(childEpic);
        return childEpic;
    }


    Environment createEnvironment(String name, Project project, Map<String, Object> attributes) {
        Environment environment = getInstance().create().environment(name, project, attributes);
        registerForDisposal(environment);
        return environment;
    }


    /**
     * Register BaseAsset for dispose it at the end.
     *
     * @param asset Item to register.
     */
    void registerForDisposal(BaseAsset asset) {
        if (!baseAssets.contains(asset)) {
            baseAssets.push(asset);
        }
    }

    /**
     * Register BaseAsset for dispose it at the end.
     *
     * @param asset Item to register.
     */
    void registerForDisposal(Entity asset) {
        if (!entities.contains(asset)) {
            entities.push(asset);
        }
    }

    /**
     * Remove BaseAssets that have been registered for disposal.
     */
    public void dispose() {
    	new EntityCleaner(baseAssets, entities, getDefaultProject()).delete();
    }

    private Project getDefaultProject() {
        Collection<Project> projects = getInstance().get().projects(null);

        return BaseSDKTester.first(projects);
    }
}