package com.versionone.om.tests;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.versionone.DB.DateTime;
import com.versionone.om.BuildProject;
import com.versionone.om.BuildRun;
import com.versionone.om.ChangeSet;
import com.versionone.om.EntityCollection;


/**
 * Test for the EntityCollection class
 */
public class EntityCollectionTester extends BaseSDKTester {

    static final String changeSetName = "Test ChangeSet";
    static final String changeSetRef = "123456";

    @Test
    public void testCopyTo() {
        final int changeSetCount = 3;

        ChangeSet changeSetA =
                getInstance().create().changeSet(changeSetName, changeSetRef);
        final ChangeSet[] changeSetsB = new ChangeSet[changeSetCount + 1];
        final String[] expectChangeSetName = new String[changeSetCount + 1];
        expectChangeSetName[0] = changeSetA.getName();

        for (int i = 0; i < changeSetCount; i++) {
            changeSetsB[i] = getInstance().create().
                    changeSet(changeSetName + i, changeSetRef + i);
            expectChangeSetName[i + 1] = changeSetsB[i].getName();
        }

        BuildProject buildProject =
                getInstance().create().buildProject("BP", "1234");
        BuildRun buildRun = buildProject.createBuildRun("BR", DateTime.now());
        EntityCollection<ChangeSet> entityCollection = (EntityCollection<ChangeSet>)buildRun.getChangeSets();
        Assert.assertEquals(0, entityCollection.size());

        entityCollection.add(changeSetA);
        entityCollection.copyTo(changeSetsB, changeSetCount);

        ListAssert.areEqualIgnoringOrder(expectChangeSetName,
                Arrays.asList(changeSetsB),
                new EntityToNameTransformer<ChangeSet>());

        // clear all data.
        for (ChangeSet item : changeSetsB) {
            item.delete();
        }
        changeSetA.delete();
        buildRun.delete();
        buildProject.delete();
    }
}
