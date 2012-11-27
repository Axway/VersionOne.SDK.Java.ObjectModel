/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

import java.util.Collection;

import com.versionone.om.filters.PrimaryWorkitemFilter;
import com.versionone.om.listvalue.DefectResolutionReason;
import com.versionone.om.listvalue.DefectType;

/**
 * A Defect.
 */
@MetaDataAttribute(value = "Defect", defaultOrderByToken = "Defect.Order")
public class Defect extends PrimaryWorkitem {

    /**
     * Constructor used to represent an Defect entity that DOES exist in the
     * VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    Defect(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an Defect entity that does NOT yet exist in
     * the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    Defect(V1Instance instance) {
        super(instance);
    }

    /**
     * @return Free text field indicating environment this Defect was identified
     *         in.
     */
    public String getEnvironment() {
        return (String) get("Environment");
    }

    /**
     * @param value Free text field indicating environment this Defect was
     *                identified in.
     */
    public void setEnvironment(String value) {
        set("Environment", value);
    }

    /**
     * @return Free text field indicating who found the defect.
     */
    public String getFoundBy() {
        return (String) get("FoundBy");
    }

    /**
     * @param value Free text field indicating who found the defect.
     */
    public void setFoundBy(String value) {
        set("FoundBy", value);
    }

    /**
     * @return Build number where this Defect was found.
     */
    public String getFoundInBuild() {
        return (String) get("FoundInBuild");
    }

    /**
     * @param value Build number where this Defect was found.
     */
    public void setFoundInBuild(String value) {
        set("FoundInBuild", value);
    }

    /**
     * @return Version where this Defect was found.
     */
    @MetaRenamedAttribute("VersionAffected")
    public String getFoundInVersion() {
        return (String) get("VersionAffected");
    }

    /**
     * @param value Version where this Defect was found.
     */
    @MetaRenamedAttribute("VersionAffected")
    public void setFoundInVersion(String value) {
        set("VersionAffected", value);
    }

    /**
     * @return Build number where this Defect was resolved.
     */
    @MetaRenamedAttribute("FixedInBuild")
    public String getResolvedInBuild() {
        return (String) get("FixedInBuild");
    }

    /**
     * @param value Build number where this Defect was resolved.
     */
    @MetaRenamedAttribute("FixedInBuild")
    public void setResolvedInBuild(String value) {
        set("FixedInBuild", value);
    }

    /**
     * @return Reason this Defect was resolved.
     */
    public IListValueProperty getResolutionReason() {
        return getListValue(DefectResolutionReason.class, "ResolutionReason");
    }

    /**
     * @return Text field for the description of how this Defect was resolved.
     */
    @MetaRenamedAttribute("Resolution")
    public String getResolutionDetails() {
        return (String) get("Resolution");
    }

    /**
     * @param value Text field for the description of how this Defect was
     *                resolved.
     */
    @MetaRenamedAttribute("Resolution")
    public void setResolutionDetails(String value) {
        set("Resolution", value);
    }

    /**
     * @return The Member this defect was verified by.
     */
    public Member getVerifiedBy() {
        return getRelation(Member.class, "VerifiedBy");
    }

    /**
     * @param value The Member this defect was verified by.
     */
    public void setVerifiedBy(Member value) {
        setRelation("VerifiedBy", value);
    }

    /**
     * @return This Defect's Type.
     */
    public IListValueProperty getType() {
        return getListValue(DefectType.class, "Type");
    }

    /**
     * @return Build Run's this Defect was found in.
     */
    @MetaRenamedAttribute("FoundInBuildRuns")
    public Collection<BuildRun> getFoundIn() {
        return getMultiRelation("FoundInBuildRuns");
    }

    /**
     * Primary workitems that are affected by this defect.
     *
     * @param filter Criteria to filter stories and defects on. Pass a
     *                DefectFilter or StoryFilter to get only Defects or
     *                Stories, respectively.
     * @return list of the primary work items.
     */
    public Collection<PrimaryWorkitem> getAffectedPrimaryWorkitems(
            PrimaryWorkitemFilter filter) {
        filter = (filter != null) ? filter : new PrimaryWorkitemFilter();

        filter.affectedByDefects.clear();
        filter.affectedByDefects.add(this);
        return getInstance().get().primaryWorkitems(filter);
    }

    /**
     * Inactivates the Defect.
     *
     * @throws UnsupportedOperationException The Defect is an invalid state for
     *                 the Operation, e.g. it is already closed.
     * @see com.versionone.om.BaseAsset#closeImpl().
     */
    @Override
    void closeImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Inactivate");
    }

    /**
     * Reactivates the Defect.
     *
     * @throws UnsupportedOperationException The Defect is an invalid state for.
     *                 the Operation, e.g. it is already closed.
     * @see com.versionone.om.BaseAsset#reactivateImpl().
     */
    @Override
    void reactivateImpl() throws UnsupportedOperationException {
        getInstance().executeOperation(this, "Reactivate");
    }
}
