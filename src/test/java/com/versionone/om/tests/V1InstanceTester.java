/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Test;

import com.versionone.apiclient.AttributeSelection;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.om.Defect;
import com.versionone.om.Project;
import com.versionone.om.TesterBase;
import com.versionone.om.V1Instance;
import com.versionone.om.listvalue.CustomListValue;

public class V1InstanceTester extends TesterBase {
    private MockAssetType superType;
    private MockAssetType type;
    private MockAssetType subType;
    private MockAssetType wrongType;

    private AttributeSelection flattenSelection(IAssetType querytype, List<IAttributeDefinition> orig) throws Exception {
        return (AttributeSelection) invokePrivateMethod(V1Instance.class, "flattenSelection",
                new Class<?>[]{IAssetType.class, List.class}, querytype, orig);
    }

    private void beforeFlattenSelection() {
        superType = new MockAssetType("SuperType");
        type = new MockAssetType("SuperType.Type");
        subType = new MockAssetType("SuperType.Type.Sub");
        wrongType = new MockAssetType("WrongType");
    }

    @Test
    public void testFlattenSelectionEmpty() throws Exception {
        IAssetType querytype = new MockAssetType();
        List<IAttributeDefinition> orig = new LinkedList<IAttributeDefinition>();
        AttributeSelection res = flattenSelection(querytype, orig);
        Assert.assertEquals("Expect empty", res.size(), 0);
    }

    @Test
    public void testFlattenSelectionSuperToken() throws Exception {
        beforeFlattenSelection();
        final MockAttributeDefinition superToken = new MockAttributeDefinition("Token1", superType);
        final List<IAttributeDefinition> orig = new LinkedList<IAttributeDefinition>();
        orig.add(superToken);
        type.putAttributeDefinition(superToken, "Token1");

        AttributeSelection res = flattenSelection(type, orig);

        assertEqualsAsSet(null, Arrays.asList(superToken), res);
    }

    @Test
    public void testFlattenSelection() throws Exception {
        beforeFlattenSelection();
        final MockAttributeDefinition tokenB = new MockAttributeDefinition("Token0", type);
        final MockAttributeDefinition superTokenB = new MockAttributeDefinition("Token-1", superType);
        final MockAttributeDefinition subTokenB = new MockAttributeDefinition("Token-2", subType);

        final MockAttributeDefinition superToken = new MockAttributeDefinition("Token1", superType);
        final MockAttributeDefinition token = new MockAttributeDefinition("Token2", type);
        final MockAttributeDefinition subToken = new MockAttributeDefinition("Token3", subType);
        final MockAttributeDefinition wrongToken = new MockAttributeDefinition("Tooken4", wrongType);
        final MockAttributeDefinition basedToken = new MockAttributeDefinition("Tooken6", type, tokenB);
        final MockAttributeDefinition basedWrongToken = new MockAttributeDefinition("Tooken7", wrongType, tokenB);
        final MockAttributeDefinition wrongBasedToken = new MockAttributeDefinition("Tooken8", type, wrongToken);
        final MockAttributeDefinition superBasedToken = new MockAttributeDefinition("Tooken9", subType, superTokenB);
        final MockAttributeDefinition subBasedToken = new MockAttributeDefinition("Tooken10", subType, subTokenB);
        final List<IAttributeDefinition> orig = new LinkedList<IAttributeDefinition>();
        orig.add(superToken);
        orig.add(token);
        orig.add(subToken);
        orig.add(wrongToken);
        orig.add(basedToken);
        orig.add(basedWrongToken);
        orig.add(wrongBasedToken);
        orig.add(superBasedToken);
        orig.add(subBasedToken);

        AttributeSelection res = flattenSelection(type, orig);

        AttributeSelection expected = new AttributeSelection();
        expected.addAll(orig);
        expected.add(expected.indexOf(superToken), type.getAttributeDefinition(superToken.getName()));
        expected.remove(superToken);
        expected.remove(wrongToken);
        expected.remove(basedWrongToken);
        expected.add(expected.indexOf(superBasedToken), type.getAttributeDefinition(superBasedToken.getName()));
        expected.remove(superBasedToken);
        expected.remove(subBasedToken);
        expected.add(subTokenB);

        assertEqualsAsSet(null, expected, res);
    }

    private Mockery mockery = new Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    @Test
    public void testFlattenSelectionJMock() throws Exception {
        final IAssetType type1 = mockery.mock(IAssetType.class, "type1");
        final IAttributeDefinition token0 = mockery.mock(IAttributeDefinition.class, "Token0");
        List<IAttributeDefinition> orig = new LinkedList<IAttributeDefinition>();
        orig.add(token0);
        mockery.checking(new Expectations() {
            {
                one(token0).getAssetType();
                will(returnValue(type1));
                one(token0).getBase();
                will(returnValue(null));
                one(type1).isA(type1);
                will(returnValue(true));
            }
        });

        AttributeSelection res = flattenSelection(type1, orig);

        assertEqualsAsSet(null, res, Arrays.asList(token0));
    }

    @Test
    public void testFlattenSelectionJMock2() throws Exception {
        final IAssetType type1 = mockery.mock(IAssetType.class, "type1");
        final IAssetType type2 = mockery.mock(IAssetType.class, "type2");
        final IAttributeDefinition token0 = mockery.mock(IAttributeDefinition.class, "Token0");
        final IAttributeDefinition token1 = mockery.mock(IAttributeDefinition.class, "Token1");

        List<IAttributeDefinition> orig = new LinkedList<IAttributeDefinition>();
        orig.add(token1);
        mockery.checking(new Expectations() {
            {
                allowing(token1).getAssetType();
                will(returnValue(type2));
                one(type2).isA(type1);
                will(returnValue(true));
                one(type1).isA(type1);
                will(returnValue(false));
                one(type1).getAttributeDefinition("Token1");
                will(returnValue(token1));

                allowing(token1).getBase();
                will(returnValue(token0));
                one(token1).getName();
                will(returnValue("Token1"));
                one(token0).getAssetType();
                will(returnValue(type1));
            }
        });

        AttributeSelection res = flattenSelection(type1, orig);

        assertEqualsAsSet(null, res, Arrays.asList(token1));
    }

    private static List<IAttributeDefinition> getSuggestedSelection(IAssetType assetType, Class type) throws Exception {
        return (List<IAttributeDefinition>) invokePrivateMethod(
                V1Instance.class, "getSuggestedSelection",
                new Class<?>[]{IAssetType.class, Class.class}, assetType, type);
    }

    private void beforeGetSuggestedSelection() {
        type = new MockAssetType("MockType");
    }

    @Test
    public void testGetSuggestedSelectionForDefect() throws Exception {
        beforeGetSuggestedSelection();
        type.createAttributeDefinition("Number");
        type.createAttributeDefinition("Name");
        List<IAttributeDefinition> res = getSuggestedSelection(type, Defect.class);

        assertEqualsAsSet(null, type.getAttributeDefinitions(), res);
    }

    @Test
    public void testGetSuggestedSelectionForProject() throws Exception {
        beforeGetSuggestedSelection();
        type.createAttributeDefinition("Name");
        List<IAttributeDefinition> res = getSuggestedSelection(type, Project.class);

        assertEqualsAsSet(null, type.getAttributeDefinitions(), res);
    }

    @Test
    public void testGetSuggestedSelectionForCustomListValue() throws Exception {
        beforeGetSuggestedSelection();
        List<IAttributeDefinition> res = getSuggestedSelection(type, CustomListValue.class);

        Assert.assertEquals(0, res.size());
    }
}
