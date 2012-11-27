/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * An Entity that only exists in the context of a Project.
 */
@MetaDataAttribute(defaultAttributeSelectionNames = "Number")
public abstract class ProjectAsset extends BaseAsset {

    /**
     * Constructor used to represent an ProjectAsset entity that DOES exist in
     * the VersionOne System.
     *
     * @param id Unique ID of this entity.
     * @param instance this entity belongs to.
     */
    ProjectAsset(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    /**
     * Constructor used to represent an ProjectAsset entity that does NOT yet
     * exist in the VersionOne System.
     *
     * @param instance this entity belongs to.
     */
    ProjectAsset(V1Instance instance) {
        super(instance);
    }

    /**
     * @return The Project this ProjectAsset belongs in. This must be present.
     */
    @MetaRenamedAttribute("Scope")
    public Project getProject() {
        return getRelation(Project.class, "Scope");
    }

    /**
     * @param project The Project this ProjectAsset belongs in.
     */
    public void setProject(Project project) {
        setRelation("Scope", project);
    }

    /**
     * @return ID (or Number) of this entity as displayed in the VersionOne
     *         system.
     */
    @MetaRenamedAttribute("Number")
    public String getDisplayID() {
        return (String) get("Number");
    }
}