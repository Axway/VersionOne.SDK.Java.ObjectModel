package com.versionone.om.tests;

import com.versionone.apiclient.FilterTerm;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Defect;
import com.versionone.om.filters.DefectFilter;

public class DefectFilterTester extends PrimaryWorkitemFilterTesterBase {
    private DefectFilter getFilter() {
        DefectFilter filter = new DefectFilter();
        filter.project.add(sandboxProject);
        return filter;
    }

    @Test
    public void foundBy() {
        DefectFilter filter = getFilter();
        filter.foundBy.add("Joe");
        Assert.assertEquals("Should be one defect FoundBy \"Joe\"", 1, getInstance().get().defects(filter).size());
    }

    @Test
    public void type() {
        DefectFilter filter = getFilter();
        filter.type.add("Code");
        Assert.assertEquals("Should be one defect of Type \"Code\"",
                1, getInstance().get().defects(filter).size());
    }

    @Test
    public void typeAndFoundBy() {
        DefectFilter filter = getFilter();
        filter.type.add("Code");
        filter.foundBy.add("Bob");
        Assert.assertEquals("Should be one defect of Type \"Code\" and FoundBy \"Bob\"",
                1, getInstance().get().defects(filter).size());

        filter = getFilter();
        filter.type.add("Documentation");
        filter.foundBy.add("Bob");
        Assert.assertEquals("Should be no defects of Type \"Documentation\" and FoundBy \"Bob\"",
                0, getInstance().get().defects(filter).size());
    }

    @Test
    public void foundInBuild() {
        DefectFilter filter = getFilter();
        filter.foundInBuild.add("1.0.0.0");
        Assert.assertEquals("Should be two defects FoundInBuild \"1.0.0.0\"",
                2, getInstance().get().defects(filter).size());
    }

    @Test
    public void environment() {
        DefectFilter filter = getFilter();
        filter.environment.add("Windows");
        Assert.assertEquals("Should be one defect with Environment of \"Windows\"",
                1, getInstance().get().defects(filter).size());
    }

    @Test
    public void resolvedInBuild() {
        DefectFilter filter = getFilter();
        filter.resolvedInBuild.add("1.0.0.2");
        filter.orderBy.add("ResolvedInBuild");
        Assert.assertEquals("Should be one defect ResolvedInBuild \"1.0.0.2\"",
                1, getInstance().get().defects(filter).size());
    }

    @Test
    public void verifiedBy() {
        DefectFilter filter = getFilter();
        filter.verifiedBy.add(null);
        filter.verifiedBy.add(danny);
        Assert.assertEquals("Should be two defects VerifiedBy Danny or no one.",
                2, getInstance().get().defects(filter).size());
    }

    @Test
    public void resolutionReason() {
        DefectFilter filter = getFilter();
        filter.resolutionReason.add("Fixed");
        Assert.assertEquals("Should be one defect of ResolutionReason \"Fixed\"",
                1, getInstance().get().defects(filter).size());
    }

    @Test
    public void estimate() {
        DefectFilter filter = getFilter();
        filter.estimate.addTerm(FilterTerm.Operator.Equal, 1.0);
        Assert.assertEquals(1, getInstance().get().defects(filter).size());

        filter.estimate.clear();
        filter.estimate.addTerm(FilterTerm.Operator.NotExists);
        Assert.assertEquals(1, getInstance().get().defects(filter).size());

        filter.estimate.clear();
        filter.estimate.addTerm(FilterTerm.Operator.Exists);
        Assert.assertEquals(2, getInstance().get().defects(filter).size());
    }
    
	@Test
	public void testNoProjectAmongDefects() {
		String sandboxName = getSandboxProject().getName();

		resetInstance();

		ListAssert.notcontains(sandboxName, getInstance().get().baseAssets(new DefectFilter()), new EntityToNameTransformer<Defect>());
	}
}
