/*(c) Copyright 2008, VersionOne, Inc. All rights reserved. (c)*/
package com.versionone.om.filters;

import com.versionone.Oid;
import com.versionone.apiclient.AndFilterTerm;
import com.versionone.apiclient.FilterTerm;
import com.versionone.apiclient.GroupFilterTerm;
import com.versionone.apiclient.IAssetType;
import com.versionone.apiclient.IAttributeDefinition;
import com.versionone.apiclient.IFilterTerm;
import com.versionone.apiclient.V1Exception;
import com.versionone.om.AssetID;
import com.versionone.om.Entity;
import com.versionone.om.SDKException;
import com.versionone.om.TransformIterable;
import com.versionone.om.TransformIterable.ITransformerGeneric;
import com.versionone.om.V1Instance;
import com.versionone.om.listvalue.ListValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FilterBuilder {

    private final IAssetType assetType;
    final V1Instance instance;

    public final GroupFilterTerm root = new AndFilterTerm();

    public FilterBuilder(IAssetType assetType, V1Instance instance) {
        this.assetType = assetType;
        this.instance = instance;
    }

    private IAttributeDefinition resolve(String name) {
        return assetType.getAttributeDefinition(name);
    }

    Oid getOid(AssetID id) throws SDKException {
        try {
            return instance.getApiClient().getServices().getOid(id.toString());
        } catch (V1Exception e) {
            throw new SDKException(e);
        }
    }

    public void simple(String name, List<?> values) {
        if (values.size() > 0) {
            root.Term(resolve(name)).equal(values.toArray());
        }
    }

    public <T extends Entity> void relation(String name, Collection<T> values) {
        if (values.size() > 0) {
            ITransformerGeneric<T, Oid> trans = new ITransformerGeneric<T, Oid>() {
                public Oid transform(T e, Class<Oid> dClass) {
                    return (e == null) ? Oid.Null : getOid(e.getID());
                }
            };
            root.Term(resolve(name)).equal(new TransformIterable<T, Oid>(values, trans, Oid.class).toArray());
        }
    }

    public <T extends Entity> void multiRelation(String name, Collection<T> values) {
        if (values.size() > 0) {
            IAttributeDefinition def = resolve(name);
            FilterTerm valuesTerm = null;
            FilterTerm notExistsTerm = null;

            for (T t : values) {
                if (t == null) {
                    if (notExistsTerm == null) {
                        notExistsTerm = new FilterTerm(def);
                        notExistsTerm.notExists();
                    }
                } else {
                    if (valuesTerm == null) {
                        valuesTerm = new FilterTerm(def);
                    }
                    valuesTerm.equal(getOid(t.getID()));
                }
            }

            root.or(new IFilterTerm[]{valuesTerm, notExistsTerm});
        }
    }

    public <T extends ListValue> void listRelation(String name, Collection<String> values, final Class<T> listElementClass) {
        if (values.size() > 0) {
            ITransformerGeneric<String, Oid> trans = new ITransformerGeneric<String, Oid>() {

                public Oid transform(String s, Class<Oid> dClass) {
                    if (s == null) {
                        return Oid.Null;
                    }

                    return getOid(instance.getListValueByName(listElementClass, s).getID());
                }
            };

            root.Term(resolve(name)).equal(new TransformIterable<String, Oid>(values, trans, Oid.class).toArray());
        }
    }

    public void simple(String name, boolean value) {
        root.Term(resolve(name)).equal(new Object[]{value});
    }

    public void comparison(String name, FilterTerm.Operator operator, Object value) {
        FilterTerm term = root.Term(resolve(name));

        switch (operator) {
            case Exists:
                term.exists();
                break;
            case NotExists:
                term.notExists();
                break;
            default:
                term.operate(operator, value);
                break;
        }
    }

    public void comparison(String attributeName, IComparisonSearcher searcher) {
        Map<FilterTerm.Operator, Object> terms = searcher.getTerms();

        for (Map.Entry<FilterTerm.Operator, Object> term : terms.entrySet()) {
            comparison(attributeName, term.getKey(), term.getValue());
        }
    }
}
