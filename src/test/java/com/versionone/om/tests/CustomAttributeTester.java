/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.Story;

public class CustomAttributeTester extends BaseSDKTester {

    private static final String CUSTOM_LIST_ATTRIBUTE2 = "Custom List Attribute2";
    private static final String CUSTOM_LIST_ATTRIBUTE = "Custom List Attribute";
    private static final String CLEAR_CUSTOM_LIST_ATTRIBUTE = "Clear Custom List Attribute";
    private static final String NORTH_AMERICA = "North America";
    private static final String ORIGIN = "Origin";
    private static final String PIERRE_PART = "Pierre Part";
    private static final String BIRTHDAY = "Birthday";
    private static final String FRECKLES = "Freckles";
    private static final String SHOE_SIZE = "ShoeSize";
    private static final String NAPOLEONVILLE = "Napoleonville";
    private static final String HOMETOWN = "Hometown";
    private static final String CUSTOM_ATTRIBUTE_HELPERS = "Custom Attribute Helpers";
    private static final String SIMPLE_CUSTOM_ATTRIBUTE = "Simple Custom Attribute";
    private static final DateTime EXPECTED_BIRTHDAY = DateTime.now().getDate();
    private static final double SIZE_11_5 = 11.5d;

    @Test
    public void testGetSimpleCustomAttribute() {
        Story story = getSandboxProject().createStory(SIMPLE_CUSTOM_ATTRIBUTE);
        Assert.assertNull(story.getCustomField().get(HOMETOWN));
        story.getCustomField().set(HOMETOWN, NAPOLEONVILLE);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert
                .assertEquals(NAPOLEONVILLE, story.getCustomField().get(
                        HOMETOWN));
    }

    @Test
    public void testSimpleAttributeHelpers() {
        Story story = getSandboxProject().createStory(CUSTOM_ATTRIBUTE_HELPERS);
        story.getCustomField().set(SHOE_SIZE, SIZE_11_5);
        story.getCustomField().set(FRECKLES, true);
        story.getCustomField().set(HOMETOWN, PIERRE_PART);
        story.getCustomField().set(BIRTHDAY, EXPECTED_BIRTHDAY);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());

        double shoeSize = story.getCustomField().getNumeric(SHOE_SIZE);
        Assert.assertEquals(SIZE_11_5, shoeSize, ESTIMATES_PRECISION);

        boolean hasFreckles = story.getCustomField().getBoolean(FRECKLES);
        Assert.assertTrue(hasFreckles);

        DateTime birthday = story.getCustomField().getDate(BIRTHDAY);
        Assert.assertEquals(EXPECTED_BIRTHDAY, birthday);

        String hometown = story.getCustomField().getString(HOMETOWN);
        Assert.assertEquals(PIERRE_PART, hometown);
    }

    @Test
    public void testGetListCustomAttribute() {
        Story story = getSandboxProject().createStory(CUSTOM_LIST_ATTRIBUTE);
        story.getCustomDropdown().get(ORIGIN).setCurrentValue(NORTH_AMERICA);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertEquals(NORTH_AMERICA, story.getCustomDropdown()
                .get(ORIGIN).toString());
    }

    @Test
    public void testCustomListTypeValues() {
        Story story = getSandboxProject().createStory(CUSTOM_LIST_ATTRIBUTE2);

        int count = 0;
        boolean foundNAmerica = false;

        for (String value : story.getCustomDropdown().get(ORIGIN)
                .getAllValues()) {
            count++;

            if (value.equals(NORTH_AMERICA)) {
                foundNAmerica = true;
            }
        }

        Assert.assertEquals(
                "Expected 5 Origin values.  I know this is weak, but what can you do?",
                        5, count);
        Assert.assertTrue("Expected to find value '" + NORTH_AMERICA
                + "', but it was not in the collection.", foundNAmerica);
    }

    @Test
    public void testClearCustomListType() {
        Story story = getSandboxProject().createStory(
                CLEAR_CUSTOM_LIST_ATTRIBUTE);
        story.getCustomDropdown().get(ORIGIN).setCurrentValue(NORTH_AMERICA);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertEquals(NORTH_AMERICA, story.getCustomDropdown()
                .get(ORIGIN).toString());
        story.getCustomDropdown().get(ORIGIN).clearCurrentValue();
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertNull("Clear should remove the list value", story
                .getCustomDropdown().get(ORIGIN).toString());
        story.getCustomDropdown().get(ORIGIN).setCurrentValue(NORTH_AMERICA);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertEquals(NORTH_AMERICA, story.getCustomDropdown()
                .get(ORIGIN).toString());
        story.getCustomDropdown().get(ORIGIN).setCurrentValue(null);
        story.save();

        resetInstance();
        story = getInstance().get().storyByID(story.getID());
        Assert.assertNull("Clear should remove the list value", story
                .getCustomDropdown().get(ORIGIN).toString());
    }
}
