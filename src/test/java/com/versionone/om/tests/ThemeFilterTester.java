package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.Theme;
import com.versionone.om.filters.ThemeFilter;

public class ThemeFilterTester extends BaseSDKTester {

    private Project sandboxProject;
    protected Member andre;
    protected Member danny;
    protected Theme theme1;

    private ThemeFilter getFilter() {
        ThemeFilter filter = new ThemeFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    @Before
    public void createAssets() {
        if (sandboxProject == null) {
            sandboxProject = getSandboxProject();

            andre = getInstance().get().memberByID("Member:1000");
            danny = getInstance().get().memberByID("Member:1005");

            Theme theme1 = sandboxProject.createTheme("Theme 1");
            Theme theme2 = sandboxProject.createTheme("Theme 2");
            Theme theme11 = theme1.createChildTheme("Child Theme 1");

            theme1.setCustomer(andre);
            theme1.getSource().setCurrentValue("Customer");
            theme1.getRisk().setCurrentValue("Medium");
            theme1.getPriority().setCurrentValue("Medium");
            theme1.save();

            theme2.setCustomer(danny);
            theme2.getSource().setCurrentValue("Sales");
            theme2.getRisk().setCurrentValue("Low");
            theme2.getPriority().setCurrentValue("Low");
            theme2.save();

            theme11.setCustomer(andre);
            theme11.getSource().setCurrentValue("Customer");
            theme11.getRisk().setCurrentValue("Medium");
            theme11.getPriority().setCurrentValue("Medium");
            theme11.getSource().setCurrentValue("Sales");
            theme11.save();

            this.theme1 = theme1;
        }
    }

    @Test
    public void testCustomer() {
        ThemeFilter filter = getFilter();
        filter.customer.add(andre);
        Assert.assertEquals(2, getInstance().get().themes(filter).size());
    }

    @Test
    public void testRisk() {
        ThemeFilter filter = getFilter();
        filter.risk.add("Low");
        Assert.assertEquals(1, getInstance().get().themes(filter).size());
    }

    @Test
    public void testPriority() {
        ThemeFilter filter = getFilter();
        filter.priority.add("Medium");
        Assert.assertEquals(2, getInstance().get().themes(filter).size());
    }

    @Test
    public void testType() {
        ThemeFilter filter = getFilter();
        filter.type.add(null);
        Assert.assertEquals(3, getInstance().get().themes(filter).size());
    }

    @Test
    public void testSource() {
        ThemeFilter filter = getFilter();
        filter.source.add("Sales");
        Assert.assertEquals(2, getInstance().get().themes(filter).size());
    }

    @Test
    public void testParent() {
        ThemeFilter filter = getFilter();
        filter.parent.add(theme1);
        Assert.assertEquals(1, getInstance().get().themes(filter).size());
    }

}
