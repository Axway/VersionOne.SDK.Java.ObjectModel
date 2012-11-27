/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import com.versionone.om.filters.RegressionSuiteFilter;

import java.util.Collection;
import java.util.Map;

/**
 * The RegressionPlan asset.
 */
@MetaDataAttribute("RegressionPlan")
public class RegressionPlan extends ProjectAsset {

    /**
     * Constructor used to represent an RegressionPlan entity that DOES exist in the VersionOne System.
     *
     * @param assetId  Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    RegressionPlan(AssetID assetId, V1Instance instance) {
        super(assetId, instance);
    }

    /**
     * Constructor used to represent an RegressionPlan entity that does NOT yet exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    RegressionPlan(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Member who owns this RegressionPlan.
     */
    public Member getOwner() {
        return getRelation(Member.class, "Owner");
    }

    /**
     * @param value The Member who owns this RegressionPlan.
     */
    public void setOwner(Member value) {
        setRelation("Owner", value);
    }

    /**
     * @return Cross-reference of this RegressionPlan with an external system.
     */
    public String getReference() {
        return (String) get("Reference");
    }

    /**
     * @param value Cross-reference of this RegressionPlan with an external system.
     */
    public void setReference(String value) {
        set("Reference", value);
    }

    /**
     * Create a new Regression Suite with title assigned with this Regression Plan.
     *
     * @param name Title of the suite.
     * @return Regression Suite.
     */
    public RegressionSuite createRegressionSuite(String name) {
        return getInstance().create().regressionSuite(name, this, null);
    }

    /**
     * Create a new Regression Suite with title assigned with this Regression Plan.
     *
     * @param name       Title of the suite.
     * @param attributes Additional attributes for initialization Regression Suite.
     * @return Regression Suite.
     */
    public RegressionSuite createRegressionSuite(String name, Map<String, Object> attributes) {
        return getInstance().create().regressionSuite(name, this, attributes);
    }

    /**
     * Projects associated with this TestSuite.
     *
     * @param filter Criteria to filter on. If null, all RegressionSuites in the project are returned.
     * @return An Collection of RegressionSuites.
     */
    public Collection<RegressionSuite> getRegressionSuites(RegressionSuiteFilter filter) {
        filter = (filter != null) ? filter : new RegressionSuiteFilter();

        filter.regressionPlan.clear();
        filter.regressionPlan.add(this);

        return getInstance().get().regressionSuites(filter);
    }

    @Override
    boolean canCloseImpl() {
        return false;
    }

    @Override
    boolean canReactivateImpl() {
        return false;
    }

    /**
     * @throws UnsupportedOperationException The RegressionPlan is an invalid state for
     *                                       the Operation, e.g. it is already closed.
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot close regression plan.");
    }

    /**
     * @throws UnsupportedOperationException The RegressionPlan is an invalid state for
     *                                       the Operation, e.g. it is already active.
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Cannot reactivate regression plan.");
    }
}

