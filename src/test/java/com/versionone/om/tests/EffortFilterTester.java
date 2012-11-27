package com.versionone.om.tests;

import com.versionone.DB;
import com.versionone.apiclient.FilterTerm;
import com.versionone.om.Effort;
import com.versionone.om.Story;
import com.versionone.om.Task;
import com.versionone.om.filters.EffortFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class EffortFilterTester extends BaseSDKTester {
    @Test
    public void date() {
        Story story = getEntityFactory().createStory("Story1", getSandboxProject());
        Task task = getEntityFactory().createTask("Task 1", story, new HashMap<String, Object>());

        Effort effort1 = task.createEffort(1d);
        DB.DateTime effort1Date = effort1.getDate();
        DB.DateTime effort2Date = effort1Date.add(GregorianCalendar.DAY_OF_YEAR, 1);
        Effort effort2 = task.createEffort(2d);
        effort2.setDate(effort2Date);
        effort2.save();
        resetInstance();

        EffortFilter filter = new EffortFilter();
        filter.workitem.add(task);
        filter.date.addTerm(FilterTerm.Operator.GreaterThan, effort1Date);
        Collection<Effort> efforts = getInstance().get().effortRecords(filter);
        ListAssert.notcontains(effort1, efforts);
        ListAssert.contains(effort2, efforts);

        filter.date.clear();
        filter.date.addTerm(FilterTerm.Operator.LessThan, effort2Date);
        efforts = getInstance().get().effortRecords(filter);
        ListAssert.contains(effort1, efforts);
        ListAssert.notcontains(effort2, efforts);

        filter.date.clear();
        filter.date.addTerm(FilterTerm.Operator.LessThanOrEqual, effort1Date);
        efforts = getInstance().get().effortRecords(filter);
        ListAssert.contains(effort1, efforts);
        ListAssert.notcontains(effort2, efforts);

        filter.date.clear();
        filter.date.addTerm(FilterTerm.Operator.GreaterThanOrEqual, effort2Date);
        efforts = getInstance().get().effortRecords(filter);
        ListAssert.notcontains(effort1, efforts);
        ListAssert.contains(effort2, efforts);
    }

    @Test
    public void value() {
        Story story = getEntityFactory().createStory("Story1", getSandboxProject());
        Task task = getEntityFactory().createTask("Task 1", story, new HashMap<String, Object>());

        Effort effort1 = task.createEffort(1);
        Effort effort2 = task.createEffort(2);
        Effort effort3 = task.createEffort(3);

        resetInstance();

        EffortFilter filter = new EffortFilter();
        filter.workitem.add(task);
        filter.value.addTerm(FilterTerm.Operator.Equal, 2.0);
        Collection<Effort> efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(1, efforts.size());
        ListAssert.notcontains(effort1, efforts);
        ListAssert.notcontains(effort3, efforts);
        ListAssert.contains(effort2, efforts);

        filter.value.clear();
        filter.value.addTerm(FilterTerm.Operator.LessThan, 2.5);
        efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(2, efforts.size());
        ListAssert.contains(effort1, efforts);
        ListAssert.contains(effort2, efforts);
        ListAssert.notcontains(effort3, efforts);

        filter.value.clear();
        filter.value.addTerm(FilterTerm.Operator.GreaterThan, 2.5);
        efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(1, efforts.size());
        ListAssert.contains(effort3, efforts);
        ListAssert.notcontains(effort1, efforts);
        ListAssert.notcontains(effort2, efforts);
    }

    @Test
    public void valueRange() {
        Story story = getEntityFactory().createStory("Story1", getSandboxProject());
        Task task = getEntityFactory().createTask("Task 1", story, new HashMap<String, Object>());

        Effort effort1 = task.createEffort(1);
        Effort effort2 = task.createEffort(2);
        Effort effort3 = task.createEffort(3);

        resetInstance();

        EffortFilter filter = new EffortFilter();
        filter.workitem.add(task);
        filter.value.range(0.5, 3.5);
        Collection<Effort> efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(3, efforts.size());
        ListAssert.contains(effort1, efforts);
        ListAssert.contains(effort2, efforts);
        ListAssert.contains(effort3, efforts);

        filter.value.clear();
        filter.value.range(1.5, 3.5);
        efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(2, efforts.size());
        ListAssert.notcontains(effort1, efforts);
        ListAssert.contains(effort2, efforts);
        ListAssert.contains(effort3, efforts);

        filter.value.range(3.5, 4.5);
        efforts = getInstance().get().effortRecords(filter);
        Assert.assertEquals(0, efforts.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueRangeInvalidBounds() {
        EffortFilter filter = new EffortFilter();
        filter.value.range(10.0, 0.0);
    }
}