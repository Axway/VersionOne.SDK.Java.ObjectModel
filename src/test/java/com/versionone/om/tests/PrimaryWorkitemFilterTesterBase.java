package com.versionone.om.tests;

import org.junit.Before;

import com.versionone.om.Defect;
import com.versionone.om.Epic;
import com.versionone.om.Member;
import com.versionone.om.Project;
import com.versionone.om.Story;

public class PrimaryWorkitemFilterTesterBase extends BaseSDKTester {
    static final String REFERENCE = "123456";
    protected Project sandboxProject;
    protected Story story1;
    private Story story2;
    private Story story3;
    private Epic epic1;
    private Epic epic2;
    protected Defect defect1;
    private Defect defect2;
    private Defect defect3;
    protected Member andre;
    protected Member danny;

    @Before
    public void createAssets() {
        if (sandboxProject == null) {
            sandboxProject = getSandboxProject();
            andre = getInstance().get().memberByID("Member:1000");
            danny = getInstance().get().memberByID("Member:1005");
            epic1 = getInstance().create().epic("Epic 1", getSandboxProject());
            epic2 = epic1.generateChildEpic();
            epic2.setName("Epic 2");
            epic2.save();
            story1 = getSandboxProject().createStory("Story 1");
            story2 = getSandboxProject().createStory("Story 2");
            story3 = getSandboxProject().createStory("Story 3");
            defect1 = getSandboxProject().createDefect("Defect 1");
            defect2 = getSandboxProject().createDefect("Defect 2");
            defect3 = getSandboxProject().createDefect("Defect 3");

            story1.setDescription("ABCDEFGHIJKJMNOPQRSTUVWXYZ");
            story1.save();

            story2.setDescription("1234567890");
            story2.save();

            story3.setDescription("123 ABC");
            story3.save();

            story1.getOwners().add(andre);
            story1.getOwners().add(danny);
            story3.getOwners().add(andre);

            defect2.getOwners().add(andre);
            defect3.getOwners().add(danny);

            defect1.setFoundInBuild("1.0.0.0");
            defect1.setResolvedInBuild("1.0.0.2");
            defect1.setEnvironment("Windows");
            defect1.setEstimate(2.0);
            defect1.setReference(REFERENCE);
            defect1.save();

            defect2.getAffectedByDefects().add(defect1);
            defect2.setFoundInBuild("1.0.0.0");
            defect2.setFoundBy("Joe");
            defect2.setVerifiedBy(andre);
            defect2.setEnvironment("Mac");
            defect2.getType().setCurrentValue("Documentation");
            defect2.getResolutionReason().setCurrentValue("Duplicate");
            defect2.setEstimate(1.0);
            defect2.save();

            defect3.setFoundInBuild("1.0.0.1");
            defect3.setFoundBy("Bob");
            defect3.setVerifiedBy(danny);
            defect3.getType().setCurrentValue("Code");
            defect3.getResolutionReason().setCurrentValue("Fixed");
            defect3.save();
        }
    }
}
