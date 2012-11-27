package com.versionone.om.tests;

import com.versionone.om.*;
import com.versionone.om.filters.SecondaryWorkitemFilter;
import com.versionone.DB.DateTime;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemberTester extends BaseSDKTester {
    @Test
    public void testGetByName() {
        Member fred = null;

        for (Member member : getInstance().get().members(null)) {
            if (member.getUsername() != null) {
                fred = member;
                break;
            }
        }

        Assert.assertNotNull(fred);

        AssetID oldID = fred.getID();
        resetInstance();
        fred = getInstance().get().memberByID(oldID);

        Assert.assertEquals(fred.getID(), getInstance().get().memberByUserName(fred.getUsername()).getID());
    }

    @Test
    public void createMemberWithAttributes() {
        final String phone = "555-555-5555";
        final String email = "test@test.com";
        final String name = "TestMemberName";
        final String description = "Test for Member creation with required attributes";
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("Description", description);
        attributes.put("Phone", phone);
        attributes.put("Email", email);

        Member member = getInstance().create().member(name, newGuid(), attributes);
        AssetID epicID = member.getID();

        resetInstance();

        member = getInstance().get().memberByID(epicID);

        Assert.assertEquals(name, member.getName());
        Assert.assertEquals(phone, member.getPhone());
        Assert.assertEquals(email, member.getEmail());
        Assert.assertEquals(description, member.getDescription());

        member.delete();
    }

    @Test
    public void create() {
        final String phone = "555-555-5555";
        final String email = "test@test.com";

        Member member = getInstance().create().member("Test User", newGuid());
        member.setEmail(email);
        member.setPhone(phone);
        member.save();
        final double creationTime = (double) System.currentTimeMillis();

        AssetID memberId = member.getID();

        resetInstance();

        member = getInstance().get().memberByID(memberId);

        Assert.assertEquals(email, member.getEmail());
        Assert.assertEquals(phone, member.getPhone());
        Assert.assertFalse(member.getNotifyViaEmail());
        final double DELTA = 10 * 60 * 1000;//10 minutes
        Assert.assertEquals(creationTime, member.getCreateDate().getValue().getTime(), DELTA);
        Assert.assertEquals(creationTime, member.getChangeDate().getValue().getTime(), DELTA);
    }

    @Test
    public void createWithComment() {
        final String phone = "@&'10101-010101";
        final String email = "&asdc'<>testcomment@testcomment.com";
        final String userName = "Test User with comment";
        final String comment = "Comment !@#$%^&*+<> тест";
        final String edited = " edited";

        Member member = getInstance().create().member(userName, newGuid());
        member.setEmail(email);
        member.setPhone(phone);
        member.save(comment);

        AssetID memberId = member.getID();

        resetInstance();

        member = getInstance().get().memberByID(memberId);

        Assert.assertEquals(email, member.getEmail());
        Assert.assertEquals(phone, member.getPhone());
        Assert.assertEquals(userName, member.getName());

        member.setName(userName + edited);
        member.save(comment + edited);

        resetInstance();

        member = getInstance().get().memberByID(memberId);
        Assert.assertEquals(userName + edited, member.getName());

        member.delete();
    }

    @Test
    public void ownedSecondaryWorkitems() {
        final String taskName = "Task";

        Story story = getSandboxProject().createStory("Story");
        Task task = story.createTask(taskName);
        story.createTest("Test");

        Member member = getInstance().get().memberByID("Member:20");

        task.getOwners().add(member);

        SecondaryWorkitemFilter filter = new SecondaryWorkitemFilter();
        filter.project.add(getSandboxProject());
        Collection<SecondaryWorkitem> items = member.getOwnedSecondaryWorkitems(filter);
        ListAssert.areEqual(new String[]{taskName}, items, new EntityToNameTransformer<SecondaryWorkitem>());
    }

    @Test
    public void sendConversationEmailsSetting() {
        Member member = getInstance().getLoggedInMember();
        boolean sendConversationEmails = member.getSendConversationEmails();
        member.setSendConversationEmails(!sendConversationEmails);
        member.save();

        Assert.assertTrue(member.getSendConversationEmails() != sendConversationEmails);
    }
}
