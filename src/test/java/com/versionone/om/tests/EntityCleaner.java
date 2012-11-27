/*(c) Copyright 2012, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.tests;

import java.util.Collection;
import java.util.Stack;

import com.versionone.om.BaseAsset;
import com.versionone.om.Entity;
import com.versionone.om.Environment;
import com.versionone.om.Project;
import com.versionone.om.Workitem;

class EntityCleaner {
	private final Stack<BaseAsset> baseAssets;
    private final Stack<Entity> entities;
    private final Project defaultProject;

	EntityCleaner(Stack<BaseAsset> baseAssets, Stack<Entity> entities, Project defaultProject) {
		this.baseAssets = baseAssets;
        this.entities = entities;
        this.defaultProject = defaultProject;
	}

	void delete() {
        processEntities();
        deleteBaseAssets();
    }

    private void processEntities() {
        while (entities.size() > 0) {
            Entity item = entities.pop();

            if (item instanceof Environment) {
                assignEnvironmentToDefaultScope((Environment)item);
            }
        }
    }

    private void assignEnvironmentToDefaultScope(Environment environment) {
        if (environment.isClosed()) {
            environment.reactivate();
        }

        environment.setProject(defaultProject);
        environment.save();
    }

    private void deleteBaseAssets() {
        while (baseAssets.size() > 0) {
            BaseAsset item = baseAssets.pop();
            prepareAssetForDelete(item);

            deleteAsset(item);
        }
    }

    private static void prepareAssetForDelete(BaseAsset item) {
        try {
            reopenProjectIfClosed(item);
            reopenIfClosed(item);
        } catch(Exception ex) {
        	System.out.println(ex.getMessage());
        }
    }

    private static void reopenProjectIfClosed(BaseAsset item) {
        if (!(item instanceof Workitem)) {
            return;
        }

        Workitem workitem = ((Workitem)item);
        if (workitem.getProject().isClosed() && workitem.getProject().canReactivate()) {
            reactivateAsset(workitem.getProject());
        }
    }

    private static void reopenIfClosed(BaseAsset item) {
        if (item.isClosed() && item.canReactivate()) {
            reactivateAsset(item);
        }
    }

    private static void reactivateAsset(BaseAsset item) {
        try {
            item.reactivate();
        } catch (Exception ex) {
        	System.out.println(String.format("Can't reactivate %s item.", item.getID().getToken()));
            System.out.println(ex.getMessage());
        }
    }

    private static void deleteAsset(BaseAsset item) {
        try {
            item.delete();
        } catch (Exception ex) {
        	System.out.println(String.format("Can't delete %s item.", item.getID().getToken()));
            System.out.println(ex.getMessage());
        }
    }
}
