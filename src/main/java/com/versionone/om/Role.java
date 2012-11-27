/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om;

/**
 * List of roles allowed on a member. <p/> The id's of these roles must match
 * the Role id's in the VersionOne system.
 */
@MetaDataAttribute("Role")
public class Role extends Entity {

    /**
     * Has no read or write access to any entities in the VersionOne System.
     */
    public static final Role NO_ACCESS = new Role(AssetID.valueOf("Role:0"));
    /**
     * Has full access to all entities in the VersionOne System. 
     * Can administer members, custom attributes, list values, and system
     * wide configuration.
     */
    public static final Role SYSTEM_ADMIN = new Role(AssetID.valueOf("Role:1"));
	/**
	 *  Has full read and write access to all entities in the VersionOne System
	 *  Can manage Project Membership, Program Membership, Members, Member Groups, Member Security, and Teams
	 */
	public static final Role MemberAdmin = new Role(AssetID.valueOf("Role:12"));
    /**
     * Has full read and write access to all entities in the VersionOne System.
     * Can manage Project Membership and Program Membership.
     */
    public static final Role PROJECT_ADMIN = new Role(AssetID.valueOf("Role:2"));
    /**
     * Has full read and write access to all entities in the VersionOne System
     * Cannot manage Project Membership or Program Membership.
     */
    public static final Role PROJECT_LEAD = new Role(AssetID.valueOf("Role:3"));
    /**
     * Has read access to all entities in the VersionOne System Has write access
     * to all entities except Projects, Iterations, and Goals in the VersionOne
     * System.
     */
    public static final Role TEAM_MEMBER = new Role(AssetID.valueOf("Role:4"));
    /**
     * Has read access to all entities in the VersionOne System Has write access
     * to Tasks, Effort, Issues, Requests, and Defects in the VersionOne System.
     */
    public static final Role DEVELOPER = new Role(AssetID.valueOf("Role:5"));
    /**
     * Has read access to all entities in the VersionOne System Has write access
     * to Tests, Effort, Issues, Requests, and Defects in the VersionOne System.
     */
    public static final Role TESTER = new Role(AssetID.valueOf("Role:6"));
    /**
     * Has read access to all entities in the VersionOne System Has write access
     * to Themes, Stories, Tests, Issues, Requests, Defects, and Goals in the
     * VersionOne System.
     */
    public static final Role CUSTOMER = new Role(AssetID.valueOf("Role:7"));
    /**
     * Has read access but no write access to all entities in the VersionOne
     * System.
     */
    public static final Role OBSERVER = new Role(AssetID.valueOf("Role:8"));
    /**
     * Has read access to Projects, Themes, Stories, Iterations, Defects,
     * Retrospectives, and Goals but no write access in the VersionOne System.
     */
    public static final Role VISITOR = new Role(AssetID.valueOf("Role:9"));
    /**
     * Has read access to Projects, Themes, Iterations, and Goals but no write
     * access in the VersionOne System.
     */
    public static final Role INHERITOR = new Role(AssetID.valueOf("Role:10"));
	/**
	 * Has read access to all entities in the VersionOne System and write access to Requests
	 */
	public static final Role Requestor = new Role(AssetID.valueOf("Role:11"));
    
    Role(V1Instance instance) {
        super(instance);
    }

    Role(AssetID id, V1Instance instance) {
        super(id, instance);
    }

    private Role(AssetID id) {
        super(id, null);
    }

    /**
     * Roles cannot be updated or saved.
     *
     * @param comment Comment.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void save(String comment) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Role's cannot be updated or saved.");
    }

    /**
     * Roles cannot be updated or saved.
     *
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void save() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Roles cannot be updated or saved.");
    }
}
