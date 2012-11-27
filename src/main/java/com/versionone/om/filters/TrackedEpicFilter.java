package com.versionone.om.filters;

import com.versionone.apiclient.*;
import com.versionone.om.Entity;
import com.versionone.om.Epic;
import com.versionone.om.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TrackedEpicFilter extends BaseAssetFilter {
    private final List<Project> projects = new ArrayList<Project>();

    @Override
    public Class<? extends Entity> getEntityType() {
        return Epic.class;
    }

    /**
     * Create filter to obtain tracked Epics for enlisted projects.
     *
     * @param projects
     */
    public TrackedEpicFilter(Collection projects) {
        if (projects != null) {
            this.projects.addAll(projects);
        }
    }

    @Override
    void internalModifyFilter(FilterBuilder builder) {
        super.internalModifyFilter(builder);

        try {
            if (projects.size() == 1) {
                createSingleScopeFilter(builder);
            } else {
                createMultipleScopeFilter(builder);
            }
        } catch (APIException e) {
            throw new RuntimeException(e);
        }
    }

    private void createMultipleScopeFilter(FilterBuilder builder) throws APIException {
        builder.relation("Scope", projects);

        IMetaModel metaModel = builder.instance.getApiClient().getMetaModel();
        IAssetType epicType = metaModel.getAssetType("Epic");

        IAttributeDefinition scopeAttribute = epicType.getAttributeDefinition("Scope");
        FilterTerm scopeTerm = new FilterTerm(scopeAttribute);

        List<Object> projectTokens = new ArrayList<Object>();

        for (Project project : projects) {
            projectTokens.add(project.toString());
        }

        scopeTerm.operate(FilterTerm.Operator.Equal, projectTokens.toArray());
        IAttributeDefinition superAndUpAttribute = epicType.getAttributeDefinition("SuperAndUp").filter(scopeTerm);

        FilterTerm superAndUpTerm = builder.root.Term(superAndUpAttribute);
        superAndUpTerm.notExists();
    }

    private void createSingleScopeFilter(FilterBuilder builder) throws APIException {
        Project project = projects.get(0);

        IMetaModel metaModel = builder.instance.getApiClient().getMetaModel();
        IAssetType epicType = metaModel.getAssetType("Epic");
        IAssetType scopeType = metaModel.getAssetType("Scope");

        IAttributeDefinition notClosedScopeAttribute = scopeType.getAttributeDefinition("AssetState");
        FilterTerm notClosedScopeTerm = new FilterTerm(notClosedScopeAttribute);
        notClosedScopeTerm.notEqual("Closed");
        IAttributeDefinition scopeAttribute = epicType.getAttributeDefinition("Scope.ParentMeAndUp").filter(notClosedScopeTerm);
        FilterTerm scopeTerm = builder.root.Term(scopeAttribute);
        scopeTerm.equal(project.toString());

        IAttributeDefinition superAndUpAttribute = epicType.getAttributeDefinition("SuperAndUp");
        superAndUpAttribute = superAndUpAttribute.filter(scopeTerm);
        FilterTerm superAndUpTerm = builder.root.Term(superAndUpAttribute);
        superAndUpTerm.notExists();
    }

    @Override
    //TODO investigate if this method redundant and filter can work using base class implementation
    void internalModifyState(FilterBuilder builder) {
        if(hasState()) {
            builder.root.and(isActive() ? new TokenTerm("AssetState='Active';AssetType='Epic'") :
                    new TokenTerm("AssetState='Closed';AssetType='Epic'"));
        } else {
            builder.root.and(new TokenTerm("AssetState!='Dead';AssetType='Epic'"));
        }
    }
}
