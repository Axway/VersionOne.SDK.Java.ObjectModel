package com.versionone.om;

import com.versionone.om.filters.BaseAssetFilter;

/**
 * Filter for getting teams.
 */
public class TeamFilter extends BaseAssetFilter {

    @Override
	public
    Class<? extends Entity> getEntityType() {
        return Team.class;
    }

}
