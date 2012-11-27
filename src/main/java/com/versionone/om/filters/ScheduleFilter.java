package com.versionone.om.filters;

import java.util.Collection;

import com.versionone.om.Entity;
import com.versionone.om.Project;
import com.versionone.om.Schedule;

public class ScheduleFilter extends BaseAssetFilter 
{

	@Override
	public Class<? extends Entity> getEntityType() {
		return Schedule.class;
	}

    /**
     *  Filter on Projects
     */
    public Collection<Project> projects = newList();

    @Override
	void internalModifyFilter(FilterBuilder builder) {
		super.internalModifyFilter(builder);
		builder.multiRelation("ScheduledScopes", projects);
	}
}
