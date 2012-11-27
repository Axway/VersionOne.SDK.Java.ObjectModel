/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.om.Member;
import com.versionone.om.filters.MemberFilter;

public class MemberFilterTester extends BaseSDKTester {

    @Test
    public void testShortName() {
        Member admin = getInstance().get().memberByID("Member:20");

        MemberFilter filter = new MemberFilter();
        String shortName = admin.getShortName();
        filter.shortName.add(shortName);

        Collection<Member> members = getInstance().get().members(filter);
        Assert.assertTrue(findRelated(admin, members));

        for (Member member : members) {
            Assert.assertEquals(shortName, member.getShortName());
        }
    }

    @Test
    public void testUserName() {
        Member admin = getInstance().get().memberByID("Member:20");

        MemberFilter filter = new MemberFilter();
        String userName = admin.getUsername();
        filter.username.add(userName);

        Collection<Member> members = getInstance().get().members(filter);
        Assert.assertTrue(findRelated(admin, members));

        for (Member member : members) {
            Assert.assertEquals(userName, member.getUsername());
        }
    }
}
