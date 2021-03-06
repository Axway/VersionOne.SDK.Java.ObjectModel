/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IOperation;
import com.versionone.apiclient.MetaException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class MockAssetType implements IAssetType {
    private String name;
    private Map<String, IAttributeDefinition> tokens = new HashMap<String, IAttributeDefinition>();

    public MockAssetType() {
        name = "Mock";
    }

    public MockAssetType(String name) {
        this.name = "Mock." + name;
    }

    public IAttributeDefinition getAttributeDefinition(String name) throws MetaException {
        if (!tokens.containsKey(name)) {
            tokens.put(name, new MockAttributeDefinition("AutogeneratedByName-" + name, this));
        }
        return tokens.get(name);
    }

    public Collection<IAttributeDefinition> getAttributeDefinitions() {
        return tokens.values();
    }

    public void putAttributeDefinition(IAttributeDefinition attrDef, String name) throws MetaException {
        tokens.put(name, attrDef);
    }

    public void createAttributeDefinition(String name) throws MetaException {
        tokens.put(name, new MockAttributeDefinition(name));
    }

    public IAssetType getBase() throws MetaException {
        return null;
    }

    public IAttributeDefinition getDefaultOrderBy() throws MetaException {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getToken() {
        return name;
    }

    public boolean isA(IAssetType targetType) throws MetaException {
        if (!(targetType instanceof MockAssetType))
            return false;
        MockAssetType mockAssetType = (MockAssetType) targetType;
        return name.startsWith(mockAssetType.name);
    }

    public IAttributeDefinition getShortNameAttribute() {
        return null;
    }

    public IAttributeDefinition getNameAttribute() {
        return null;
    }

    public IAttributeDefinition getDescriptionAttribute() {
        return null;
    }

    public IOperation getOperation(String name) {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ':' + name;
    }
}