/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefectTester extends BaseSDKTester {

    private static final String defectName = "Defect #1 Created in Unit Test";
    private static final String defectDescription = "the description";
    private static final String defectFoundBy = "Yogi Bear";
    private static final double defectEstimate = 4.4;

    @Test
    public void getDefectByID() {
        Defect defect = getInstance().get().defectByID("Defect:1406");

        Assert.assertEquals("Pick Lists Reversed", defect.getName());
    }

    @Test
    public void createDefect() {
        AssetID id = createBasicDefect(getSandboxProject()).getID();
        resetInstance();

        Defect defect = getInstance().get().defectByID(id);

        Assert.assertEquals(defectName, defect.getName());
        Assert.assertEquals(defectDescription, defect.getDescription());
        Assert.assertEquals(defectFoundBy, defect.getFoundBy());
        Assert.assertEquals(defectEstimate, defect.getEstimate(), 0.009);
        Assert.assertEquals(getSandboxProject(), defect.getProject());

        defect.delete();
    }

    @Test
    public void testRussianAcceptLanguage() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(new Locale("ru", "RU"));
        createBasicDefect(getSandboxProject()).getID();
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void testRussianCustomHeaderAcceptLanguage() {
        getInstance().getCustomHttpHeaders().put("Accept-Language", "ru_RU");
        createBasicDefect(getSandboxProject()).getID();
    }

    @Test
    public void testEnglishAcceptLanguage() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(new Locale("en", "US"));
        createBasicDefect(getSandboxProject()).getID();
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void testCreateDefectWithRequiredAttributes() {
        final String description = "Test for Defect creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        getInstance().setValidationEnabled(true);

        Defect defect = getSandboxProject().createDefect(defectName, attributes);

        resetInstance();

        defect = getInstance().get().defectByID(defect.getID());

        Assert.assertEquals(defectName, defect.getName());
        Assert.assertEquals(description, defect.getDescription());
        Assert.assertEquals(getSandboxProject(), defect.getProject());

        defect.delete();
        getInstance().setValidationEnabled(false);
    }


    @Test
    public void createDefectWithAdditionalAttributes() {
    	Map<String, Object> attributes = new HashMap<String, Object>();
    	attributes.put("Description", defectDescription);
    	attributes.put("Estimate", defectEstimate);
    	attributes.put("FoundBy", defectFoundBy);
        Defect defect = getSandboxProject().createDefect(defectName, attributes);

        AssetID id = defect.getID();
        resetInstance();

        defect = getInstance().get().defectByID(id);

        Assert.assertEquals(defectName, defect.getName());
        Assert.assertEquals(defectDescription, defect.getDescription());
        Assert.assertEquals(defectFoundBy, defect.getFoundBy());
        Assert.assertEquals(defectEstimate, defect.getEstimate(), 0.009);
        Assert.assertEquals(getSandboxProject(), defect.getProject());

        defect.delete();
    }

    @Test
    public void canClose() {
        Defect defect = createBasicDefect(getSandboxProject());

        Assert.assertTrue(defect.canClose());

        defect.close();

        Assert.assertFalse(defect.canClose());
    }

    @Test(expected = java.lang.IllegalStateException.class)
    public void honorTrackingLevelToDo() {
        // The V1SDKTests system is assumed to be configured for "Defect:On"
        Defect defect = getSandboxProject().createDefect("Honor Tracking Level");

        Task cannotHaveToDo = defect.createTask("Cannot Have ToDo");

        cannotHaveToDo.setToDo(10.); //Should throw
    }

    @Test(expected = IllegalStateException.class)
    public void honorTrackingLevelDetailEstimate() {
        // The V1SDKTests system is assumed to be configured for "Defect:On"
        Defect defect = getSandboxProject().createDefect("Honor Tracking Level");

        Task cannotHaveDetailEstimate = defect.createTask("Cannot Have DetailEstimate");

        cannotHaveDetailEstimate.setDetailEstimate(10.); //Should throw
    }

    @Test(expected = IllegalStateException.class)
    public void honorTrackingLevelEffort() {
        // The V1SDKTests system is assumed to be configured for "Defect:On"
        Defect defect = getSandboxProject().createDefect("Honor Tracking Level");

        Task cannotHaveEffort = defect.createTask("Cannot Have Effort");

        cannotHaveEffort.createEffort(10.); // should throw
    }

    private static Defect createBasicDefect(Project project) {
        Defect defect = project.createDefect(defectName);
        defect.setDescription(defectDescription);
        defect.setEstimate(defectEstimate);
        defect.setFoundBy(defectFoundBy);
        defect.save();
        return defect;
    }
}
