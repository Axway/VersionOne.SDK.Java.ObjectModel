/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.MetaException;

class MockAttributeDefinition implements IAttributeDefinition {

    private static final String X = "+";

    String name;
    AttributeType type = AttributeType.Text;
    IAssetType assetType;
    IAttributeDefinition base;

    public MockAttributeDefinition(String name) {
        this.name = name;
    }

    public MockAttributeDefinition(String name, AttributeType type) {
        this.name = name;
        this.type = type;
    }

    public MockAttributeDefinition(String name, IAssetType assetType) {
        this.name = name;
        this.assetType = assetType;
    }

    public MockAttributeDefinition(String name, IAttributeDefinition base) {
        this.name = name;
        this.base = base;
    }

    public MockAttributeDefinition(String name, IAssetType assetType, IAttributeDefinition base) {
        this.name = name;
        this.assetType = assetType;
        this.base = base;
    }

    public Object coerce(Object value) throws MetaException {
        return value;
    }

    public IAssetType getAssetType() throws MetaException {
        return assetType;
    }

    public AttributeType getAttributeType() {
        return type;
    }

    public IAttributeDefinition getBase() throws MetaException {
        return base;
    }

    public String getDisplayName() {
        return null;
    }

    public String getName() {
        return name;
    }

    public IAssetType getRelatedAsset() throws MetaException {
        return null;
    }

    public String getToken() {
        return "Mock." + name;
    }

    public boolean isMultiValue() {
        return false;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isRequired() {
        return false;
    }

    public String toString() {
        return getEqualsString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MockAttributeDefinition)) return false;

        MockAttributeDefinition that = (MockAttributeDefinition) o;
        return getEqualsString().equals(that.getEqualsString());
    }

    public int hashCode() {
        return getEqualsString().hashCode();
    }

    private String getEqualsString() {
        return name + X + type + X + base + X + assetType;
    }

    public IAttributeDefinition aggregate(
            com.versionone.apiclient.IAttributeDefinition.Aggregate aggregate) {
        // not using method
        return null;
    }

    public IAttributeDefinition downcast(IAssetType assetType) {
        // not using method
        return null;
    }

    public IAttributeDefinition filter(IFilterTerm filter) {
        // not using method
        return null;
    }

    public IAttributeDefinition join(IAttributeDefinition joined) {
        // not using method
        return null;
    }
}