/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.versionone.om.*;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.filters.StoryFilter;

public class TeamTester extends BaseSDKTester {

    @Test
    public void testAssignTeamToStory() {
        Team team = getInstance().create().team("Bears");

        Story teamStory = getSandboxProject().createStory("For Team");

        teamStory.setTeam(team);
        teamStory.save();

        resetInstance();
        team = getInstance().get().teamByID(team.getID());
        teamStory = getInstance().get().storyByID(teamStory.getID());

        boolean included = false;
        for (PrimaryWorkitem story : team
                .getPrimaryWorkitems(new StoryFilter())) {
            if (story.equals(teamStory)) {
                included = true;
                break;
            }
        }

        Assert.assertTrue("Expected Story \"" + team.getName()
                + "\" in team \"" + teamStory.getName() + "\" stories.",
                included);
    }

    @Test
    public void testEnumerateRetrospectives() {
        Collection<Retrospective> retros = getInstance().get().teamByID(
                "Team:1763").getRetrospectives(null);

        String[] expected = new String[]{"Retrospective:1789",
                "Retrospective:1790", "Retrospective:1791"};
        ListAssert.areEqualIgnoringOrder(expected, retros,
                new EntityToAssetIDTransformer<Retrospective>());
    }

    @Test
    public void testCreateTeamWithAttributes() {
        final String name = "Team Name";
        final String description = "Test for Team creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);

        Team team = getInstance().create().team(name, attributes);

        Assert.assertEquals(name, team.getName());
        Assert.assertEquals(description, team.getDescription());

        team.delete();
    }
}
